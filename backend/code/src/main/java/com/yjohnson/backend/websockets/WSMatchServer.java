package com.yjohnson.backend.websockets;

import com.yjohnson.backend.entities.Match.MatchEntity;
import com.yjohnson.backend.entities.Match.MatchEntityEncoder;
import com.yjohnson.backend.entities.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Controller
@ServerEndpoint(value = "/ws/{id}", encoders = {MatchEntityEncoder.class})
public class WSMatchServer {
	// Store all socket session and their corresponding username.
	private static final Map<Session, Long> sessionUserIDMap = new Hashtable<>();
	private static final Map<Long, Session> userIDSessionMap = new Hashtable<>();
	private static WSMatchService wsMatchService;
	private final Logger logger = LoggerFactory.getLogger(WSMatchServer.class);

	private ListIterator<MatchEntity> iterator;
	private User currentUser;
	private Iterable<User> allUsers;
	private final ArrayList<MatchEntity> matches = new ArrayList<>();
	private WSRequest lastAction;

	@Autowired
	public void setWSMatchService(WSMatchService wsMatchService) {
		WSMatchServer.wsMatchService = wsMatchService;  // we are setting the static variable
	}


	@OnOpen
	public void onOpen(Session session, @PathParam("id") Long id) throws IOException {
		logger.info("Call to onOpen()");
		updateMaps(session, id);
		this.allUsers = wsMatchService.userService.getAllUsersFromDB();

		Long userID = sessionUserIDMap.get(session);
		Optional<User> u = wsMatchService.userService.getUserByID(userID);
		if (u.isPresent()) {
			this.currentUser = u.get();
			wsMatchService.matchService.matchUser(this.currentUser, this.allUsers).forEach(matches::add);
			String message = "User ID:" + id + " has started a websocket connection";
			broadcast(message);
		} else {
			session.close(new CloseReason(
					CloseReason.CloseCodes.CANNOT_ACCEPT,
					"Cannot find user with ID" + userID
			));
		}

	}

	private void updateMaps(Session session, Long id) {
		sessionUserIDMap.put(session, id);
		userIDSessionMap.put(id, session);
	}

	private void broadcast(String message) {
		sessionUserIDMap.forEach((session, username) -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.error("Exception: " + e.getMessage());
				e.printStackTrace();
			}

		});

	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// Handle new messages
		logger.info("Call to onMessage: Got Message:" + message);
		WSRequest action = null;
		try {
			action = WSRequest.valueOf(message.trim());
			switch (action) {
				case NEXT_MATCH:
					if (this.iterator.hasNext()) session.getBasicRemote().sendObject(iterator.next());
					else session.getBasicRemote().sendText(WSResponse.END_OF_MATCHES.toString());
					break;
				case PREVIOUS_MATCH:
					if (this.iterator.hasPrevious()) session.getBasicRemote().sendObject(iterator.previous());
					else session.getBasicRemote().sendText(WSResponse.END_OF_MATCHES.toString());
					break;
				case START_MATCHING_PROCESS:
					this.iterator = matches.listIterator();
					broadcast(currentUser.getUsername() + ": " + message);
					break;
				case END_MATCHING_PROCESS:
					// todo
					break;
				case CONFIRM_MATCH:

					break;
			}
		} catch (IllegalArgumentException | EncodeException e) {
			logger.error("Caught exception " + e);
		} finally {
			lastAction = action;
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		Long id = sessionUserIDMap.get(session);
		sessionUserIDMap.remove(session);
		userIDSessionMap.remove(id);

		String message = id + " disconnected";
		broadcast(message);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.error(throwable.getMessage());
		logger.info("Entered into Error");
	}


//
//	WSMatchService wsMatchService;
//
//	public WSMatchServer(WSMatchService wsMatchService) {
//		this.wsMatchService = wsMatchService;
//	}
//
//	@MessageMapping("/match/{optionalId}")
//	@SendTo("/topic/match")
//	public Iterable<MatchEntity> match(WSMessage message, @PathVariable Optional<Long> optionalId) throws Exception {
//		Thread.sleep(1000); // simulated delay
//		switch (message.getAction()) {
//			case NEXT_MATCH:
//				break;
//			case PREVIOUS_MATCH:
//				break;
//			case START_MATCHING_PROCESS:
//				if (optionalId.isPresent()) wsMatchService.match(message, optionalId.get());
//				else throw new IllegalStateException("No ID");
//				break;
//			case END_MATCHING_PROCESS:
//				break;
//			case CONFIRM_MATCH:
//				break;
//			default:
//				throw new IllegalStateException("Unexpected value: " + message);
//		}
//		return new ArrayList<>();
//	}
}