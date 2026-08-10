package com.foi.nloncar.thesis_manager.aspect;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public aspect LoginRedirectAspect {

	Object around():
			execution(* com.foi.nloncar.thesis_manager.gui.controllers..*(..))
					&& !execution(* com.foi.nloncar.thesis_manager.gui.controllers.LoginController.login(..))  {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest()
				.getSession();

		if (session.getAttribute("userId") == null) {
			return "redirect:/login";
		}

		return proceed();
	}
}
