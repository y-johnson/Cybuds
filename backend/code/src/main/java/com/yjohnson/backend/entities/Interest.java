package com.yjohnson.backend.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Interests")
public class Interest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	Long id;

	@Column(nullable = false, unique = true, length = 20)
	String name;

	@Column(length = 250)
	String description;

	private Interest(String name, String description) {
		this.name = name;
		this.description = description;
	}

	protected Interest() {
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"name = " + name + ", " +
				"description = " + description + ")";
	}
}