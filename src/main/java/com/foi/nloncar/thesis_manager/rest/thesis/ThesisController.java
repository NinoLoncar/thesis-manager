package com.foi.nloncar.thesis_manager.rest.thesis;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.dto.CreateThesisRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping
	@RequiresPermission("THESES_READ")
	public ResponseEntity<?> getTheses(@RequestParam(name = "mentorId", required = false) Integer mentorId) {
		return ResponseEntity.ok().body(thesisService.getAllTheses(mentorId));
	}

	@DeleteMapping("/{id}")
	@RequiresPermission("THESIS_DELETE")
	public ResponseEntity<?> deleteThesis(@PathVariable("id") Integer id) {
		thesisService.deleteThesis(id);
		return ResponseEntity.ok().body("Thesis has been deleted");
	}
}
