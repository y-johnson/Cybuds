package com.yjohnson.backend.entities.Interest;

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

	/*
	Mappings are complicated when you want multiple path variable types.
	https://stackoverflow.com/questions/52260551/spring-boot-rest-single-path-variable-which-takes-different-type-of-values
	 */
	/**
	 * Retrieves a {@code InterestEntity} from the database whose ID matches the given path variable. 
	 * @param id       the id of the interest to retrieve
	 *
	 * @return the {@code InterestEntity} object that corresponds with the path variable (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@GetMapping(value = "/{identifier:[0-9]+}")
	public ResponseEntity<?> getInterestById(@PathVariable("identifier") Optional<Long> id) {
		Optional<InterestEntity> optionalInterest;
		if (id.isPresent()) optionalInterest = interestRepository.findById(id.get());     // 1
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return optionalInterest.map(interestEntity -> new ResponseEntity<>(interestEntity, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(
				HttpStatus.NOT_FOUND));
	}

	/**
	 * Retrieves a {@code InterestEntity} from the database whose name matches the given path variable.
	 *
	 * @param name       the name of the interest to retrieve
	 *
	 * @return the {@code InterestEntity} object that corresponds with the path variable (OK) or an empty body (BAD REQUEST or NOT FOUND).
	 */
	@GetMapping(value = "/{identifier:[A-Za-z]+}")
	public ResponseEntity<?> getInterestByName(@PathVariable(name = "identifier") Optional<String> name) {
		Optional<InterestEntity> optionalInterest;
		if (name.isPresent()) optionalInterest = interestRepository.findByName(name.get());     // 1
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
	 * Updates the {@code InterestEntity} that corresponds to the given ID path variable with the values of the request body. It is not possible to update the
	 * ID or interested users via this method. Values that do not need to be updated can be omitted.
	 *
	 * @param valuesToUpdate a {@code InterestEntity}-like JSON that holds the values to update.
	 * @param id             the ID of the {@code InterestEntity} to be updated.
	 *
	 * @return a response entity with the updated {@code InterestEntity} object (OK) or an empty body (BAD REQUEST, CONFLICT, or NOT FOUND).
	 */
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
	@GetMapping
	public Iterable<InterestEntity> retrieveAll() {
		return interestRepository.findAll();
	}
}
