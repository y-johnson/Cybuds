package com.yjohnson.backend.entities.Group;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public enum GroupType implements Serializable {
	@JsonProperty("Student major") STUDENT_MAJOR,
	@JsonProperty("College") COLLEGE,
	@JsonProperty("Student class") STUDENT_CLASS
}