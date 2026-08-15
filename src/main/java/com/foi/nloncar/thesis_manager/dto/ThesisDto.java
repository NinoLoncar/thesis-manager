package com.foi.nloncar.thesis_manager.dto;

import java.time.LocalDateTime;

public record ThesisDto(Integer id, String title, String abstractText, String type, String status,
						Integer mentorId, String mentorName, Integer studentId, String studentName) {
}
