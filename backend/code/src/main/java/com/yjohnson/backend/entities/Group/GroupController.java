package com.yjohnson.backend.entities.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/groups")
public class GroupController {
	@Autowired
	GroupRepository groupRepository;

	@PostMapping
	public ResponseEntity<?> addGroup(@RequestBody GroupEntity newGroupEntity) {
		if (newGroupEntity.name == null || newGroupEntity.name.isEmpty()) return new ResponseEntity<>("Given name is empty!", HttpStatus.BAD_REQUEST);

		if (groupRepository.findGroupByName(newGroupEntity.name).isPresent()) {              //1
			return new ResponseEntity<>(newGroupEntity.name, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(groupRepository.save(newGroupEntity), HttpStatus.CREATED);
		}
	}

	@GetMapping
	public Iterable<GroupEntity> retrieveAll (){
		return groupRepository.findAll();
	}
}
