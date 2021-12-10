package com.yjohnson.backend.websockets;

import com.yjohnson.backend.entities.Group.GroupType;
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
import java.util.Optional;

@Controller
@ServerEndpoint(value = "/ws/{id}", encoders = {MatchEntityEncoder.class})
public class WSMatchServer {
	private static WSMatchService wsMatchService;
	private final Logger logger = LoggerFactory.getLogger(WSMatchServer.class);


	@Autowired
	public void setWSMatchService(WSMatchService wsMatchService) {
		WSMatchServer.wsMatchService = wsMatchService;  // we are setting the static variable
	}


	@OnOpen
	public void onOpen(Session session, @PathParam("id") Long id) throws IOException {
		logger.info("Call to onOpen()");
		if (wsMatchService.onOpen(session, id).isPresent()) {
			session.getBasicRemote().sendText("User ID:" + id + " has started a websocket connection");
		} else {
			session.close(new CloseReason(
					CloseReason.CloseCodes.CANNOT_ACCEPT,
					"Cannot find user with ID" + id
			));
		}
	}


	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// Handle new messages
		logger.info("Call to onMessage: Got Message:" + message);
		String[] s = message.split(" ");
		WSRequest action = null;
		try {
			action = WSRequest.valueOf(s[0].trim());
			switch (action) {
				case NEXT_MATCH:
					if (wsMatchService.iterator.hasNext()) session.getBasicRemote().sendObject(wsMatchService.iterator.next());
					else session.getBasicRemote().sendText(WSResponse.END_OF_MATCHES.toString());
					break;
				case PREVIOUS_MATCH:
					if (wsMatchService.iterator.hasPrevious()) session.getBasicRemote().sendObject(wsMatchService.iterator.previous());
					else session.getBasicRemote().sendText(WSResponse.END_OF_MATCHES.toString());
					break;
				case START_MATCHING_PROCESS:
					GroupType subaction;
					if (s.length > 1) {
						subaction = GroupType.valueOf(s[1].trim());
						wsMatchService.matchService.matchUserByChoice(subaction, wsMatchService.currentUser, wsMatchService.allUsers).forEach(wsMatchService.matches::add);
					} else {
						wsMatchService.matchService.matchUser(wsMatchService.currentUser, wsMatchService.allUsers).forEach(wsMatchService.matches::add);
					}
					wsMatchService.iterator = wsMatchService.matches.listIterator();
					session.getBasicRemote().sendText(wsMatchService.currentUser.getUsername() + ": " + message);

					break;
				case END_MATCHING_PROCESS:
					session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Requested close."));
					onClose(session);
					break;
				case CONFIRM_MATCH:
					if (wsMatchService.lastAction == WSRequest.NEXT_MATCH) wsMatchService.iterator.previous().setConfirmed(true);
					else wsMatchService.iterator.next().setConfirmed(true);
					break;
				default:
					break;
			}
		} catch (IllegalArgumentException | EncodeException e) {
			logger.error("Caught exception " + e);
			onError(session, e);
		} finally {
			wsMatchService.lastAction = action;
		}
	}
	

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		wsMatchService.onClose(session);

		session.getBasicRemote().sendText(wsMatchService.currentUser.getUsername() + " disconnected.");
	}




	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.error(throwable.getMessage());
		try {
			session.getBasicRemote().sendText("Ran into an error with the request.");
		} catch (IOException e) {
			logger.error("Wow this is really bad. Error on the error; inspect the session object.");
		}
	}
}