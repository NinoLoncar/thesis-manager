package com.foi.nloncar.thesis_manager.rest.security;

import com.foi.nloncar.thesis_manager.dto.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
		authenticationService.login(request, session);
		return ResponseEntity.ok().body("Login successful");
	}
}
