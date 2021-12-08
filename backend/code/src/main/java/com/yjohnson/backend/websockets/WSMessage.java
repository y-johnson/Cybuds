package com.yjohnson.backend.websockets;

public class WSMessage {
	private WebsocketAction action;

	public WSMessage() {
	}

	public WSMessage(WebsocketAction action) {
		this.action = action;
	}

	public WebsocketAction getAction() {
		return action;
	}

	public void setAction(WebsocketAction action) {
		this.action = action;
	}
}