package com.foi.nloncar.thesis_manager.rest.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHasher {
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public String hash(String password) {
		return passwordEncoder.encode(password);
	}

	public boolean check(String password, String hash) {
		return passwordEncoder.matches(password, hash);
	}
}
