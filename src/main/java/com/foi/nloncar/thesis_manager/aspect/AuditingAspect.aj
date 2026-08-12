package com.foi.nloncar.thesis_manager.aspect;

import com.foi.nloncar.thesis_manager.model.Auditable;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

public aspect AuditingAspect {

	Object around(Auditable entity):
			execution(* com.foi.nloncar.thesis_manager.rest.user.UserService.saveUser(..))
					&& args(entity) {

		LocalDateTime now = LocalDateTime.now();
		String currentUser = getCurrentUser();

		if (entity.getCreatedAt() == null) {
			entity.setCreatedAt(now);
			entity.setCreatedBy(currentUser);
		}
		entity.setUpdatedAt(now);
		entity.setUpdatedBy(currentUser);

		return proceed(entity);
	}

	private String getCurrentUser() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest()
				.getSession();
		return (String) session.getAttribute("userEmail");
	}
}
