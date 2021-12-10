package com.yjohnson.backend.entities.Group;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

@Service
public class GroupService {
	public static final String STATIC_MAJORS_TXT = "/static/txt/majors.txt";
	public static final String STATIC_COLLEGES_TXT = "/static/txt/colleges.txt";
	private static boolean loaded = false;
	private final GroupRepository groupRepository;
	@Value("classpath:static/txt/*")
	private Resource[] resources;
	private List<String> filenames;

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;

	}

	@PostConstruct
	void init() throws IOException {
		InputStream f = new ClassPathResource("static/txt/majors.txt").getInputStream();
		preloadGroups(groupRepository, "%s is a major at ISU.", GroupType.STUDENT_MAJOR, f);

		f = new ClassPathResource("static/txt/colleges.txt").getInputStream();

		preloadGroups(groupRepository, "%s is a college at ISU.", GroupType.COLLEGE, f);
	}

	private static void preloadGroups(GroupRepository groupRepository, String format, GroupType groupType, InputStream file) {
		if (!loaded) {

			try (Scanner scanner = new Scanner(file).useDelimiter("\\r?\\n")) {
				while (scanner.hasNext()){
					String name = scanner.next();
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
				}
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