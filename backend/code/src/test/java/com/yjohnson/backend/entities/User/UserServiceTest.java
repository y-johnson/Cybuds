package com.yjohnson.backend.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
	public static final long ID = 1L;
	UserRepository repo;
	UserService service;
	User user;

	@BeforeEach
	void setUp() {
		repo = mock(UserRepository.class);
		user = new User(
				"exampleUsername",
				"example@email.com",
				"examplePassword",
				"ExampleFirstName",
				"ExampleMiddleName",
				"ExampleLastName",
				"ExampleAddress",
				"1231231234",
				StudentClassification.SENIOR,
				Gender.OTHER,
				new HashSet<>(),
				new HashSet<>(),
				ID
		);
		when(repo.findByEmail("a@example.com")).thenReturn(Optional.of(user));
		when(repo.findByUsername("exampleUsername")).thenReturn(Optional.of(user));
		when(repo.findById(ID)).thenReturn(Optional.of(user), Optional.empty());
		doNothing().when(repo).deleteById(ID);
		service = new UserService(repo);
	}

	@Test
	void getUser() {
		Optional<User> user1 = service.getUser("1");
		Optional<User> user2 = service.getUser("exampleUsername");

		assert user1.isPresent() && user2.isPresent();
		System.out.println(user1.get());
		System.out.println(user2.get());
		assert user1.get().equals(user2.get());
		assert !service.getUser("WrongExample").isPresent();
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
	}

	@Test
	void getUserByID() {
		Optional<User> userByID = service.getUserByID(ID);
		assert userByID.isPresent();
		assert userByID.get().equals(user);
	}

	@Test
	void saveUpdatedUser() {

	}

	@Test
	void getAllUsersFromDB() {
	}
}