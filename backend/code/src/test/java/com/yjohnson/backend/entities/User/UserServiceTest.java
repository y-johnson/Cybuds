package com.yjohnson.backend.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class UserServiceTest {
	public static final long ID = 1L;
	@Autowired
	UserRepository userRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	InterestRepository interestRepository;
	@Autowired
	UserGroupRepository userGroupRepository;
	@Autowired
	UserInterestRepository userInterestRepository;

	UserService service;
	User user;

	@BeforeEach
	void setUp() {
		user = new User(
				"exampleUsername",
				"example@email.com",
				"examplePassword",
				"ExampleFirst",
				"ExampleMiddle",
				"ExampleLast",
				"ExampleAddress",
				"1231231234",
				StudentClassification.SENIOR,
				Gender.OTHER,
				new HashSet<>(),
				new HashSet<>(),
				ID
		);
		userRepository.save(user);

		service = new UserService(userRepository, groupRepository, userGroupRepository, interestRepository, userInterestRepository);
	}

	@Test
	void getUser() {
		Optional<User> user1 = service.getUserByString("1");
		Optional<User> user2 = service.getUserByString("exampleUsername");
		assert user1.isPresent() && user2.isPresent();
		assert user1.get().equals(user2.get());
		assert !service.getUserByString("WrongExample").isPresent();
	}

	@Test
	void deleteUserByID() {
		Optional<User> userByID = service.getUserByID(ID);
		assert userByID.isPresent();
		try {
			service.deleteUserByID(ID);     // Make sure it calls deleteById()
		} catch (CloneNotSupportedException e) {
			fail();                         // Cloning must be supported
		}
		assert !service.getUserByID(ID).isPresent();

		int i = 0;
		for (User ignored : service.getAllUsersFromDB()) {
			++i;
		}
		assert i == 0;
	}

	@Test
	void getUserByID() {
		Optional<User> userByID = service.getUserByID(ID);
		assert userByID.isPresent();
		assert userByID.get().equals(user);
	}

	@Test
	void saveUpdatedUser() {
		Optional<User> userByID = service.getUserByID(ID);
		assert userByID.isPresent();
		User values = new User();
		values.setEmail("Changed@Email.com");
		Optional<User> user = service.getUserByID(ID);
		assert user.isPresent();
		assert service.saveUpdatedUser(values, userByID.get()).equals(new User(
				"exampleUsername",
				"Changed@Email.com",
				"examplePassword",
				"ExampleFirst",
				"ExampleMiddle",
				"ExampleLast",
				"ExampleAddress",
				"1231231234",
				StudentClassification.SENIOR,
				Gender.OTHER,
				new HashSet<>(),
				new HashSet<>(),
				ID
		));
	}

	@Test
	void getAllUsersFromDB() {
		int i = 0;
		for (User ignored : service.getAllUsersFromDB()) {
			++i;
		}
		assert i == 1;
	}
}