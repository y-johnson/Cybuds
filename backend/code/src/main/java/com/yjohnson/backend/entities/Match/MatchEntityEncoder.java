package com.yjohnson.backend.entities.Match;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MatchEntityEncoder implements Encoder.Text<MatchEntity> {
	@Override
	public String encode(MatchEntity matchEntity) throws EncodeException {
		ObjectMapper om = new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		try {
			return om.writeValueAsString(matchEntity);
		} catch (JsonProcessingException e) {
			System.err.println(e);
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