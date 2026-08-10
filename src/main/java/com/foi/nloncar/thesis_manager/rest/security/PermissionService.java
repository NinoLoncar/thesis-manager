package com.foi.nloncar.thesis_manager.rest.security;

import com.foi.nloncar.thesis_manager.model.Permission;
import com.foi.nloncar.thesis_manager.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

	private final PermissionRepository permissionRepository;

	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	public Set<String> getPermissionsForUser(Integer userId) {
		return permissionRepository.findAllByUserId(userId).stream()
				.map(Permission::getName)
				.collect(Collectors.toSet());
	}
}
