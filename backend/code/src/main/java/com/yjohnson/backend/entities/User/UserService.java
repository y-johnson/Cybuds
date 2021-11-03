package com.yjohnson.backend.entities.User;

import java.util.Optional;

public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	Optional<User> getUser(String identifier) {
		Optional<User> optionalUser;
		try {
			// Treat it as a Long first (id)
			Long id = Long.parseLong(identifier);
			optionalUser = userRepository.findById(id);     // 1
		} catch (NumberFormatException e) {
			// If it is not a long, treat it as a String (username)
			optionalUser = userRepository.findByUsername(identifier);   // 1
		}
		return optionalUser;
	}

	Optional<User> deleteUserByID(Long id) throws CloneNotSupportedException {
		Optional<User> optionalUser = getUserByID(id);
		if (optionalUser.isPresent()) {
			User deleted = optionalUser.get().clone();
			userRepository.delete(optionalUser.get());                  // 2
			return Optional.of(deleted);
		}
		return Optional.empty();
	}

	Optional<User> getUserByID(Long id) {
		return userRepository.findById(id);
	}

	User saveUpdatedUser(User valuesToUpdate, User user) {
		return userRepository.save(user.updateContents(valuesToUpdate));
	}

	Iterable<User> getAllUsersFromDB() {
		return userRepository.findAll();
	}
}