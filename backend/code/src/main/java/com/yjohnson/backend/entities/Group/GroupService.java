package com.yjohnson.backend.entities.Group;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class GroupService {
	private final GroupRepository groupRepository;

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	protected Optional<GroupEntity> getGroupByID(Long id) {
		return groupRepository.findById(id);
	}

	protected Iterable<GroupEntity> getAllGroups() {
		return groupRepository.findAll();
	}

	protected GroupEntity saveUpdatedGroup(GroupEntity valuesToUpdate, GroupEntity group) {
		return groupRepository.save(group.updateContents(valuesToUpdate));
	}

	protected Optional<GroupEntity> deleteGroupById(Long id) throws CloneNotSupportedException {
		Optional<GroupEntity> optionalGroup = groupRepository.findById(id);  //1
		if (optionalGroup.isPresent()) {
			GroupEntity deleted = optionalGroup.get().clone();
			groupRepository.delete(optionalGroup.get());                         //2
			return Optional.of(deleted);
		}
		return Optional.empty();
	}

	protected Optional<GroupEntity> getGroupByString(String identifier) {
		Optional<GroupEntity> optionalGroup;
		try {
			// Treat it as a Long first (id)
			Long id = Long.parseLong(identifier);
			optionalGroup = groupRepository.findById(id);     // 1
		} catch (NumberFormatException e) {
			// If it is not a long, treat it as a String (name)
			optionalGroup = groupRepository.findByName(identifier);   // 1
		}
		return optionalGroup;
	}

	protected Optional<GroupEntity> addGroupToDB(GroupEntity newGroupEntity) {
		newGroupEntity.setName(StringUtils.trimWhitespace(StringUtils.capitalize(newGroupEntity.getName())));
		newGroupEntity.setDescription(StringUtils.trimWhitespace(StringUtils.capitalize(newGroupEntity.getDescription())));
		if (groupRepository.findByName(newGroupEntity.getName()).isPresent()) {              //1
			return Optional.empty();
		} else {
			return Optional.of(groupRepository.save(newGroupEntity));
		}
	}

	protected Iterable<GroupEntity> getGroupsByType(GroupType type){
		return groupRepository.findAllByGroupType(type);
	}
}