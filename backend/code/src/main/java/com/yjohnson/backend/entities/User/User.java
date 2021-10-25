package com.yjohnson.backend.entities.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

enum Gender {
	MALE,
	FEMALE,
	OTHER
}

@Entity
@Table(name = "Users")
public class User implements Serializable, Cloneable {
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

	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<R_UserInterest> interestedIn;

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

	public User updateContents(User requestedToUpdate) {
		if (requestedToUpdate.address != null) this.setAddress(requestedToUpdate.address);
		if (requestedToUpdate.username != null) this.setUsername(requestedToUpdate.username);
		if (requestedToUpdate.email != null) this.setEmail(requestedToUpdate.email);
		if (requestedToUpdate.passwordHash != null) this.setPasswordHash(requestedToUpdate.passwordHash);
		if (requestedToUpdate.firstName != null) this.setFirstName(requestedToUpdate.firstName);
		if (requestedToUpdate.middleName != null) this.setMiddleName(requestedToUpdate.middleName);
		if (requestedToUpdate.lastName != null) this.setLastName(requestedToUpdate.lastName);
		if (requestedToUpdate.phoneNumber != null) this.setPhoneNumber(requestedToUpdate.phoneNumber);
		if (requestedToUpdate.gender != null) this.setGender(requestedToUpdate.gender);
		return this;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	protected User clone() throws CloneNotSupportedException {
		return (User) super.clone();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Set<R_UserInterest> getInterestedIn() {
		return interestedIn;
	}


}