package com.foi.nloncar.thesis_manager.gui.controllers;

import com.foi.nloncar.thesis_manager.annotation.RequiresPagePermission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

	@GetMapping("/users")
	@RequiresPagePermission("USERS_READ")
	public String usersPage(Model model) {
		return "user/users";
	}
}
