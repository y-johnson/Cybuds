package com.yjohnson.backend.entities.User;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

@Service
public class MatchService {
	UserService userService;

	protected Optional<User> matchUserRandomly(User currentUser, Iterable<User> allUsers) {
		LinkedList<User> list = new LinkedList<>();
		allUsers.forEach(list::add);

		int size = list.size();
		Random r = new Random();

		Optional<User> matchingUser = Optional.empty();
		while (!list.isEmpty()) {
			int index = r.nextInt(size);
			matchingUser = Optional.ofNullable(list.get(index));
			if (!matchingUser.isPresent() || matchingUser.get().getId().equals(currentUser.getId())) {
				list.remove(index);
			} else return matchingUser;
		}

		return matchingUser;
	}
}
