package com.yjohnson.backend.entities.Interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/interests")
public class InterestController {

	@Autowired
	InterestRepository interestRepository;

	@PostMapping
	public ResponseEntity<?> addInterest(@RequestBody InterestEntity newInterestEntity) {
		if (newInterestEntity.name == null || newInterestEntity.name.isEmpty()) return new ResponseEntity<>("Given name is empty!", HttpStatus.BAD_REQUEST);

		if (interestRepository.findByName(newInterestEntity.name).isPresent()) {              //1
			return new ResponseEntity<>(newInterestEntity.name, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(interestRepository.save(newInterestEntity), HttpStatus.CREATED);
		}
	}

	@GetMapping
	public Iterable<InterestEntity> retrieveAll() {
		return interestRepository.findAll();
	}
}
