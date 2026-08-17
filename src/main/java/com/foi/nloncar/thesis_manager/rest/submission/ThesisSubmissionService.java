package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.dto.resource.ThesisSubmissionDto;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisSubmission;
import com.foi.nloncar.thesis_manager.model.ThesisSubmissionStatus;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisSubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ThesisSubmissionService {

	private final ThesisSubmissionRepository submissionRepository;
	private final ThesisRepository thesisRepository;
	private final FileStorageService fileStorageService;

	public ThesisSubmissionService(ThesisSubmissionRepository submissionRepository,
								   ThesisRepository thesisRepository,
								   FileStorageService fileStorageService) {
		this.submissionRepository = submissionRepository;
		this.thesisRepository = thesisRepository;
		this.fileStorageService = fileStorageService;
	}

	public List<ThesisSubmissionDto> getSubmissionsForThesis(Integer thesisId) {
		return submissionRepository.findByThesisIdOrderByVersionDesc(thesisId).stream()
				.map(this::toDto)
				.toList();
	}

	public void createSubmission(Integer thesisId, String description, MultipartFile file, Integer currentUserId) {
		Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
				() -> new NotFoundException("Thesis not found"));

		if (thesis.getStudent() == null || !thesis.getStudent().getId().equals(currentUserId)) {
			throw new AuthorizationException("Only the thesis's assigned student can submit a version");
		}

		if (!"application/pdf".equals(file.getContentType())) {
			throw new ValidationException("Only PDF files are allowed");
		}

		Integer nextVersion = submissionRepository.findByThesisIdOrderByVersionDesc(thesisId).stream()
				.findFirst()
				.map(submission -> submission.getVersion() + 1)
				.orElse(1);

		String fileName = formatString(thesis.getTitle()) + "-v" + nextVersion + ".pdf";
		String subfolder = thesis.getId().toString() + "_" + thesis.getTitle();
		String filePath = fileStorageService.store(file, subfolder, fileName);

		ThesisSubmission submission = new ThesisSubmission(
				thesis,
				thesis.getStudent(),
				nextVersion,
				filePath,
				fileName,
				description,
				ThesisSubmissionStatus.UNDER_REVIEW
		);

		submissionRepository.save(submission);
	}

	private String formatString(String value) {
		return value.replaceAll("[^a-zA-Z0-9-_]", "_");
	}

	private ThesisSubmissionDto toDto(ThesisSubmission submission) {
		return new ThesisSubmissionDto(
				submission.getId(),
				submission.getThesis().getId(),
				submission.getStudent().getId(),
				submission.getStudent().fullName(),
				submission.getVersion(),
				submission.getFileName(),
				submission.getDescription(),
				submission.getStatus().name(),
				submission.getReviewedBy() != null ? submission.getReviewedBy().fullName() : null,
				submission.getReviewedAt(),
				submission.getCreatedAt()
		);
	}
}
