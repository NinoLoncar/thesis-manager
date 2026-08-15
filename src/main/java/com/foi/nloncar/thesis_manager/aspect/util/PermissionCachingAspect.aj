package com.foi.nloncar.thesis_manager.aspect.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.Set;

public aspect PermissionCachingAspect {

	private final Cache<Integer, Set<String>> cache = Caffeine.newBuilder()
			.maximumSize(500)
			.expireAfterWrite(Duration.ofMinutes(15))
			.build();

	Object around(Integer userId):
			execution(* com.foi.nloncar.thesis_manager.rest.security.PermissionService.getPermissionsForUser(java.lang.Integer))
					&& args(userId) {

		Set<String> cached = cache.getIfPresent(userId);
		if (cached != null) {
			return cached;
		}

		Set<String> result = (Set<String>) proceed(userId);
		cache.put(userId, result);
		return result;
	}
}
