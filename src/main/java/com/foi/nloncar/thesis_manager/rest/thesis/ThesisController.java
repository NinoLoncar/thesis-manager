package com.foi.nloncar.thesis_manager.rest.thesis;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.dto.CreateThesisRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theses")
public class ThesisController {

	private final ThesisService thesisService;

	public ThesisController(ThesisService thesisService) {
		this.thesisService = thesisService;
	}

	@PostMapping
	@RequiresPermission("THESIS_CREATE")
	public ResponseEntity<?> createThesis(@RequestBody CreateThesisRequest request, HttpSession session) {
		Integer mentorId = (Integer) session.getAttribute("userId");
		return ResponseEntity.ok().body(thesisService.createThesis(request, mentorId));
	}
}
