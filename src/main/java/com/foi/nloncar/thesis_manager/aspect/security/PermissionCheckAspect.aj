package com.foi.nloncar.thesis_manager.aspect.security;

import com.foi.nloncar.thesis_manager.aspect.ApplicationContextHolder;
import com.foi.nloncar.thesis_manager.exception.AuthenticationException;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.rest.security.PermissionService;
import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

public aspect PermissionCheckAspect {

	Object around(RequiresPermission requiresPermission):
			execution(* com.foi.nloncar.thesis_manager.rest..*.*(..))
					&& @annotation(requiresPermission) {

		Integer userId = getCurrentUserId();
		if (userId == null) {
			throw new AuthenticationException("Not logged in");
		}

		Set<String> permissions = permissionService().getPermissionsForUser(userId);
		if (!permissions.contains(requiresPermission.value())) {
			throw new AuthorizationException("Missing permission: " + requiresPermission.value());
		}

		return proceed(requiresPermission);
	}

	private Integer getCurrentUserId() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest()
				.getSession();
		return (Integer) session.getAttribute("userId");
	}

	private PermissionService permissionService() {
		return ApplicationContextHolder.getBean(PermissionService.class);
	}
}
