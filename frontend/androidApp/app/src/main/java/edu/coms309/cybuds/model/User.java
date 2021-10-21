package edu.coms309.cybuds.model;

import java.io.Serializable;

public class User implements Serializable {
	private Long id;
	private String username;
	private String email;
	private String passwordHash;
	private String firstName;
	private String middleName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private String gender;

	public User() {

	}

	public Long getId() {
		return id;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String printable() {
		return "\n ID:  " + getId()
				+ "\n UserName:  " + getUsername()
				+ "\n Email:  " + getEmail()
				+ "\n PasswordHash:  " + getPasswordHash()
				+ "\n FirstName:  " + getFirstName()
				+ "\n MiddleName:  " + getMiddleName()
				+ "\n LastName:  " + getLastName()
				+ "\n Address:  " + getAddress()
				+ "\n PhoneNumber:  " + getPhoneNumber()
				+ "\n Gender:  " + getGender() + "\n";
	}
}
