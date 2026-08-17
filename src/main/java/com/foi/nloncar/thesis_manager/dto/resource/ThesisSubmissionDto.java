package com.foi.nloncar.thesis_manager.dto.resource;

import java.time.LocalDateTime;

public record ThesisSubmissionDto(Integer id, Integer thesisId, Integer studentId, String studentName,
								   Integer version, String fileName, String description, String status,
								   String reviewedByName, LocalDateTime reviewedAt, LocalDateTime createdAt) {
}
