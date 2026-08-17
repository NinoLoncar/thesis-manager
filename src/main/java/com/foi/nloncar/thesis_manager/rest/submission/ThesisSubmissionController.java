package com.foi.nloncar.thesis_manager.rest.submission;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
