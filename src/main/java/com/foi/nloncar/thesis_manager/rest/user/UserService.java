package com.foi.nloncar.thesis_manager.rest.user;

import com.foi.nloncar.thesis_manager.dto.CreateUserRequest;
import com.foi.nloncar.thesis_manager.dto.UserDto;
import com.foi.nloncar.thesis_manager.model.Role;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.RoleRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream()
				.map(user -> new UserDto(user.getId(), user.getEmail()))
				.toList();
	}

	public UserDto createUser(CreateUserRequest request) {
		List<Role> roles = roleRepository.findAllById(request.roleIds());

		User user = new User(request.email(), request.firstName(), request.lastName(), request.password());
		user.getRoles().addAll(roles);

		User saved = saveUser(user);
		return new UserDto(saved.getId(), saved.getEmail());
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}
}
