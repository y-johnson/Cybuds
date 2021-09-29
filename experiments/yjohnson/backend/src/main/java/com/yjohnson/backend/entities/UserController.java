package com.yjohnson.backend.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping()
	public ResponseEntity<String> addUser(@RequestBody User newUser) {
		if (!userRepository.findUserByEmail(newUser.email).isEmpty()) {
			return new ResponseEntity<>("Specified email (" + newUser.email + ") is already in use!", HttpStatus.CONFLICT);
		} else if (!userRepository.findUserByUsername(newUser.username).isEmpty()) {
			return new ResponseEntity<>("Specified username (" + newUser.username + ") is already in use!", HttpStatus.CONFLICT);
		} else {
			userRepository.save(newUser);
			return new ResponseEntity<>("Successfully added " + newUser + " to the database.", HttpStatus.CREATED);
		}
	}

	@DeleteMapping()
	public void deleteUser(@RequestParam("id") Long id) {
		userRepository.deleteById(id);
	}


	@GetMapping(path = "/all")
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
