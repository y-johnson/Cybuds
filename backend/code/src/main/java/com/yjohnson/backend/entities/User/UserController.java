package com.yjohnson.backend.entities.User;

import com.yjohnson.backend.entities.DB_Relations.R_UserGroup;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.Group.GroupType;
import com.yjohnson.backend.exceptions.CybudsActionResultsInConflictException;
import com.yjohnson.backend.exceptions.CybudsEntityByIdNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	private final MatchService matchService;
	private final UserService userService;

	public UserController(MatchService matchService, UserService userService) {
		this.matchService = matchService;
		this.userService = userService;
	}

	/**
	 * Retrieves a {@code User} from the database whose ID or username matches the given path variable. Only one of the two is required.
	 *
	 * @param identifier the id or username of the user to retrieve
	 *
	 * @return the {@code User} object that corresponds with the path variable (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@Operation(summary = "Get a user by its ID or username")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Got the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@GetMapping("/{identifier}")
	public ResponseEntity<?> getUser(@PathVariable Optional<String> identifier) {
		Optional<User> optionalUser;
		if (identifier.isPresent()) {
			optionalUser = userService.getUserByString(identifier.get());
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// If it is a valid identifier then return the user associated with it
		return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}


	/**
	 * Deletes the {@code User} that corresponds to the given ID from the database.
	 *
	 * @param id the ID of the {@code User} to be deleted.
	 *
	 * @return a response entity with the deleted {@code User} object (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@Operation(summary = "Delete a user by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Unrecoverable exception occurred"),
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Optional<Long> id) {
		if (id.isPresent()) {
			try {
				Optional<User> result = userService.deleteUserByID(id.get());
				return result.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
				             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


	/**
	 * Updates the {@code User} that corresponds to the given ID path variable with the values of the request body. It is not possible to update the
	 * ID, group relations, or interests via this method. Values that do not need to be updated can be omitted.
	 *
	 * @param valuesToUpdate a {@code User}-like JSON that holds the values to update.
	 * @param id             the ID of the {@code User} to be updated.
	 *
	 * @return a response entity with the updated {@code User} object (OK) or an empty body (BAD REQUEST, CONFLICT, or NOT FOUND).
	 */
	@Operation(summary = "Updates a user by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "409", description = "Update results in conflict"),
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<User> updateUser(@RequestBody Optional<User> valuesToUpdate, @PathVariable Optional<Long> id) {
		try {
			if (valuesToUpdate.isPresent() && id.isPresent()) {
				Optional<User> fromDB = userService.getUserByID(id.get());
				return fromDB.map(user -> new ResponseEntity<>(
						userService.saveUpdatedUser(user, valuesToUpdate.get()),  // 2
						HttpStatus.OK
				)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Gets all groups for a given user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Got user's groups", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserGroup.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@GetMapping("/{pathID}/groups")
	public ResponseEntity<?> getAllGroupsForUser(@PathVariable Optional<Long> pathID) {
		return pathID.map(id -> userService.ugService.getGroupsOfUserByID(id)
		                                             .map(groups -> new ResponseEntity<>(groups, HttpStatus.OK))
		                                             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)))
		             .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}

	@Operation(summary = "Add a User-Group relation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Added the group", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserGroup.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Entity not found"),
			@ApiResponse(responseCode = "409", description = "Relation already exists")
	})
	@PostMapping("/{id}/groups/{gid}")
	public ResponseEntity<?> addGroupRelation(@PathVariable("id") Optional<Long> user_id, @PathVariable("gid") Optional<Long> group_id) {
		/* Parameter Checking */
		if (!user_id.isPresent() || !group_id.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		try {
			return new ResponseEntity<>(
					userService.ugService.addRelationToUser(user_id.get(), group_id.get()),
					HttpStatus.OK
			);
		} catch (CybudsEntityByIdNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (CybudsActionResultsInConflictException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@Operation(summary = "Delete a User-Group relation based off both their IDs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted the group", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserGroup.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@DeleteMapping("/{id}/groups/{gid}")
	public ResponseEntity<?> deleteGroupRelation(@PathVariable("id") Optional<Long> user_id, @PathVariable("gid") Optional<Long> group_id) {
		/* Parameter Checking */
		if (!user_id.isPresent() || !group_id.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		try {
			return new ResponseEntity<>(
					userService.ugService.deleteRelationForUser(user_id.get(), group_id.get()),
					HttpStatus.OK
			);
		} catch (CybudsEntityByIdNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (CybudsActionResultsInConflictException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	@Operation(summary = "Gets all interests for a given user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Got user's interests", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserInterest.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),

	})
	@GetMapping("/{id}/interests")
	public ResponseEntity<?> getAllInterestsForUser(@PathVariable Optional<Long> id) {
		id.ifPresent(identifier -> userService.igService.getInterestsOfUserByID(identifier)
		                                                .map(groups -> new ResponseEntity<>(groups, HttpStatus.OK))
		                                                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)));
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Add a User-Interest relation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Added the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserInterest.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "409", description = "Relation already exists")
	})
	@PostMapping("/{id}/interests/{gid}")
	public ResponseEntity<?> addInterestRelation(@PathVariable("id") Optional<Long> user_id, @PathVariable("gid") Optional<Long> interest_id) {
		/* Parameter Checking */
		if (!user_id.isPresent() || !interest_id.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		try {
			return new ResponseEntity<>(
					userService.igService.addRelationToUser(user_id.get(), interest_id.get()),
					HttpStatus.OK
			);
		} catch (CybudsEntityByIdNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (CybudsActionResultsInConflictException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@Operation(summary = "Delete a User-Interest relation based off both their IDs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserInterest.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@DeleteMapping("/{id}/interests/{gid}")
	public ResponseEntity<?> deleteInterestRelation(@PathVariable("id") Optional<Long> user_id, @PathVariable("gid") Optional<Long> interest_id) {
		/* Parameter Checking */
		if (!user_id.isPresent() || !interest_id.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		try {
			return new ResponseEntity<>(
					userService.igService.deleteRelationForUser(user_id.get(), interest_id.get()),
					HttpStatus.OK
			);
		} catch (CybudsEntityByIdNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (CybudsActionResultsInConflictException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
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
	@GetMapping("/{id}/match")
	public ResponseEntity<?> matchUserByChoice(@PathVariable Optional<Long> id) {
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
	@GetMapping("/{id}/match/{choice}")
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


	/**
	 * Retrieves all the {@code User} objects in the database.
	 *
	 * @return all the users in the database
	 */
	@Operation(summary = "Gets all users")
	@GetMapping
	public Iterable<User> getAllUsers() {
		return userService.getAllUsersFromDB();    //1
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
	@GetMapping("/{id}/randomMatch")
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