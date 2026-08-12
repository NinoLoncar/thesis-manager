package com.foi.nloncar.thesis_manager.dto;

import java.util.List;

public record CreateUserRequest(String email, String firstName, String lastName, String password, List<Integer> roleIds) {
}
