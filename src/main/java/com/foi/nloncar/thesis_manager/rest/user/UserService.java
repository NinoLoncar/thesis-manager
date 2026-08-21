package com.foi.nloncar.thesis_manager.rest.user;

import com.foi.nloncar.thesis_manager.dto.request.CreateUserRequest;
import com.foi.nloncar.thesis_manager.dto.request.UpdateUserRequest;
import com.foi.nloncar.thesis_manager.dto.resource.UserDetailsDto;
import com.foi.nloncar.thesis_manager.dto.resource.UserDto;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import com.foi.nloncar.thesis_manager.model.Role;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.RoleRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisReservationRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisSubmissionCommentRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisSubmissionRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import com.foi.nloncar.thesis_manager.rest.security.PasswordHasher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordHasher passwordHasher;
	private final ThesisRepository thesisRepository;
	private final ThesisReservationRepository reservationRepository;
	private final ThesisSubmissionRepository submissionRepository;
	private final ThesisSubmissionCommentRepository commentRepository;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordHasher passwordHasher,
						ThesisRepository thesisRepository, ThesisReservationRepository reservationRepository,
						ThesisSubmissionRepository submissionRepository,
						ThesisSubmissionCommentRepository commentRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordHasher = passwordHasher;
		this.thesisRepository = thesisRepository;
		this.reservationRepository = reservationRepository;
		this.submissionRepository = submissionRepository;
		this.commentRepository = commentRepository;
	}

	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream()
				.map(user -> new UserDto(user.getId(), user.getEmail()))
				.toList();
	}

	public UserDto createUser(CreateUserRequest request) {
		validateUser(request.email(), request.firstName(), request.lastName());
		validatePassword(request.password());

		List<Role> roles = roleRepository.findAllById(request.roleIds());

		User user = new User(request.email(), request.firstName(), request.lastName(), passwordHasher.hash(request.password()));
		user.getRoles().addAll(roles);

		User saved = saveUser(user);
		return new UserDto(saved.getId(), saved.getEmail());
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(Integer id) {
		userRepository.findById(id).orElseThrow(
				() -> new NotFoundException("User not found"));

		if (thesisRepository.existsByMentorId(id)) {
			throw new ValidationException("Cannot delete a user that is mentoring a thesis");
		}
		if (thesisRepository.existsByStudentId(id)) {
			throw new ValidationException("Cannot delete a user that is assigned to a thesis");
		}
		if (reservationRepository.existsByStudentId(id)) {
			throw new ValidationException("Cannot delete a user that has thesis reservations");
		}
		if (submissionRepository.existsByStudentId(id) || submissionRepository.existsByReviewedById(id)) {
			throw new ValidationException("Cannot delete a user that has thesis submissions");
		}
		if (commentRepository.existsByAuthorId(id)) {
			throw new ValidationException("Cannot delete a user that has authored comments");
		}

		userRepository.deleteById(id);
	}

	public UserDetailsDto getUserById(Integer id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new NotFoundException("User not found"));

		List<Integer> roleIds = user.getRoles().stream().map(Role::getId).toList();
		return new UserDetailsDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), roleIds);
	}

	public void updateUser(Integer id, UpdateUserRequest request) {
		validateUser(request.email(), request.firstName(), request.lastName());

		User user = userRepository.findById(id).orElseThrow(
				() -> new NotFoundException("User not found"));

		List<Role> roles = roleRepository.findAllById(request.roleIds());

		user.setEmail(request.email());
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.getRoles().clear();
		user.getRoles().addAll(roles);

		if (request.password() != null && !request.password().isBlank()) {
			validatePassword(request.password());
			user.setPassword(passwordHasher.hash(request.password()));
		}

		saveUser(user);
	}

	private void validateUser(String email, String firstName, String lastName) {
		if (email == null || email.isBlank()) {
			throw new ValidationException("Email is required");
		}
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new ValidationException("Email is not valid");
		}
		if (firstName == null || firstName.isBlank()) {
			throw new ValidationException("First name is required");
		}
		if (lastName == null || lastName.isBlank()) {
			throw new ValidationException("Last name is required");
		}
	}

	private void validatePassword(String password) {
		if (password == null || password.isBlank()) {
			throw new ValidationException("Password is required");
		}
		if (password.length() < 6) {
			throw new ValidationException("Password must be at least 6 characters");
		}
	}

}
