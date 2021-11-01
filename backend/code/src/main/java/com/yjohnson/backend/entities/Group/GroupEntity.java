package com.yjohnson.backend.entities.Group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yjohnson.backend.entities.DB_Relations.R_UserGroup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class GroupEntity implements Serializable, Cloneable {
	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<R_UserGroup> members;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	@Column(nullable = false)
	String name;
	String description;

	public GroupType groupType;

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
	public GroupEntity clone() throws CloneNotSupportedException {
		return (GroupEntity) super.clone();
	}

	public GroupEntity updateContents(GroupEntity toCopy) {
		if (toCopy.name != null) this.name = toCopy.name;
		if (toCopy.description != null) this.description = toCopy.description;
		return this;
	}
}