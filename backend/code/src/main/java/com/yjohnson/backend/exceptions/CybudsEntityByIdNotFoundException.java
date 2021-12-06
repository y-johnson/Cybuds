package com.yjohnson.backend.exceptions;

public class CybudsEntityByIdNotFoundException extends Exception {
	public CybudsEntityByIdNotFoundException() {
		super();
	}
	public CybudsEntityByIdNotFoundException(String errorMessage) {
		 super(errorMessage);
	}
}
