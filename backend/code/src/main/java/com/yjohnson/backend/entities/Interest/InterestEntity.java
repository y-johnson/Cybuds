package com.yjohnson.backend.entities.Interest;

import com.yjohnson.backend.entities.User.User;

import javax.persistence.*;
import java.util.Set;

@Entity
public class InterestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(nullable = false, unique = true)
	String name;

	String description;

	@OneToMany(cascade = CascadeType.ALL)
	Set<Relation_UserInterest> interested;

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
}
