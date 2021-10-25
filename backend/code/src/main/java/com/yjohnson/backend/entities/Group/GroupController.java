package com.yjohnson.backend.entities.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/groups")
public class GroupController {
	@Autowired
	GroupRepository groupRepository;

	@PostMapping
	public ResponseEntity<?> addGroup(@RequestBody GroupEntity newGroupEntity) {
		if (newGroupEntity.getName() == null || newGroupEntity.getName().isEmpty()) return new ResponseEntity<>(
				"Given name is empty!",
				HttpStatus.BAD_REQUEST
		);

		if (groupRepository.findByName(newGroupEntity.getName()).isPresent()) {              //1
			return new ResponseEntity<>(newGroupEntity.getName(), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(groupRepository.save(newGroupEntity), HttpStatus.CREATED);
		}
	}

	@GetMapping
	public ResponseEntity<?> getGroup(@RequestParam Map<String, String> parameters) {
		Optional<GroupEntity> optionalGroupEntity;
		if (parameters.containsKey("id")) {
			optionalGroupEntity = groupRepository.findById(Long.valueOf(parameters.get("id")));     // 1
		} else if (parameters.containsKey("name")) {
			optionalGroupEntity = groupRepository.findByName(parameters.get("name"));   // 1
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return optionalGroupEntity.map(group -> new ResponseEntity<>(group, HttpStatus.OK))
		                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping
	public ResponseEntity<GroupEntity> deleteGroup(@RequestParam("id") Long id) {
		try {
			Optional<GroupEntity> optionalGroupEntity = groupRepository.findById(id);  //1
			if (optionalGroupEntity.isPresent()) {
				GroupEntity deleted = optionalGroupEntity.get().clone();
				Long uid = optionalGroupEntity.get().getId();
				groupRepository.deleteById(uid);                         //2
				return new ResponseEntity<>(deleted, HttpStatus.OK);
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping
	public ResponseEntity<GroupEntity> updateGroup(@RequestBody GroupEntity requestedToUpdate) {
		if (requestedToUpdate != null && requestedToUpdate.getName() != null) {
			Optional<GroupEntity> optionalGroupEntity = Optional.empty();
			if (requestedToUpdate.getId() != null) optionalGroupEntity = groupRepository.findById(requestedToUpdate.getId());
			if (!optionalGroupEntity.isPresent()) optionalGroupEntity = groupRepository.findByName(requestedToUpdate.getName());
			if (optionalGroupEntity.isPresent()) {
				GroupEntity updatedGroupEntity = optionalGroupEntity.get();
				updatedGroupEntity.setName(requestedToUpdate.getName());
				updatedGroupEntity.setDescription(requestedToUpdate.getDescription());
				return new ResponseEntity<>(
						groupRepository.save(updatedGroupEntity),  // Updated all but ID.
						HttpStatus.OK
				);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/all")
	public Iterable<GroupEntity> retrieveAll() {
		return groupRepository.findAll();
	}
}
