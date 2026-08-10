package com.foi.nloncar.thesis_manager.rest.security;

import com.foi.nloncar.thesis_manager.dto.LoginRequest;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;

	public AuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void login(LoginRequest request, HttpSession session) {
		User user = userRepository.findByEmail(request.email()).orElseThrow(
				() -> new AuthenticationException("Invalid email or password")
		);

		if (!user.getPassword().equals(request.password())) {
			throw new AuthenticationException("Invalid email or password");
		}

		session.setAttribute("userId", user.getId());
	}
}
