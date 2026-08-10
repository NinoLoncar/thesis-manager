package com.foi.nloncar.thesis_manager.aspect.exception_handling;

import com.foi.nloncar.thesis_manager.rest.security.AuthenticationException;
import org.springframework.http.ResponseEntity;

public aspect ExceptionHandlingAspect {

	Object around(): execution(* AuthenticationController.login(..)) {
		try {
			return proceed();
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}
}
