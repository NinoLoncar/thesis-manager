package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.dto.request.CreateCommentRequest;
import com.foi.nloncar.thesis_manager.dto.resource.ThesisSubmissionCommentDto;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisSubmission;
import com.foi.nloncar.thesis_manager.model.ThesisSubmissionComment;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.ThesisSubmissionCommentRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThesisSubmissionCommentService {

	private final ThesisSubmissionCommentRepository commentRepository;
	private final ThesisSubmissionService submissionService;
	private final UserRepository userRepository;

	public ThesisSubmissionCommentService(ThesisSubmissionCommentRepository commentRepository,
										   ThesisSubmissionService submissionService,
										   UserRepository userRepository) {
		this.commentRepository = commentRepository;
		this.submissionService = submissionService;
		this.userRepository = userRepository;
	}

	public List<ThesisSubmissionCommentDto> getCommentsForSubmission(Integer submissionId) {
		return commentRepository.findBySubmissionIdOrderByCreatedAtAsc(submissionId).stream()
				.map(this::toDto)
				.toList();
	}

	public void createComment(CreateCommentRequest request, Integer currentUserId) {
		ThesisSubmission submission = submissionService.getSubmissionEntity(request.submissionId());
		Thesis thesis = submission.getThesis();

		boolean isMentor = thesis.getMentor().getId().equals(currentUserId);
		boolean isStudent = thesis.getStudent() != null && thesis.getStudent().getId().equals(currentUserId);

		if (!isMentor && !isStudent) {
			throw new AuthorizationException("Only the thesis's mentor or student can comment on this submission");
		}

		User author = userRepository.findById(currentUserId).orElseThrow(
				() -> new NotFoundException("User not found"));

		ThesisSubmissionComment comment = new ThesisSubmissionComment(submission, author, request.content());
		commentRepository.save(comment);
	}

	private ThesisSubmissionCommentDto toDto(ThesisSubmissionComment comment) {
		return new ThesisSubmissionCommentDto(
				comment.getId(),
				comment.getSubmission().getId(),
				comment.getAuthor().getId(),
				comment.getAuthor().fullName(),
				comment.getContent(),
				comment.getCreatedAt()
		);
	}
}
