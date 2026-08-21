package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.dto.resource.ThesisSubmissionDto;
import com.foi.nloncar.thesis_manager.dto.resource.MessageResponse;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisStatus;
import com.foi.nloncar.thesis_manager.model.ThesisSubmission;
import com.foi.nloncar.thesis_manager.model.ThesisSubmissionStatus;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisSubmissionRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import com.foi.nloncar.thesis_manager.rest.thesis.ThesisService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThesisSubmissionService {

	private final ThesisSubmissionRepository submissionRepository;
	private final ThesisRepository thesisRepository;
	private final UserRepository userRepository;
	private final FileStorageService fileStorageService;
	private final ThesisService thesisService;

	public ThesisSubmissionService(ThesisSubmissionRepository submissionRepository,
								   ThesisRepository thesisRepository,
								   UserRepository userRepository,
								   FileStorageService fileStorageService,
								   ThesisService thesisService) {
		this.submissionRepository = submissionRepository;
		this.thesisRepository = thesisRepository;
		this.userRepository = userRepository;
		this.fileStorageService = fileStorageService;
		this.thesisService = thesisService;
	}

	public List<ThesisSubmissionDto> getSubmissionsForThesis(Integer thesisId) {
		return submissionRepository.findByThesisIdOrderByVersionDesc(thesisId).stream()
				.map(this::toDto)
				.toList();
	}

	public ThesisSubmissionDto getSubmissionById(Integer id) {
		return toDto(getSubmissionEntity(id));
	}

	public ThesisSubmission getSubmissionEntity(Integer id) {
		return submissionRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Submission not found"));
	}

	public SubmissionFile loadFile(Integer id) {
		ThesisSubmission submission = getSubmissionEntity(id);

		try {
			Resource resource = new UrlResource(Path.of(submission.getFilePath()).toUri());
			return new SubmissionFile(resource, submission.getFileName());
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read file", e);
		}
	}

	public void createSubmission(Integer thesisId, String description, MultipartFile file, Integer currentUserId) {
		Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
				() -> new NotFoundException("Thesis not found"));

		if (thesis.getStudent() == null || !thesis.getStudent().getId().equals(currentUserId)) {
			throw new AuthorizationException("Only the thesis's assigned student can submit a version");
		}

		if (thesis.getStatus() == ThesisStatus.SUBMITTED) {
			throw new ValidationException("This thesis has already been submitted and accepted");
		}

		if (submissionRepository.existsByThesisIdAndStatus(thesisId, ThesisSubmissionStatus.UNDER_REVIEW)) {
			throw new ValidationException("A previous version is still under review");
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

	public MessageResponse acceptSubmission(Integer id, Integer currentUserId) {
		ThesisSubmission submission = getSubmissionEntity(id);
		validateReview(submission, currentUserId);

		User reviewer = userRepository.findById(currentUserId).orElseThrow(
				() -> new NotFoundException("User not found"));

		submission.setStatus(ThesisSubmissionStatus.ACCEPTED);
		submission.setReviewedBy(reviewer);
		submission.setReviewedAt(LocalDateTime.now());
		submissionRepository.save(submission);

		Thesis thesis = submission.getThesis();
		thesis.setStatus(ThesisStatus.SUBMITTED);
		thesis.setSubmittedAt(LocalDateTime.now());
		thesisService.saveThesis(thesis);

		return new MessageResponse("Submission accepted as the final version");
	}

	public MessageResponse requestChanges(Integer id, Integer currentUserId) {
		ThesisSubmission submission = getSubmissionEntity(id);
		validateReview(submission, currentUserId);

		User reviewer = userRepository.findById(currentUserId).orElseThrow(
				() -> new NotFoundException("User not found"));

		submission.setStatus(ThesisSubmissionStatus.CHANGES_REQUESTED);
		submission.setReviewedBy(reviewer);
		submission.setReviewedAt(LocalDateTime.now());
		submissionRepository.save(submission);

		return new MessageResponse("Changes requested");
	}

	private void validateReview(ThesisSubmission submission, Integer currentUserId) {
		if (!submission.getThesis().getMentor().getId().equals(currentUserId)) {
			throw new AuthorizationException("Only the thesis's mentor can review submissions");
		}

		if (submission.getStatus() != ThesisSubmissionStatus.UNDER_REVIEW) {
			throw new ValidationException("Only submissions under review can be reviewed");
		}
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
