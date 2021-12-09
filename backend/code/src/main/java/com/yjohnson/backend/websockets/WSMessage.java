package com.yjohnson.backend.websockets;

public class WSMessage {
	private WSRequest action;

	public WSMessage() {
	}

	public WSMessage(WSRequest action) {
		this.action = action;
	}

	public WSRequest getAction() {
		return action;
	}

	public void setAction(WSRequest action) {
		this.action = action;
	}
}