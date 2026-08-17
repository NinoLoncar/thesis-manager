package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.dto.resource.MessageResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/submissions")
public class ThesisSubmissionController {

	private final ThesisSubmissionService submissionService;

	public ThesisSubmissionController(ThesisSubmissionService submissionService) {
		this.submissionService = submissionService;
	}

	@GetMapping
	@RequiresPermission("SUBMISSIONS_READ")
	public ResponseEntity<?> getSubmissions(@RequestParam(name = "thesisId") Integer thesisId) {
		return ResponseEntity.ok().body(submissionService.getSubmissionsForThesis(thesisId));
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@RequiresPermission("SUBMISSION_CREATE")
	public ResponseEntity<?> createSubmission(
			@RequestParam("thesisId") Integer thesisId,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam("file") MultipartFile file,
			HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		submissionService.createSubmission(thesisId, description, file, currentUserId);
		return ResponseEntity.ok().body(new MessageResponse("Submission created successfully"));
	}
}
