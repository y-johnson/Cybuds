package com.yjohnson.backend.entities;

import com.yjohnson.backend.entities.User.User;
import com.yjohnson.backend.entities.User.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping()
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
		Optional<User> query = userRepository.findByEmail(attemptedLogin.email);        // 1
		if (query.isPresent() && Objects.equals(query.get().passwordHash, attemptedLogin.passwordHash)) {
			return new ResponseEntity<>(query.get(), HttpStatus.OK);
		} else {
			query = userRepository.findByUsername(attemptedLogin.username);                   // 2
			if (query.isPresent() && Objects.equals(query.get().passwordHash, attemptedLogin.passwordHash)) {
				return new ResponseEntity<>(query.get(), HttpStatus.OK);
			} else {
				if (query.isPresent() && query.get().passwordHash.isEmpty()) return new ResponseEntity<>(attemptedLogin, HttpStatus.BAD_REQUEST);
				else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	}

	/**
	 * Adds a given user to the database. This method sanitizes the input to a degree; the fields will be trimmed, names will be capitalized in Title
	 * case (e.g. "marTHa" -> "Martha"), the username and email will be lowercase and the phone number will be reduced to a max of 10 digits.
	 * <p>
	 * If the given object contains repeated unique fields, then those fields are returned alongside a CONFLICT status code.
	 *
	 * @param toRegister the user object to insert into the database.
	 *
	 * @return a response entity with the created {@code User} object (CREATED) or the conflicting value (CONFLICT).
	 */
	@PostMapping("/register")
	public ResponseEntity<?> stageRegistration(@RequestBody Optional<User> toRegister) {
		try {
			if (toRegister.isPresent() && toRegister.get().validate()) {
				toRegister.get().setFirstName(StringUtils.trimWhitespace(StringUtils.capitalize(toRegister.get().getFirstName().toLowerCase())).substring(0,15));
				toRegister.get().setMiddleName(StringUtils.trimWhitespace(StringUtils.capitalize(toRegister.get().getMiddleName().toLowerCase())));
				toRegister.get().setLastName(StringUtils.trimWhitespace(StringUtils.capitalize(toRegister.get().getLastName().toLowerCase())));
				toRegister.get().setUsername(StringUtils.trimAllWhitespace(toRegister.get().getUsername().toLowerCase()));
				toRegister.get().setEmail(StringUtils.trimAllWhitespace(toRegister.get().getEmail().toLowerCase()));
				toRegister.get().setPhoneNumber(StringUtils.deleteAny(toRegister.get().getPhoneNumber(), "-()/_-+ ").substring(0, 10));

				if (userRepository.findByEmail(toRegister.get().getEmail()).isPresent()) {               // 1
					return new ResponseEntity<>(toRegister.get().getEmail(), HttpStatus.CONFLICT);
				} else if (userRepository.findByUsername(toRegister.get().getUsername()).isPresent()) {  // 2
					return new ResponseEntity<>(toRegister.get().getUsername(), HttpStatus.CONFLICT);
				}
				return new ResponseEntity<>(userRepository.save(toRegister.get()), HttpStatus.CREATED);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/api/majors")
	public ResponseEntity<?> retrieveMajors() {
		List<String> list = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("static/majors.txt"))
		                                                          .getFile()))) {
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
		try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("static/colleges.txt"))
		                                                          .getFile()))) {
			stream.forEach(list::add);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}