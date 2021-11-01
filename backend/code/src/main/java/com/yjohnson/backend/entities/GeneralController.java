package com.yjohnson.backend.entities;

import com.yjohnson.backend.entities.User.User;
import com.yjohnson.backend.entities.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/")
public class GeneralController {
	private final UserRepository userRepository;

	public GeneralController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Sends the user object that matches the attemptedLogin object. It queries the database for the unique user with the provided email; if no email
	 * was provided, then it searches for the unique user with the provided username.
	 * <p>
	 * When the queried user is present and their password hash matches the provided one, the User object is returned as the response body. Otherwise,
	 * a 404 is returned instead.
	 *
	 * @param attemptedLogin the pseudo-user object that contains either the email or username and a password hash
	 *
	 * @return the User that corresponds to that object, 404 otherwise
	 */
	@PostMapping("/login")
	public ResponseEntity<User> stageLogin(@RequestBody User attemptedLogin) {
		Optional<User> query = userRepository.findUserByEmail(attemptedLogin.email);        // 1
		if (query.isPresent() && Objects.equals(query.get().passwordHash, attemptedLogin.passwordHash)) {
			return new ResponseEntity<>(query.get(), HttpStatus.OK);
		} else {
			query = userRepository.findUserByUsername(attemptedLogin.username);                   // 2
			if (query.isPresent() && Objects.equals(query.get().passwordHash, attemptedLogin.passwordHash)) {
				return new ResponseEntity<>(query.get(), HttpStatus.OK);
			} else {
				if (query.isPresent() && query.get().passwordHash.isEmpty()) return new ResponseEntity<>(attemptedLogin, HttpStatus.BAD_REQUEST);
				else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	}

	@GetMapping("/api/majors")
	public ResponseEntity<?> retrieveMajors() {
		List<String> list = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/static/majors.txt"))) {
			stream.forEach(list::add);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/api/colleges")
	public ResponseEntity<?> retrieveColleges() {
		List<String> list = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/static/colleges.txt"))) {
			stream.forEach(list::add);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}