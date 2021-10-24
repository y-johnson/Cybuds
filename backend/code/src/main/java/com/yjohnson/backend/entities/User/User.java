package com.yjohnson.backend.entities.User;

import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.Interest.Relation_UserInterest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

enum Gender {
	MALE,
	FEMALE,
	OTHER
}

@Entity
@Table(name = "Users")
public class User implements Serializable {
	@Column(nullable = false, unique = true)
	public String username;
	@Column(nullable = false, unique = true)
	public String email;
	@Column(nullable = false)
	public String passwordHash;
	@Column(nullable = false, length = 15)
	public String firstName;
	@Column(length = 15)
	public String middleName;
	@Column(nullable = false, length = 15)
	public String lastName;
	public String address;
	public String phoneNumber;
	@Enumerated(EnumType.STRING)
	public Gender gender;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	protected User() {
	}

	public User(String lastName, String firstName, String middleName, String email, String username, String passwordHash, Gender gender,
	            String address, String phoneNumber) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.email = email;
		this.username = username;
		this.passwordHash = passwordHash;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL)
	Set<Relation_UserInterest> interestedIn;
}