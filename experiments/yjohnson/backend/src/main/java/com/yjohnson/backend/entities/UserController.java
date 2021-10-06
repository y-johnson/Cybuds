package com.yjohnson.backend.entities;

import net.bytebuddy.dynamic.DynamicType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	/**
	 * Adds a given user to the database.
	 * <p>
	 * If the given user object contains repeated unique fields, then those fields are returned alongside a 409 status code.
	 *
	 * @param newUser the user object to insert into the database.
	 *
	 * @return the user and a 201 status code if successful, otherwise the incompatible field and a 409 status code.
	 */
	@PostMapping()
	public ResponseEntity<?> addUser(@RequestBody User newUser) {
		// This one can very likely be optimized, but as of 10/2 the only idea that came to mind was a JOIN query (same cost).
		if (!userRepository.findUserByEmail(newUser.email).isEmpty()) {              //1
			return new ResponseEntity<>(newUser.email, HttpStatus.CONFLICT);
		} else if (!userRepository.findUserByUsername(newUser.username).isEmpty()) { //2
			return new ResponseEntity<>(newUser.username, HttpStatus.CONFLICT);
		} else {
			userRepository.save(newUser);                                            //3
			return new ResponseEntity<>(newUser, HttpStatus.CREATED);
		}
	}

	/**
	 * Deletes the user that corresponds to the given ID from the database.
	 *
	 * @param id the ID of th=e user to be deleted.
	 *
	 * @return if successfully deleted, a response entity with the deleted user's ID; otherwise, a 404 reponse code.
	 */
//	@GetMapping()
//	public ResponseEntity<User> getUser(@RequestParam("id") Long id) {
//		Optional<User> optionalUser = userRepository.findById(id); // 1
//		/* Don't even ask me, this is apparently the best way to handle Optionals */
//		return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//	}

	@GetMapping()
	public ResponseEntity<User> getUser(@RequestParam Map<String, String> parameters) {
		Optional<User> optionalUser;
		if (parameters.containsKey("id")) {
			optionalUser = userRepository.findById(Long.valueOf(parameters.get("id")));     // 1
		} else if (parameters.containsKey("username")){
			optionalUser = userRepository.findUserByUsername(parameters.get("username"));   // 1
		} else if (parameters.containsKey("email")){
			optionalUser = userRepository.findUserByEmail(parameters.get("email"));         // 1
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		/* Don't even ask me, this is apparently the best way to handle Optionals */
		return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Deletes the user that corresponds to the given ID from the database.
	 *
	 * @param id the ID of th=e user to be deleted.
	 *
	 * @return if successfully deleted, a response entity with the deleted user's ID; otherwise, a 404 reponse code.
	 */
	@DeleteMapping()
	public ResponseEntity<Long> deleteUser(@RequestParam("id") Long id) {
		/*
		 * For the sake of performance, the method will only query the database twice (once to figure out if it exists and once to delete it if it
		 * does).
		 * The findById method returns an Optional, which is really just the same idea as returning a null value in C.
		 */
		Optional<User> optionalUser = userRepository.findById(id);  //1
		if (optionalUser.isPresent()) {
			Long uid = optionalUser.get().getId();
			userRepository.deleteById(uid);                         //2
			return new ResponseEntity<>(uid, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Presents all users in the database.
	 *
	 * @return all the users in the databsae
	 */
	@GetMapping(path = "/all")
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();    //1
	}
}
