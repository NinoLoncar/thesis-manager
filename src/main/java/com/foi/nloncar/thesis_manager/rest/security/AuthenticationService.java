package com.foi.nloncar.thesis_manager.rest.security;

import com.foi.nloncar.thesis_manager.dto.request.LoginRequest;
import com.foi.nloncar.thesis_manager.exception.AuthenticationException;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordHasher passwordHasher;

	public AuthenticationService(UserRepository userRepository, PasswordHasher passwordHasher) {
		this.userRepository = userRepository;
		this.passwordHasher = passwordHasher;
	}

	public void login(LoginRequest request, HttpSession session) {
		User user = userRepository.findByEmail(request.email()).orElseThrow(
				() -> new AuthenticationException("Invalid email or password")
		);

		if (!passwordHasher.check(request.password(), user.getPassword())) {
			throw new AuthenticationException("Invalid email or password");
		}

		session.setAttribute("userId", user.getId());
		session.setAttribute("userEmail", user.getEmail());
	}
}
