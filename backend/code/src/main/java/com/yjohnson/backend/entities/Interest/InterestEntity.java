package com.yjohnson.backend.entities.Interest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
public class InterestEntity implements Serializable, Cloneable {
	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<R_UserInterest> interested;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	@Column(nullable = false, unique = true)
	String name;
	String description;

	public InterestEntity() {
	}

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

	/**
	 * Returns a memberwise copy of this object. Note that the {@code} attribute is not reflective of this object's literal ID in the database at any
	 * moment in time; it reflects what it was at the moment this method was called.
	 *
	 * @return a clone of this instance.
	 *
	 * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface. Subclasses that override the {@code
	 *                                    clone} method can also throw this exception to indicate that an instance cannot be cloned.
	 * @see Cloneable
	 */
	@Override
	public InterestEntity clone() throws CloneNotSupportedException {
		return (InterestEntity) super.clone();
	}

	public InterestEntity updateContents(InterestEntity toCopy) {
		if (toCopy.name != null) this.name = toCopy.name;
		if (toCopy.description != null) this.description = toCopy.description;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InterestEntity that = (InterestEntity) o;
		return getId().equals(that.getId()) && getName().equals(that.getName()) && Objects.equals(getDescription(), that.getDescription());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getDescription());
	}

	@Override
	public String toString() {
		return "InterestEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	public InterestEntity(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
