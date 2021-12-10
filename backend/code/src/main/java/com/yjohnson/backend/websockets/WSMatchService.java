package com.yjohnson.backend.websockets;

import com.yjohnson.backend.entities.Match.MatchEntity;
import com.yjohnson.backend.entities.Match.MatchService;
import com.yjohnson.backend.entities.User.User;
import com.yjohnson.backend.entities.User.UserService;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.*;

@Service
public class WSMatchService {
	// Store all socket session and their corresponding username.
	protected static final Map<Session, Long> sessionUserIDMap = new Hashtable<>();
	protected static final Map<Long, Session> userIDSessionMap = new Hashtable<>();
	protected final MatchService matchService;
	protected final UserService userService;
	protected final ArrayList<MatchEntity> matches = new ArrayList<>();
	protected ListIterator<MatchEntity> iterator;
	protected User currentUser;
	protected Iterable<User> allUsers;
	protected WSRequest lastAction;

	public WSMatchService(MatchService matchService, UserService userService) {
		this.matchService = matchService;
		this.userService = userService;
	}

	protected void onClose(Session session) {
		Long id = WSMatchService.sessionUserIDMap.get(session);
		removeFromMaps(session, id);
	}

	protected void removeFromMaps(Session session, Long id) {
		sessionUserIDMap.remove(session);
		userIDSessionMap.remove(id);
	}

	public Optional<User> onOpen(Session session, Long id) {
		addToMaps(session, id);
		this.allUsers = userService.getAllUsersFromDB();

		Long userID = WSMatchService.sessionUserIDMap.get(session);
		Optional<User> u = userService.getUserByID(userID);
		u.ifPresent(user -> currentUser = user);
		return u;
	}

	protected void addToMaps(Session session, Long id) {
		sessionUserIDMap.put(session, id);
		userIDSessionMap.put(id, session);
	}
}