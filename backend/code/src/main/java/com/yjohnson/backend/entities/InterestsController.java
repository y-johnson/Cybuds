package com.yjohnson.backend.entities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/interests")
public class InterestsController {
	private final InterestRepository interestRepository;

	public InterestsController(InterestRepository interestRepository) {
		this.interestRepository = interestRepository;
	}

	@PostMapping
	public ResponseEntity<?> addInterests(@RequestBody Interest interest) {

		if (interest.name == null || interest.name.isEmpty()) return new ResponseEntity<>(interest.name, HttpStatus.BAD_REQUEST);

		if (interestRepository.findInterestsByName(interest.name).isPresent()) {                //1
			return new ResponseEntity<>(interest.name, HttpStatus.CONFLICT);
		} else {
			interestRepository.save(interest);                                                  //2
			return new ResponseEntity<>(interestRepository.findInterestsByName(interest.name).get().name, HttpStatus.CREATED);//3
		}
	}

	@GetMapping
	public ResponseEntity<Interest> getInterest(@RequestParam Map<String, String> parameters) {
		Optional<Interest> optionalInterest;
		if (parameters.containsKey("id")) {
			optionalInterest = interestRepository.findById(Long.valueOf(parameters.get("id")));     // 1
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		/* Don't even ask me, this is apparently the best way to handle Optionals */
		return optionalInterest.map(interest -> new ResponseEntity<>(interest, HttpStatus.OK))
		                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "/all")
	public Iterable<Interest> getAllInterests() {
		interestRepository.findAll().forEach(System.out::println);
		return interestRepository.findAll();
	}

}