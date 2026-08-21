package com.foi.nloncar.thesis_manager.dto.resource;

import java.time.LocalDateTime;

public record ThesisSubmissionCommentDto(Integer id, Integer submissionId, Integer authorId, String authorName,
										  String content, LocalDateTime createdAt) {
}
