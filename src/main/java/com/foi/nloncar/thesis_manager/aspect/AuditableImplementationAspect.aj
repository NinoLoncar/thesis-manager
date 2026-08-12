package com.foi.nloncar.thesis_manager.aspect;

import com.foi.nloncar.thesis_manager.model.Auditable;
import com.foi.nloncar.thesis_manager.model.User;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

public aspect AuditableImplementationAspect {

	declare parents:User implements Auditable;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime Auditable.createdAt;

	@Column(name = "created_by")
	private String Auditable.createdBy;

	@Column(name = "updated_at")
	private LocalDateTime Auditable.updatedAt;

	@Column(name = "updated_by")
	private String Auditable.updatedBy;

	public LocalDateTime Auditable.getCreatedAt() {
		return this.createdAt;
	}

	public void Auditable.setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String Auditable.getCreatedBy() {
		return this.createdBy;
	}

	public void Auditable.setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime Auditable.getUpdatedAt() {
		return this.updatedAt;
	}

	public void Auditable.setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String Auditable.getUpdatedBy() {
		return this.updatedBy;
	}

	public void Auditable.setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}
