package com.yjohnson.backend.entities.Match;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MatchEntityEncoder implements Encoder.Text<MatchEntity> {
	@Override
	public String encode(MatchEntity matchEntity) throws EncodeException {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(matchEntity);
		} catch (JsonProcessingException ignored) {
			throw new EncodeException(matchEntity, "Could not encode.");
		}
	}

	@Override
	public void init(EndpointConfig endpointConfig) {

	}

	@Override
	public void destroy() {

	}
}