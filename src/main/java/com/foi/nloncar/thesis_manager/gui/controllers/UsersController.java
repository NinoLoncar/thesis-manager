package com.foi.nloncar.thesis_manager.gui.controllers;

import com.foi.nloncar.thesis_manager.annotation.RequiresPagePermission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UsersController {

	@GetMapping("/users")
	@RequiresPagePermission("USERS_READ")
	public String usersPage(Model model) {
		return "user/users";
	}

	@GetMapping("/users/create")
	@RequiresPagePermission("USER_CREATE")
	public String createUserPage(Model model) {
		return "user/create-user";
	}

	@GetMapping("/users/{id}/edit")
	@RequiresPagePermission("USER_EDIT")
	public String editUserPage(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("userId", id);
		return "user/edit-user";
	}
}
