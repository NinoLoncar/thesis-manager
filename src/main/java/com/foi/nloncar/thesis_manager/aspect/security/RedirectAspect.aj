package com.foi.nloncar.thesis_manager.aspect.security;

import com.foi.nloncar.thesis_manager.annotation.RequiresPagePermission;
import com.foi.nloncar.thesis_manager.aspect.ApplicationContextHolder;
import com.foi.nloncar.thesis_manager.rest.security.PermissionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

public aspect RedirectAspect {

	Object around():
			execution(* com.foi.nloncar.thesis_manager.gui.controllers..*(..))
					&& !execution(* com.foi.nloncar.thesis_manager.gui.controllers.LoginController.login(..))  {

		if (getCurrentUserId() == null) {
			return "redirect:/login";
		}

		return proceed();
	}

	Object around(RequiresPagePermission requiresPagePermission):
			execution(* *(..)) && @annotation(requiresPagePermission) {

		Integer userId = getCurrentUserId();
		if (userId == null) {
			return "redirect:/login";
		}

		Set<String> permissions = permissionService().getPermissionsForUser(userId);
		if (!permissions.contains(requiresPagePermission.value())) {
			return "redirect:/unauthorized";
		}

		return proceed(requiresPagePermission);
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
