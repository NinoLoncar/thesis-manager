package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.dto.resource.ThesisSubmissionDto;
import com.foi.nloncar.thesis_manager.model.ThesisSubmission;
import com.foi.nloncar.thesis_manager.repository.ThesisSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThesisSubmissionService {

	private final ThesisSubmissionRepository submissionRepository;

	public ThesisSubmissionService(ThesisSubmissionRepository submissionRepository) {
		this.submissionRepository = submissionRepository;
	}

	public List<ThesisSubmissionDto> getSubmissionsForThesis(Integer thesisId) {
		return submissionRepository.findByThesisIdOrderByVersionDesc(thesisId).stream()
				.map(this::toDto)
				.toList();
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
