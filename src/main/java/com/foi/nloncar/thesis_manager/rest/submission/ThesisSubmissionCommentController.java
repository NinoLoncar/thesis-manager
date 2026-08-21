package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.dto.request.CreateCommentRequest;
import com.foi.nloncar.thesis_manager.dto.resource.MessageResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submission-comments")
public class ThesisSubmissionCommentController {

	private final ThesisSubmissionCommentService commentService;

	public ThesisSubmissionCommentController(ThesisSubmissionCommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping
	@RequiresPermission("SUBMISSIONS_READ")
	public ResponseEntity<?> getComments(@RequestParam(name = "submissionId") Integer submissionId) {
		return ResponseEntity.ok().body(commentService.getCommentsForSubmission(submissionId));
	}

	@PostMapping
	@RequiresPermission("COMMENT_CREATE")
	public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		commentService.createComment(request, currentUserId);
		return ResponseEntity.ok().body(new MessageResponse("Comment added"));
	}
}
