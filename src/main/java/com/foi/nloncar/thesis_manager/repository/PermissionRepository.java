package com.foi.nloncar.thesis_manager.repository;

import com.foi.nloncar.thesis_manager.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

	@Query("select p from Permission p join p.roles r join r.users u where u.id = :userId")
	Set<Permission> findAllByUserId(@Param("userId") Integer userId);
}
