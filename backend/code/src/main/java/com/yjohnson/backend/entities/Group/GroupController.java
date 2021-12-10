package com.yjohnson.backend.entities.Group;

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
@RequestMapping(path = "/groups")
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupRepository groupRepository, GroupService groupService) {
		this.groupService = groupService;
	}



	/**
	 * Adds a given group to the database. This method sanitizes the input to a degree; the fields will be trimmed and names will be capitalized in
	 * Title case (e.g. "marTHa" - "Martha").
	 * <p>
	 * If the given object contains repeated unique fields, then those fields are returned alongside a CONFLICT status code.
	 *
	 * @param newGroupEntity the {@code GroupEntity} object to insert into the database.
	 *
	 * @return a response entity with the created {@code GroupEntity} object (CREATED) or the conflicting value (CONFLICT).
	 */
	@Operation(summary = "Add a group")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Added the group", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GroupEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "409", description = "Adding value results in conflict")
	})
	@PostMapping
	public ResponseEntity<?> addGroup(@RequestBody GroupEntity newGroupEntity) {
		if (newGroupEntity.getName() == null || newGroupEntity.getName().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return groupService.addGroupToDB(newGroupEntity).map(entity -> new ResponseEntity<>(entity, HttpStatus.CREATED))
		                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
	}

	/*
	Mappings are complicated when you want multiple path variable types.
	https://stackoverflow.com/questions/52260551/spring-boot-rest-single-path-variable-which-takes-different-type-of-values
	 */

	/**
	 * Retrieves a {@code GroupEntity} from the database whose ID or name matches the given path variable.
	 *
	 * @param identifier the id or name of the group to retrieve
	 *
	 * @return the {@code GroupEntity} object that corresponds with the path variable (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@Operation(summary = "Get a group by its ID or name")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Got the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GroupEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found")
	})
	@GetMapping(value = "/{identifier}")
	public ResponseEntity<?> getGroupFromString(@PathVariable("identifier") Optional<String> identifier) {
		Optional<GroupEntity> optionalGroup;
		if (identifier.isPresent()) {
			optionalGroup = groupService.getGroupByString(identifier.get());
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return optionalGroup.map(groupEntity -> new ResponseEntity<>(groupEntity, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(
				HttpStatus.NOT_FOUND));
	}

	/**
	 * Deletes the {@code GroupEntity} that corresponds to the given ID from the database.
	 *
	 * @param id the ID of the {@code GroupEntity} to be deleted.
	 *
	 * @return a response entity with the deleted {@code GroupEntity} object (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@Operation(summary = "Delete a group by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted the group", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GroupEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
			@ApiResponse(responseCode = "500", description = "Unrecoverable exception occurred"),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<GroupEntity> deleteGroup(@PathVariable Optional<Long> id) {
		if (id.isPresent()) {
			try {
				Optional<GroupEntity> deleted = groupService.deleteGroupById(id.get());
				return deleted.map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
				              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Updates the {@code GroupEntity} that corresponds to the given ID path variable with the values of the request body. It is not possible to
	 * update the ID or grouped users via this method. Values that do not need to be updated can be omitted.
	 *
	 * @param valuesToUpdate a {@code GroupEntity}-like JSON that holds the values to update.
	 * @param id             the ID of the {@code GroupEntity} to be updated.
	 *
	 * @return a response entity with the updated {@code GroupEntity} object (OK) or an empty body (BAD REQUEST, CONFLICT, or NOT FOUND).
	 */
	@Operation(summary = "Updates a group by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated the group", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GroupEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "409", description = "Update results in conflict"),
	})
	@PutMapping("/{id}")
	public ResponseEntity<GroupEntity> updateGroup(@RequestBody Optional<GroupEntity> valuesToUpdate, @PathVariable Optional<Long> id) {
		try {
			if (valuesToUpdate.isPresent() && id.isPresent()) {
				Optional<GroupEntity> fromDB = groupService.getGroupByID(id.get());     // 1
				return fromDB.map(group -> new ResponseEntity<>(
						groupService.saveUpdatedGroup(valuesToUpdate.get(), group),  // 2
						HttpStatus.OK
				)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Retrieves all the {@code GroupEntity} objects in the database.
	 *
	 * @return all the groups in the database
	 */
	@Operation(summary = "Gets all groups")
	@GetMapping
	public Iterable<GroupEntity> retrieveAll() {
		return groupService.getAllGroups();
	}

	@Operation(summary = "Gets all majors")
	@GetMapping("/majors")
	public Iterable<GroupEntity> retrieveMajors() {
		return groupService.getGroupsByType(GroupType.STUDENT_MAJOR);
	}

	@Operation(summary = "Gets all colleges")
	@GetMapping("/colleges")
	public Iterable<GroupEntity> retrieveColleges() {
		return groupService.getGroupsByType(GroupType.COLLEGE);
	}
}