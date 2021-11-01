package com.yjohnson.backend.entities.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjohnson.backend.entities.DB_Relations.R_UserGroup;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

enum Gender {
	MALE,
	FEMALE,
	OTHER
}
enum StudentClassification {
	FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
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

	@Column(nullable = false)
	public StudentClassification classification;

	@Enumerated(EnumType.STRING)
	public Gender gender;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<R_UserInterest> interestedIn;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<R_UserGroup> partOf;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	protected User() {}

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

	@JsonIgnore
	public Set<R_UserGroup> getGroups() {
		return partOf;
	}

	/**
	 * Update all declared member variables with the contents of the given {@code User} object. Note that the given object does NOT need to declare
	 * variables that do not need to be updated. This will not update the user relations with {@code Group} or {@code Interest}, or this object's
	 * {@code id}.
	 * <p>
	 * The purpose of this method is to simulate the Dto pattern with clearer readability. Used for Spring's {@code CrudRepository.save()} purposes.
	 *
	 * @param toCopy the user object to copy values from.
	 *
	 * @return this (updated) {@code User} object
	 */
	public User updateContents(User toCopy) {
		if (toCopy.address != null) this.setAddress(toCopy.address);
		if (toCopy.username != null) this.setUsername(toCopy.username);
		if (toCopy.email != null) this.setEmail(toCopy.email);
		if (toCopy.passwordHash != null) this.setPasswordHash(toCopy.passwordHash);
		if (toCopy.firstName != null) this.setFirstName(toCopy.firstName);
		if (toCopy.middleName != null) this.setMiddleName(toCopy.middleName);
		if (toCopy.lastName != null) this.setLastName(toCopy.lastName);
		if (toCopy.phoneNumber != null) this.setPhoneNumber(toCopy.phoneNumber);
		if (toCopy.gender != null) this.setGender(toCopy.gender);
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

	@JsonIgnore
	public Set<R_UserInterest> getInterests() {
		return interestedIn;
	}

	@JsonIgnore
	public Iterable<GroupEntity> getMajors() {
		Set<GroupEntity> majors = new HashSet<>();
		for (R_UserGroup relation : partOf) {
		    if (relation.getGroup().groupType == GroupType.STUDENT_MAJOR) {
				majors.add(relation.getGroup());
		    }
		}
		return majors;
	}

	@JsonIgnore
	public Iterable<GroupEntity> getColleges() {
		Set<GroupEntity> colleges = new HashSet<>();
		for (R_UserGroup relation : partOf) {
			if (relation.getGroup().groupType == GroupType.COLLEGE) {
				colleges.add(relation.getGroup());
			}
		}
		return colleges;
	}

	public boolean validate(){
		return username != null && email != null && firstName != null && lastName != null && passwordHash != null && classification != null;
	}

}