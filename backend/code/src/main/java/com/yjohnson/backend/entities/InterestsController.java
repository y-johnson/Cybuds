package com.yjohnson.backend.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/interests")
public class InterestsController {
	@Autowired
	private InterestRepository interestRepository;

	@PostMapping
	public ResponseEntity<?> addInterests(@RequestBody Interests interest) {

		if (interest.name == null || interest.name.isEmpty()) return new ResponseEntity<>(interest.name, HttpStatus.BAD_REQUEST);

		if (interestRepository.findInterestsByName(interest.name).isPresent()) {                //1
			return new ResponseEntity<>(interest.name, HttpStatus.CONFLICT);
		} else {
			interestRepository.save(interest);                                                  //2
			return new ResponseEntity<>(interestRepository.findInterestsByName(interest.name).get().name, HttpStatus.CREATED);//3
		}
	}

	@GetMapping(path = "/all")
	public Iterable<Interests> getAllInterests() {
		return interestRepository.findAll();
	}

}