package com.foi.nloncar.thesis_manager.dto;

import java.util.List;

public record UpdateUserRequest(String email, String firstName, String lastName, List<Integer> roleIds,
								String password) {
}
