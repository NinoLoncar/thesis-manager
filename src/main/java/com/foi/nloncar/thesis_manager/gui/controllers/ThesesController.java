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
}
