package com.yjohnson.backend.entities.Match;

import com.yjohnson.backend.entities.Group.GroupType;
import com.yjohnson.backend.entities.User.User;
import com.yjohnson.backend.entities.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users/{id}/match")
public class MatchController {
	private final UserService userService;
	private final MatchService matchService;

	public MatchController(UserService userService, MatchService matchService) {
		this.userService = userService;
		this.matchService = matchService;
	}

	/**
	 * Returns an ordered list of all user IDs in descending order of match score.
	 *
	 * @param id the user ID of the current user
	 *
	 * @return an ordered descending list of the users that match the most with the current user.
	 */
	@Operation(summary = "Matches a user with others based on the group type specified.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Matched the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@GetMapping()
	public ResponseEntity<?> matchUser(@PathVariable Optional<Long> id) {
		if (id.isPresent()) {
			Optional<User> optionalCurrentUser = userService.getUserByID(id.get());
			if (optionalCurrentUser.isPresent()) {
				return new ResponseEntity<>(
						matchService.matchUser(optionalCurrentUser.get(), userService.getAllUsersFromDB()),
						HttpStatus.OK
				);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Returns an ordered list of all user IDs that conform to the chosen constraint in descending order of match score.
	 *
	 * @param id     the user ID of the current user
	 * @param choice the constraint to match against
	 *
	 * @return an ordered descending list of the users that match the most with the current user.
	 */
	@Operation(summary = "Matches a user with others based on the group type specified.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Matched the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@GetMapping("/{choice}")
	public ResponseEntity<?> matchUserByChoice(@PathVariable Optional<Long> id, @PathVariable GroupType choice) {
		if (id.isPresent()) {
			Optional<User> optionalCurrentUser = userService.getUserByID(id.get());
			if (optionalCurrentUser.isPresent()) {
				return new ResponseEntity<>(
						matchService.matchUserByChoice(choice, optionalCurrentUser.get(), userService.getAllUsersFromDB()),
						HttpStatus.OK
				);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Matches a user randomly based on their ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Matched and returned user randomly", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "204", description = "No match found"),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@GetMapping("/random")
	public ResponseEntity<?> randomMatch(@PathVariable Optional<Long> id) {
		if (id.isPresent()) {
			Optional<User> optionalCurrentUser = userService.getUserByID(id.get());
			Iterable<User> allUsers = userService.getAllUsersFromDB();
			if (optionalCurrentUser.isPresent()) {
				return matchService.matchUserRandomly(optionalCurrentUser.get(), allUsers)
				                   .map(selected -> new ResponseEntity<>(selected, HttpStatus.OK))
				                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


}