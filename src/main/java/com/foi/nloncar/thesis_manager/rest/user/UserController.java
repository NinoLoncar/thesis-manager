package com.foi.nloncar.thesis_manager.rest.user;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/api/users")
	@RequiresPermission("USERS_READ")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok().body(userService.getAllUsers());
	}
}
