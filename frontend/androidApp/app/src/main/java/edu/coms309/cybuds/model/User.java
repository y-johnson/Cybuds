package edu.coms309.cybuds.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * The enum Gender.
 */
enum Gender {
	/**
	 * Male gender.
	 */
	MALE,
	/**
	 * Female gender.
	 */
	FEMALE,
	/**
	 * Other gender.
	 */
	OTHER
}

/**
 * The type User.
 */
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
	private Gender gender;
	private int gradYear;
	private String classification;

	/**
	 * Instantiates a new User.
	 */
	public User() {

	}

	/**
	 * Returns a {@code JSONObject} representation of this {@code User}, as parsed by {@code Gson}.
	 *
	 * @return the equivalent {@code JSONObject} representation of this {@code User}.
	 * @throws JSONException if the Gson object-to-JSON conversion results in an invalid format.
	 */
	public JSONObject toJSONObject() throws JSONException {
		Gson gson = new Gson();
		return new JSONObject(gson.toJson(this));
	}

	/**
	 * Printable string.
	 *
	 * @return the string
	 */
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

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets password hash.
	 *
	 * @return the password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets password hash.
	 *
	 * @param passwordHash the password hash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Gets first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name.
	 *
	 * @param firstName the first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets middle name.
	 *
	 * @return the middle name
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets middle name.
	 *
	 * @param middleName the middle name
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name.
	 *
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets address.
	 *
	 * @param address the address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets phone number.
	 *
	 * @return the phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets phone number.
	 *
	 * @param phoneNumber the phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Sets grad year.
	 *
	 * @param gradYear the grad year
	 */
	public void setGradYear(int gradYear) {
		this.gradYear = gradYear;
	}

	/**
	 * Gets gender.
	 *
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Sets gender.
	 *
	 * @param gender the gender
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * Sets classification.
	 *
	 * @param classification the classification
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}
}
