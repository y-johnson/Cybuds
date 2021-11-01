package com.yjohnson.backend.entities.User;

import com.yjohnson.backend.entities.DB_Relations.R_UserGroup;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.DB_Relations.UserGroupRepository;
import com.yjohnson.backend.entities.DB_Relations.UserInterestRepository;
import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupRepository;
import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.Interest.InterestRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	private final GroupRepository groupRepository;
	private final InterestRepository interestRepository;
	private final UserRepository userRepository;
	private final UserGroupRepository userGroupRepository;
	private final UserInterestRepository userInterestRepository;


	public UserController(UserRepository userRepository, UserGroupRepository userGroupRepository, GroupRepository groupRepository, InterestRepository interestRepository, UserInterestRepository userInterestRepository) {
		this.userRepository = userRepository;
		this.userGroupRepository = userGroupRepository;
		this.groupRepository = groupRepository;
		this.interestRepository = interestRepository;
		this.userInterestRepository = userInterestRepository;
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
			try {
				// Treat it as a Long first (id)
				Long id = Long.parseLong(identifier.get());
				optionalUser = userRepository.findById(id);     // 1
			} catch (NumberFormatException e) {
				// If it is not a long, treat it as a String (username)
				optionalUser = userRepository.findUserByUsername(identifier.get());   // 1
			}
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
				Optional<User> optionalUser = userRepository.findById(id.get());      // 1
				if (optionalUser.isPresent()) {
					User deleted = optionalUser.get().clone();
					userRepository.delete(optionalUser.get());                  // 2
					return new ResponseEntity<>(deleted, HttpStatus.OK);
				}
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
				Optional<User> fromDB = userRepository.findById(id.get());     // 1
				return fromDB.map(user -> new ResponseEntity<>(
						userRepository.save(user.updateContents(valuesToUpdate.get())),  // 2
						HttpStatus.OK
				)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
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
		return userRepository.findAll();    //1
	}

	@Operation(summary = "Gets all groups for a given user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Got user's groups", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserGroup.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
	})
	@GetMapping("/{id}/groups")
	public ResponseEntity<?> getAllGroupsForUser(@PathVariable Optional<Long> id) {
		if (id.isPresent()) {
			Optional<User> optionalUser = userRepository.findById(id.get());
			return optionalUser.map(user -> new ResponseEntity<Iterable<R_UserGroup>>(user.getGroups(), HttpStatus.OK))
			                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Add a User-Group relation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Added the group", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = R_UserGroup.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "409", description = "Relation already exists")
	})
	@PostMapping("/{id}/groups/{gid}")
	public ResponseEntity<?> addRelation(@PathVariable("id") Optional<Long> user_id, @PathVariable("gid") Optional<Long> group_id) {
		/* Parameter Checking */
		if (!user_id.isPresent() || !group_id.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Optional<User> user = userRepository.findById(user_id.get());// 1
		Optional<GroupEntity> optionalGroup = groupRepository.findById(group_id.get());// 2

		if (!user.isPresent() || !optionalGroup.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (userGroupRepository.findByUserAndGroup(user.get(), optionalGroup.get()).isPresent())
			return new ResponseEntity<>(HttpStatus.CONFLICT); // 3

		R_UserGroup relation = userGroupRepository.save(new R_UserGroup(user.get(), optionalGroup.get(), LocalDateTime.now())); //4
		user.get().getGroups().add(relation);
		optionalGroup.get().members.add(relation);
		userRepository.save(user.get()); // 5
		groupRepository.save(optionalGroup.get()); // 6
		return new ResponseEntity<>(
				relation,  // Updated all but ID.
				HttpStatus.CREATED
		);
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
	public ResponseEntity<?> deleteRelation(@PathVariable("id") Optional<Long> user_id, @PathVariable("gid") Optional<Long> group_id) {
		/* Parameter Checking */
		if (!user_id.isPresent() || !group_id.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Optional<User> user = userRepository.findById(user_id.get());// 1
		Optional<GroupEntity> optionalGroup = groupRepository.findById(group_id.get());// 2

		if (!user.isPresent() || !optionalGroup.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<R_UserGroup> relation = userGroupRepository.findByUserAndGroup(user.get(), optionalGroup.get());
		if (!relation.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 3

		user.get().getGroups().remove(relation.get());
		optionalGroup.get().members.remove(relation.get());
		userGroupRepository.delete(relation.get()); //4
		userRepository.save(user.get()); // 5
		groupRepository.save(optionalGroup.get()); // 6
		return new ResponseEntity<>(
				relation,
				HttpStatus.OK
		);
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
		if (id.isPresent()) {
			Optional<User> optionalUser = userRepository.findById(id.get());
			return optionalUser.map(user -> new ResponseEntity<Iterable<R_UserInterest>>(user.getInterests(), HttpStatus.OK))
			                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
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

		Optional<User> user = userRepository.findById(user_id.get());// 1
		Optional<InterestEntity> optionalInterest = interestRepository.findById(interest_id.get());// 2

		if (!user.isPresent() || !optionalInterest.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (userInterestRepository.findByUserAndInterest(user.get(), optionalInterest.get()).isPresent())
			return new ResponseEntity<>(HttpStatus.CONFLICT); // 3

		R_UserInterest relation = userInterestRepository.save(new R_UserInterest(user.get(), optionalInterest.get(), LocalDateTime.now())); //4
		user.get().getInterests().add(relation);
		optionalInterest.get().interested.add(relation);
		userRepository.save(user.get()); // 5
		interestRepository.save(optionalInterest.get()); // 6
		return new ResponseEntity<>(
				relation,  // Updated all but ID.
				HttpStatus.CREATED
		);
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

		Optional<User> user = userRepository.findById(user_id.get());// 1
		Optional<InterestEntity> optionalInterest = interestRepository.findById(interest_id.get());// 2

		if (!user.isPresent() || !optionalInterest.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<R_UserInterest> relation = userInterestRepository.findByUserAndInterest(user.get(), optionalInterest.get());
		if (!relation.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 3

		user.get().getInterests().remove(relation.get());
		optionalInterest.get().interested.remove(relation.get());
		userInterestRepository.delete(relation.get()); //4
		userRepository.save(user.get()); // 5
		interestRepository.save(optionalInterest.get()); // 6
		return new ResponseEntity<>(
				relation,
				HttpStatus.OK
		);
	}
}