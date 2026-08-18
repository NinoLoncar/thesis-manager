package com.foi.nloncar.thesis_manager.rest.submission;

import org.springframework.core.io.Resource;

public record SubmissionFile(Resource resource, String fileName) {
}
