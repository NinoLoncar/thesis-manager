package com.foi.nloncar.thesis_manager.rest.user;

import com.foi.nloncar.thesis_manager.dto.UserDto;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream()
				.map(user ->
						new UserDto(user.getId(), user.getEmail()))
				.toList();
	}

}
