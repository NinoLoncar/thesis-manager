package com.foi.nloncar.thesis_manager.dto.request;

import java.util.List;

public record CreateUserRequest(String email, String firstName, String lastName, String password,
								List<Integer> roleIds) {
}
