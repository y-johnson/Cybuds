package com.yjohnson.backend.entities.Interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/interests")
public class InterestController {

	@Autowired
	InterestRepository interestRepository;

	@PostMapping
	public ResponseEntity<?> addInterest(@RequestBody InterestEntity newInterestEntity) {
		if (newInterestEntity.name == null || newInterestEntity.name.isEmpty()) return new ResponseEntity<>(
				"Given name is empty!",
				HttpStatus.BAD_REQUEST
		);

		if (interestRepository.findByName(newInterestEntity.name).isPresent()) {              //1
			return new ResponseEntity<>(newInterestEntity.name, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(interestRepository.save(newInterestEntity), HttpStatus.CREATED);
		}
	}

	@GetMapping
	public ResponseEntity<?> getInterest(@RequestParam Map<String, String> parameters) {
		Optional<InterestEntity> optionalInterestEntity;
		if (parameters.containsKey("id")) {
			optionalInterestEntity = interestRepository.findById(Long.valueOf(parameters.get("id")));     // 1
		} else if (parameters.containsKey("name")) {
			optionalInterestEntity = interestRepository.findByName(parameters.get("name"));   // 1
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return optionalInterestEntity.map(interest -> new ResponseEntity<>(interest, HttpStatus.OK))
		                             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping
	public ResponseEntity<InterestEntity> deleteInterest(@RequestParam("id") Long id) {
		try {
			Optional<InterestEntity> optionalInterestEntity = interestRepository.findById(id);  //1
			if (optionalInterestEntity.isPresent()) {
				InterestEntity deleted = optionalInterestEntity.get().clone();
				Long uid = optionalInterestEntity.get().getId();
				interestRepository.deleteById(uid);                         //2
				return new ResponseEntity<>(deleted, HttpStatus.OK);
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping
	public ResponseEntity<InterestEntity> updateGroup(@RequestBody InterestEntity requestedToUpdate) {
		if (requestedToUpdate != null && requestedToUpdate.getName() != null) {
			Optional<InterestEntity> optionalInterestEntity = Optional.empty();
			if (requestedToUpdate.getId() != null) optionalInterestEntity = interestRepository.findById(requestedToUpdate.getId());
			if (!optionalInterestEntity.isPresent()) optionalInterestEntity = interestRepository.findByName(requestedToUpdate.getName());
			if (optionalInterestEntity.isPresent()) {
				InterestEntity updatedInterestEntity = optionalInterestEntity.get();
				updatedInterestEntity.setName(requestedToUpdate.getName());
				updatedInterestEntity.setDescription(requestedToUpdate.getDescription());
				return new ResponseEntity<>(
						interestRepository.save(updatedInterestEntity),  // Updated all but ID.
						HttpStatus.OK
				);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/all")
	public Iterable<InterestEntity> retrieveAll() {
		return interestRepository.findAll();
	}
}
