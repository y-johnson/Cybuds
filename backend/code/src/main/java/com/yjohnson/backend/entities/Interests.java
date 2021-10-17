package com.yjohnson.backend.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "interests")
public class Interests implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	Long id;

	@Column(nullable = false, unique = true, length = 20)
	String name;

	@Column(length = 250)
	String description;

	private Interests(String name, String description) {
		this.name = name;
		this.description = description;
	}

	protected Interests() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	private String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
}