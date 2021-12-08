package com.yjohnson.backend.websockets;

import com.yjohnson.backend.entities.Match.MatchService;
import com.yjohnson.backend.entities.User.User;
import com.yjohnson.backend.entities.User.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WSMatchService {
	public final MatchService matchService;
	public final UserService userService;

	public WSMatchService(MatchService matchService, UserService userService) {
		this.matchService = matchService;
		this.userService = userService;
	}

	protected void match(WSMessage message, Long id) {
		int idx = 0;
		Optional<User> current = userService.getUserByID(id);
		current.ifPresent(user -> matchService.matchUser(user, userService.getAllUsersFromDB()));
	}
}