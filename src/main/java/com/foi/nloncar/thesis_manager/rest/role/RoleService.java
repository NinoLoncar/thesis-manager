package com.foi.nloncar.thesis_manager.rest.role;

import com.foi.nloncar.thesis_manager.dto.RoleDto;
import com.foi.nloncar.thesis_manager.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<RoleDto> getAllRoles() {
		return roleRepository.findAll().stream()
				.map(role -> new RoleDto(role.getId(), role.getName()))
				.toList();
	}
}
