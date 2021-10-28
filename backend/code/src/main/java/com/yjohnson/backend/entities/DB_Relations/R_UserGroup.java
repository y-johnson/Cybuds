package com.yjohnson.backend.entities.DB_Relations;

import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class R_UserGroup implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "group_id")
	GroupEntity group;

	@Column(nullable = false, name = "date_added")
	LocalDateTime dateAdded;

	protected R_UserGroup() {
	}

	public R_UserGroup(User user, GroupEntity group, LocalDateTime dateAdded) {
		this.user = user;
		this.group = group;
		this.dateAdded = dateAdded;
	}
}
