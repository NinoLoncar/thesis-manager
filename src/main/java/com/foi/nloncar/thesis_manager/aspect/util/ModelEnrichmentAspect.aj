package com.foi.nloncar.thesis_manager.aspect.util;

import com.foi.nloncar.thesis_manager.aspect.ApplicationContextHolder;
import com.foi.nloncar.thesis_manager.rest.security.PermissionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.foi.nloncar.thesis_manager.aspect.security.RedirectAspect;

import java.util.Set;

public aspect ModelEnrichmentAspect {

	declare precedence :RedirectAspect,ModelEnrichmentAspect;

	Object around(Model model):
			execution(* com.foi.nloncar.thesis_manager.gui..*.*(.., Model))
					&& args(.., model) {

		HttpSession session = currentSession();
		Integer userId = (Integer) session.getAttribute("userId");
		Set<String> permissions = userId != null
				? permissionService().getPermissionsForUser(userId)
				: Set.of();

		model.addAttribute("permissions", permissions);
		model.addAttribute("currentUserFullName", session.getAttribute("userFullName"));

		return proceed(model);
	}

	private HttpSession currentSession() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest()
				.getSession();
	}

	private PermissionService permissionService() {
		return ApplicationContextHolder.getBean(PermissionService.class);
	}
}
