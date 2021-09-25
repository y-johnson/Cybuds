package com.yjohnson.backend.entities;

import com.yjohnson.backend.entities.User;
import com.yjohnson.backend.entities.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping(path = "/all")
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
