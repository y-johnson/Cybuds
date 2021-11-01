package com.yjohnson.backend.entities.Interest;

import com.yjohnson.backend.entities.User.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/interests")
public class InterestController {

	private final InterestRepository interestRepository;

	public InterestController(InterestRepository interestRepository) {
		this.interestRepository = interestRepository;
	}

	/**
	 * Adds a given interest to the database. This method sanitizes the input to a degree; the fields will be trimmed and names will be capitalized in
	 * Title case (e.g. "marTHa" -> "Martha").
	 * <p>
	 * If the given object contains repeated unique fields, then those fields are returned alongside a CONFLICT status code.
	 *
	 * @param newInterestEntity the {@code InterestEntity} object to insert into the database.
	 *
	 * @return a response entity with the created {@code InterestEntity} object (CREATED) or the conflicting value (CONFLICT).
	 */
	@Operation(summary = "Add an interest")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Added the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = InterestEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "409", description = "Adding value results in conflict")
	})
	@PostMapping
	public ResponseEntity<?> addInterest(@RequestBody InterestEntity newInterestEntity) {
		if (newInterestEntity.getName() == null || newInterestEntity.getName().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		newInterestEntity.setName(StringUtils.trimWhitespace(StringUtils.capitalize(newInterestEntity.getName())));
		newInterestEntity.setDescription(StringUtils.trimWhitespace(StringUtils.capitalize(newInterestEntity.getDescription())));
		if (interestRepository.findByName(newInterestEntity.getName()).isPresent()) {              //1
			return new ResponseEntity<>(newInterestEntity.getName(), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(interestRepository.save(newInterestEntity), HttpStatus.CREATED);
		}
	}

	/**
	 * Retrieves a {@code InterestEntity} from the database whose ID or name matches the given path variable.
	 *
	 * @param identifier the id or name of the interest to retrieve
	 *
	 * @return the {@code InterestEntity} object that corresponds with the path variable (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@Operation(summary = "Get an interest by its ID or name")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Got the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = InterestEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found")
	})
	@GetMapping(value = "/{identifier}")
	public ResponseEntity<?> getInterest(@PathVariable("identifier") Optional<String> identifier) {
		Optional<InterestEntity> optionalInterest;
		if (identifier.isPresent()) {
			try {
				// Treat it as a Long first (id)
				Long id = Long.parseLong(identifier.get());
				optionalInterest = interestRepository.findById(id);     // 1
			} catch (NumberFormatException e) {
				// If it is not a long, treat it as a String (name)
				optionalInterest = interestRepository.findByName(identifier.get());   // 1
			}
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return optionalInterest.map(interestEntity -> new ResponseEntity<>(interestEntity, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(
				HttpStatus.NOT_FOUND));
	}


	/**
	 * Deletes the {@code InterestEntity} that corresponds to the given ID from the database.
	 *
	 * @param id the ID of the {@code InterestEntity} to be deleted.
	 *
	 * @return a response entity with the deleted {@code InterestEntity} object (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@Operation(summary = "Delete an interest by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = InterestEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "Not found"),
			@ApiResponse(responseCode = "500", description = "Unrecoverable exception occurred"),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<InterestEntity> deleteInterest(@PathVariable Optional<Long> id) {
		if (id.isPresent()) {
			try {
				Optional<InterestEntity> optionalInterest = interestRepository.findById(id.get());  //1
				if (optionalInterest.isPresent()) {
					InterestEntity deleted = optionalInterest.get().clone();
					interestRepository.delete(optionalInterest.get());                         //2
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
	 * Updates the {@code InterestEntity} that corresponds to the given ID path variable with the values of the request body. It is not possible to
	 * update the ID or interested users via this method. Values that do not need to be updated can be omitted.
	 *
	 * @param valuesToUpdate a {@code InterestEntity}-like JSON that holds the values to update.
	 * @param id             the ID of the {@code InterestEntity} to be updated.
	 *
	 * @return a response entity with the updated {@code InterestEntity} object (OK) or an empty body (BAD REQUEST, CONFLICT, or NOT FOUND).
	 */
	@Operation(summary = "Updates an interest by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated the interest", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = InterestEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "Missing parameter"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "409", description = "Update results in conflict"),
	})

	@PutMapping("/{id}")
	public ResponseEntity<InterestEntity> updateGroup(@RequestBody Optional<InterestEntity> valuesToUpdate, @PathVariable Optional<Long> id) {
		try {
			if (valuesToUpdate.isPresent() && id.isPresent()) {
				Optional<InterestEntity> fromDB = interestRepository.findById(id.get());     // 1
				return fromDB.map(interest -> new ResponseEntity<>(
						interestRepository.save(interest.updateContents(valuesToUpdate.get())),  // 2
						HttpStatus.OK
				)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Retrieves all the {@code InterestEntity} objects in the database.
	 *
	 * @return all the interests in the database
	 */
	@Operation(summary = "Gets all interests")
	@GetMapping
	public Iterable<InterestEntity> retrieveAll() {
		return interestRepository.findAll();
	}
}
