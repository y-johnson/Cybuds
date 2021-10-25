package com.yjohnson.backend.entities.Interest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class InterestEntity implements Serializable, Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(nullable = false, unique = true)
	String name;

	String description;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<R_UserInterest> interested;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public InterestEntity clone() throws CloneNotSupportedException {
		return (InterestEntity) super.clone();
	}
}
