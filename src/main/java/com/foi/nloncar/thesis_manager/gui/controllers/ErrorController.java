package com.foi.nloncar.thesis_manager.gui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	@GetMapping("/unauthorized")
	public String unauthorizedPage() {
		return "error/unauthorized";
	}
}
