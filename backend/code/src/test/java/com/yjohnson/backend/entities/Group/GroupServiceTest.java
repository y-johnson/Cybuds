package com.yjohnson.backend.entities.Group;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GroupServiceTest {

	public static long ID = 1L;

	@Autowired
	GroupRepository groupRepository;


	GroupService service;
	GroupEntity entity;

	@BeforeEach
	void setUp() {
		entity = new GroupEntity("Test Entity", "This is a test entity");
		entity.id = ID;
		groupRepository.save(entity);
		service = new GroupService(groupRepository);
	}

	@Test
	void getGroupByID() {
		Optional<GroupEntity> optionalEntity = service.getGroupByID(ID);
		assert optionalEntity.isPresent();
		assert optionalEntity.get().equals(entity);
	}

	@Test
	void getAllGroups() {
		int i = 0;
		for (GroupEntity entity : service.getAllGroups()) {
			++i;
			assert entity.equals(this.entity);
		}
		assert i == 1;
	}

	@Test
	void saveUpdatedGroup() {
		Optional<GroupEntity> i = service.getGroupByID(ID);
		assert i.isPresent();
		assert i.get().equals(entity);

		GroupEntity o = new GroupEntity("Other Test", "Some other test");
		o.setId(ID);
		assert service.saveUpdatedGroup(new GroupEntity("Other Test", "Some other test"), i.get()).equals(o);
	}

	@Test
	void deleteGroupById() throws CloneNotSupportedException {
		Optional<GroupEntity> entity = service.deleteGroupById(ID);
		assert entity.isPresent();
		assert entity.get().equals(this.entity);
		int i = 0;
		for (GroupEntity ignored : service.getAllGroups()) {
			++i;
		}
		assert i == 0;
	}

	@Test
	void getGroupByString() {
		Optional<GroupEntity> optionalEntity = service.getGroupByString("Test Entity");
		assert optionalEntity.isPresent();
		assert optionalEntity.get().equals(entity);
	}

	@Test
	void addGroupToDB() {
		GroupEntity o = new GroupEntity("Other Test", "Some other test");
		o.setId(ID + 1);
		Optional<GroupEntity> added = service.addGroupToDB(o);
		assert added.isPresent();
		assert added.get().equals(o);
		int i = 0;
		for (GroupEntity ignored : service.getAllGroups()) {
			++i;
		}
		assert i == 2;
	}

	@AfterEach
	void tearDown() {
		groupRepository.findAll().forEach(groupEntity -> ID = groupEntity.id + 1);
		groupRepository.deleteAll();
	}
}