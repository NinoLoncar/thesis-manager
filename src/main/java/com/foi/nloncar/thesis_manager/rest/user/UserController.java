package com.foi.nloncar.thesis_manager.rest.user;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.dto.CreateUserRequest;
import com.foi.nloncar.thesis_manager.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@RequiresPermission("USERS_READ")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok().body(userService.getAllUsers());
	}

	@PostMapping
	@RequiresPermission("USER_CREATE")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
		return ResponseEntity.ok().body(userService.createUser(request));
	}

	@DeleteMapping("/{id}")
	@RequiresPermission("USER_DELETE")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok().body("User has been deleted");
	}

	@GetMapping("/{id}")
	@RequiresPermission("USERS_READ")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer userId) {
		return ResponseEntity.ok().body(userService.getUserById(userId));
	}

	@PutMapping("/{id}")
	@RequiresPermission("USER_EDIT")
	public ResponseEntity<?> updateUser(@PathVariable("id") Integer userId, @RequestBody UpdateUserRequest request) {
		return ResponseEntity.ok().body("User updated");
	}

}
