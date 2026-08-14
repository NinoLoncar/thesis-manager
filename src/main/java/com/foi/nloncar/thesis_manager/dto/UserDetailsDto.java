package com.foi.nloncar.thesis_manager.dto;

import java.util.List;

public record UserDetailsDto(Integer id, String email, String firstName, String lastName, List<Integer> roleIds) {
}
