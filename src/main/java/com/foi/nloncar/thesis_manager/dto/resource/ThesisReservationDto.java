package com.foi.nloncar.thesis_manager.dto.resource;

public record ThesisReservationDto(Integer id, Integer thesisId, String thesisTitle,
									Integer studentId, String studentName, String status) {
}
