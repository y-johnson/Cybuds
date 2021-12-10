//package com.yjohnson.backend.entities.Interest;
//
//import com.yjohnson.backend.entities.Group.GroupRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class InterestServiceTest {
//
//	public static long ID = 1L;
//
//	@Autowired
//	InterestRepository interestRepository;
//
//
//	InterestService service;
//	InterestEntity entity;
//
//	@BeforeEach
//	void setUp() {
//		entity = new InterestEntity("Test Entity", "This is a test entity");
//		entity.id = ID;
//		interestRepository.save(entity);
//		service = new InterestService(interestRepository);
//	}
//
//	@Test
//	void getInterestByID() {
//		Optional<InterestEntity> optionalEntity = service.getInterestByID(ID);
//		assert optionalEntity.isPresent();
//		assert optionalEntity.get().equals(entity);
//	}
//
//	@Test
//	void getAllInterests() {
//		int i = 0;
//		for (InterestEntity entity : service.getAllInterests()) {
//			++i;
//			assert entity.equals(this.entity);
//		}
//		assert i == 1;
//	}
//
//	@Test
//	void saveUpdatedInterest() {
//		Optional<InterestEntity> i = service.getInterestByID(ID);
//		assert i.isPresent();
//		assert i.get().equals(entity);
//
//		InterestEntity o = new InterestEntity("Other Test", "Some other test");
//		o.setId(ID);
//		assert service.saveUpdatedInterest(new InterestEntity("Other Test", "Some other test"), i.get()).equals(o);
//	}
//
//	@Test
//	void deleteInterestById() throws CloneNotSupportedException {
//		Optional<InterestEntity> entity = service.deleteInterestById(ID);
//		assert entity.isPresent();
//		assert entity.get().equals(this.entity);
//		int i = 0;
//		for (InterestEntity ignored : service.getAllInterests()) {
//			++i;
//		}
//		assert i == 0;
//	}
//
//	@Test
//	void getInterestByString() {
//		Optional<InterestEntity> optionalEntity = service.getInterestByString("Test Entity");
//		assert optionalEntity.isPresent();
//		assert optionalEntity.get().equals(entity);
//	}
//
//	@Test
//	void addInterestToDB() {
//		InterestEntity o = new InterestEntity("Other Test", "Some other test");
//		o.setId(ID + 1);
//		Optional<InterestEntity> added = service.addInterestToDB(o);
//		assert added.isPresent();
//		assert added.get().equals(o);
//		int i = 0;
//		for (InterestEntity ignored : service.getAllInterests()) {
//			++i;
//		}
//		assert i == 2;
//	}
//
//	@AfterEach
//	void tearDown() {
//		interestRepository.findAll().forEach(interestEntity -> ID = interestEntity.id + 1);
//		interestRepository.deleteAll();
//	}
//}