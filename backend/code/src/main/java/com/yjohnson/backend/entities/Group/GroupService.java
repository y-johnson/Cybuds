package com.yjohnson.backend.entities.Group;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class GroupService {
	public static final String STATIC_MAJORS_TXT = "/majors.txt";
	public static final String STATIC_COLLEGES_TXT = "/colleges.txt";
	private static boolean loaded = false;
	private final GroupRepository groupRepository;

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
		preloadGroups(groupRepository, "%s is a major at ISU.", GroupType.STUDENT_MAJOR, STATIC_MAJORS_TXT);
		preloadGroups(groupRepository, "%s is a college at ISU.", GroupType.COLLEGE, STATIC_COLLEGES_TXT);
	}

	private static void preloadGroups(GroupRepository groupRepository, String format, GroupType groupType, String file) {
		if (!loaded){
			URL url = GroupService.class.getResource(file);
			assert url != null;
			try (Stream<String> stream = Files.lines(Paths.get(url.toURI()))) {
				stream.forEach((name) -> {
					if (!groupRepository.findByName(name).isPresent()) {
						try {
							groupRepository.save(new GroupEntity(
									groupType,
									name,
									String.format(format, name)
							));
						} catch (DataIntegrityViolationException ignored) {
							System.err.println("Save failed for " + name);
						}
					}
				});
			} catch (IOException | URISyntaxException | NullPointerException e) {
				e.printStackTrace();
			} finally {
				loaded = true;
			}
		}

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

	protected Iterable<GroupEntity> getGroupsByType(GroupType type) {
		return groupRepository.findAllByGroupType(type);
	}
}