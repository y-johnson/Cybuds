package com.yjohnson.backend.entities.DB_Relations;

import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.Interest.InterestRepository;
import com.yjohnson.backend.entities.User.User;
import com.yjohnson.backend.entities.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/users/interests")
public class UserInterestController {
	@Autowired
	private UserInterestRepository userInterestRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private InterestRepository interestRepository;

	@GetMapping
	public Iterable<InterestEntity> getAllInterestsForUser(@RequestBody User user) {
		Set<InterestEntity> interests = new HashSet<>();
		Iterable<R_UserInterest> results = userInterestRepository.findAllByUser(user);
		for (R_UserInterest r : results) {
			System.out.println(results);
			interests.add(r.interest);
		}
		return interests;
	}

	@PostMapping
	public ResponseEntity<?> addRelation(@RequestParam Long user_id, @RequestParam Long interest_id) {
		if (user_id == null || interest_id == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<User> user = userRepository.findById(user_id);
		Optional<InterestEntity> interest = interestRepository.findById(interest_id);
		if (!user.isPresent() || !interest.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (!userInterestRepository.findByUserAndInterest(user.get(), interest.get()).isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT);
		R_UserInterest relation = userInterestRepository.save(new R_UserInterest(user.get(), interest.get(), LocalDateTime.now()));
		user.get().interestedIn.add(relation);
		interest.get().interested.add(relation);
		userRepository.save(user.get());
		interestRepository.save(interest.get());
		return new ResponseEntity<>(
				relation,  // Updated all but ID.
				HttpStatus.OK
		);
	}
}
