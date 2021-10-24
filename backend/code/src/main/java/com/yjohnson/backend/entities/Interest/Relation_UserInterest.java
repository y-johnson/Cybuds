package com.yjohnson.backend.entities.Interest;

import com.yjohnson.backend.entities.User.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Relation_UserInterest {
	@Id
	Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "interest_id")
	InterestEntity interest;

	@Column(nullable = false, name = "date_added")
	LocalDateTime dateAdded;

	@Column(name = "date_removed")
	LocalDateTime dateRemoved;

	float weight;
}
