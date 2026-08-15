package com.foi.nloncar.thesis_manager.gui.controllers;

import com.foi.nloncar.thesis_manager.annotation.RequiresPagePermission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThesesController {

	@GetMapping("/theses/create")
	@RequiresPagePermission("THESIS_CREATE")
	public String createThesisPage(Model model) {
		return "thesis/create-thesis";
	}
}
