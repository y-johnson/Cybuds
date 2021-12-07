package com.yjohnson.backend.entities.Match;

import com.yjohnson.backend.entities.User.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class MatchEntityId implements Serializable {

	private User matcher, matchee;

	public MatchEntityId(User matcher, User matchee) {
		this.matcher = matcher;
		this.matchee = matchee;
	}
}