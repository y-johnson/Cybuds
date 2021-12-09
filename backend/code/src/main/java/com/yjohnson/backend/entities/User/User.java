package com.yjohnson.backend.entities.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjohnson.backend.entities.DB_Relations.R_UserGroup;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupType;
import com.yjohnson.backend.entities.Match.MatchEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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
	@Column(nullable = false)
	private boolean premium;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String passwordHash;
	@Column(nullable = false, length = 15)
	private String firstName;
	@Column(length = 15)
	private String middleName;
	@Column(nullable = false, length = 15)
	private String lastName;
	private String biography;
	private int profilePicture;
	private String address;
	private String phoneNumber;
	@Column(nullable = false)
	private StudentClassification classification;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<R_UserInterest> interestedIn;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<R_UserGroup> partOf;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	public Set<MatchEntity> getMatches() {
		return matches;
	}

	@JsonIgnore
	@OneToMany
	private Set<MatchEntity> matches;

	protected User() {
	}

	public User(String username,
	            String email,
	            String passwordHash,
	            String firstName,
	            String middleName,
	            String lastName,
	            String address,
	            String phoneNumber,
	            StudentClassification classification,
	            Gender gender,
	            Set<R_UserInterest> interestedIn,
	            Set<R_UserGroup> partOf,
	            String biography,
	            boolean premium,
	            int profilePicture,
	            Long id) {
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.classification = classification;
		this.gender = gender;
		this.interestedIn = interestedIn;
		this.partOf = partOf;
		this.biography = biography;
		this.premium = premium;
		this.profilePicture = profilePicture;
		this.id = id;
	}

	public User(String lastName,
	            String firstName,
	            String middleName,
	            String email,
	            String username,
	            String passwordHash,
	            Gender gender,
	            String address,
	            String phoneNumber,
	            String biography,
	            boolean premium,
	            int profilePicture
	) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.email = email;
		this.username = username;
		this.passwordHash = passwordHash;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.biography = biography;
		this.premium = premium;
		this.profilePicture = profilePicture;
	}

	/**
	 * Verifies that a User is completely equal to another. This method verifies that each variable that composes the user object is equivalent.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		/* Object.equals is null-safe */
		return getUsername().equals(user.getUsername())
				&& getEmail().equals(user.getEmail())
				&& getPasswordHash().equals(user.getPasswordHash())
				&& getFirstName().equals(user.getFirstName())
				&& Objects.equals(getMiddleName(), user.getMiddleName())
				&& getLastName().equals(user.getLastName())
				&& Objects.equals(getAddress(), user.getAddress())
				&& Objects.equals(getPhoneNumber(), user.getPhoneNumber())
				&& getClassification() == user.getClassification()
				&& getGender() == user.getGender()
				&& Objects.equals(getInterests(), user.getInterests())
				&& Objects.equals(partOf, user.partOf)
				&& premium == user.premium
				&& getId().equals(user.getId());
	}

	@Override
	protected User clone() throws CloneNotSupportedException {
		return (User) super.clone();
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", passwordHash='" + passwordHash + '\'' +
				", firstName='" + firstName + '\'' +
				", middleName='" + middleName + '\'' +
				", lastName='" + lastName + '\'' +
				", biography='" + biography + '\'' +
				", address='" + address + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", classification=" + classification +
				", gender=" + gender +
				", interestedIn=" + interestedIn +
				", partOf=" + partOf +
				", id=" + id +
				'}';
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

	public StudentClassification getClassification() {
		return classification;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setClassification(StudentClassification classification) {
		this.classification = classification;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	@JsonIgnore
	public Set<R_UserGroup> getGroups() {
		return partOf;
	}
	public int getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(int profilePicture) {
		this.profilePicture = profilePicture;
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
		if (toCopy.biography != null) this.setBiography(toCopy.biography);
		if (toCopy.classification != null) this.setClassification(toCopy.classification);
		if (toCopy.phoneNumber != null) this.setPhoneNumber(toCopy.phoneNumber);
		if (toCopy.gender != null) this.setGender(toCopy.gender);
		return this;
	}

	@JsonIgnore
	public Set<GroupEntity> getMajors() {
		Set<GroupEntity> majors = new HashSet<>();
		for (R_UserGroup relation : partOf) {
			if (relation.getGroup().groupType == GroupType.STUDENT_MAJOR) {
				majors.add(relation.getGroup());
			}
		}
		return majors;
	}

	@JsonIgnore
	public Set<GroupEntity> getColleges() {
		Set<GroupEntity> colleges = new HashSet<>();
		for (R_UserGroup relation : partOf) {
			if (relation.getGroup().groupType == GroupType.COLLEGE) {
				colleges.add(relation.getGroup());
			}
		}
		return colleges;
	}

	public boolean validate() {
		return username != null && email != null && firstName != null && lastName != null && passwordHash != null && classification != null;
	}

}
