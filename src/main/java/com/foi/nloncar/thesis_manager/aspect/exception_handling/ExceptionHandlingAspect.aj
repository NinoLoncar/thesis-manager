package com.foi.nloncar.thesis_manager.aspect.exception_handling;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.aspect.PermissionCheckAspect;
import com.foi.nloncar.thesis_manager.exception.AuthenticationException;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import com.foi.nloncar.thesis_manager.rest.security.AuthenticationController;
import org.springframework.http.ResponseEntity;

public aspect ExceptionHandlingAspect {

	declare precedence : ExceptionHandlingAspect, PermissionCheckAspect;

	pointcut protectedMethods():
			execution(* AuthenticationController.login(..))
					|| @annotation(RequiresPermission);

	Object around(): protectedMethods() {
		try {
			return proceed();
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}

	Object around(): protectedMethods() {
		try {
			return proceed();
		} catch (AuthorizationException e) {
			return ResponseEntity.status(403).body(e.getMessage());
		}
	}

	Object around(): protectedMethods() {
		try {
			return proceed();
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	Object around(): protectedMethods() {
		try {
			return proceed();
		} catch (ValidationException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}
}
