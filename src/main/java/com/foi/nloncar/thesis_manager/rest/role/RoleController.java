package com.foi.nloncar.thesis_manager.rest.role;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/api/roles")
	@RequiresPermission("ROLES_READ")
	public ResponseEntity<?> getRoles() {
		return ResponseEntity.ok().body(roleService.getAllRoles());
	}
}
