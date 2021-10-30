package com.yjohnson.backend.entities.DB_Relations;

import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupRepository;
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
@RequestMapping(path = "/users/groups")
public class UserGroupController {
	@Autowired
	private UserGroupRepository relationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GroupRepository groupRepository;

	@GetMapping
	public Iterable<GroupEntity> getAllGroupsForUser(@RequestBody User user) {
		Set<GroupEntity> groups = new HashSet<>();
		Iterable<R_UserGroup> results = relationRepository.findAllByUser(user);
		for (R_UserGroup r : results) {
			System.out.println(results);
			groups.add(r.group);
		}
		return groups;
	}

//	@DeleteMapping
//	public ResponseEntity<?> deleteRelation(@RequestParam Long user_id, @RequestParam Long group_id) {
//
//	}

	@PostMapping
	public ResponseEntity<?> addRelation(@RequestParam Long user_id, @RequestParam Long group_id) {
		/* Parameter Checking */
		if (user_id == null || group_id == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Optional<User> user = userRepository.findById(user_id);// 1
		Optional<GroupEntity> optionalGroup = groupRepository.findById(group_id);// 2

		if (!user.isPresent() || !optionalGroup.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (!relationRepository.findByUserAndGroup(user.get(), optionalGroup.get()).isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT); // 3

		R_UserGroup relation = relationRepository.save(new R_UserGroup(user.get(), optionalGroup.get(), LocalDateTime.now())); //4
		user.get().partOf.add(relation);
		optionalGroup.get().members.add(relation);
		userRepository.save(user.get()); // 5
		groupRepository.save(optionalGroup.get()); // 6
		return new ResponseEntity<>(
				relation,  // Updated all but ID.
				HttpStatus.OK
		);
	}
}
