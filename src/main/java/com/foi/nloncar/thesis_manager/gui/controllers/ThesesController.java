package com.foi.nloncar.thesis_manager.gui.controllers;

import com.foi.nloncar.thesis_manager.annotation.RequiresPagePermission;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ThesesController {

	@GetMapping("/theses/create")
	@RequiresPagePermission("THESIS_CREATE")
	public String createThesisPage(Model model) {
		return "thesis/create-thesis";
	}

	@GetMapping("/theses/mentored")
	@RequiresPagePermission("THESIS_CREATE")
	public String mentoredThesesPage(HttpSession session, Model model) {
		model.addAttribute("mentorId", session.getAttribute("userId"));
		return "thesis/mentored-theses";
	}

	@GetMapping("/theses/{id}/edit")
	@RequiresPagePermission("THESIS_EDIT")
	public String editThesisPage(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("thesisId", id);
		return "thesis/edit-thesis";
	}

	@GetMapping("/theses")
	@RequiresPagePermission("THESES_READ")
	public String allThesesPage(Model model) {
		return "thesis/theses";
	}

	@GetMapping("/theses/{id}")
	@RequiresPagePermission("THESES_READ")
	public String thesisDetailsPage(@PathVariable("id") Integer id, HttpSession session, Model model) {
		model.addAttribute("thesisId", id);
		model.addAttribute("currentUserId", session.getAttribute("userId"));
		return "thesis/thesis-details";
	}

	@GetMapping("/theses/{id}/submissions")
	@RequiresPagePermission("SUBMISSIONS_READ")
	public String submissionsPage(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("thesisId", id);
		return "submission/submissions";
	}

	@GetMapping("/theses/{id}/submissions/create")
	@RequiresPagePermission("SUBMISSION_CREATE")
	public String createSubmissionPage(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("thesisId", id);
		return "submission/create-submission";
	}

	@GetMapping("/theses/{id}/submissions/{submissionId}")
	@RequiresPagePermission("SUBMISSIONS_READ")
	public String submissionDetailsPage(@PathVariable("id") Integer id,
										 @PathVariable("submissionId") Integer submissionId,
										 HttpSession session,
										 Model model) {
		model.addAttribute("submissionId", submissionId);
		model.addAttribute("currentUserId", session.getAttribute("userId"));
		return "submission/submission-details";
	}
}
