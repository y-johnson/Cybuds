package com.yjohnson.backend.entities.DB_Relations;

import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class R_UserInterest implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "interest_id")
	InterestEntity interest;

	@Column(nullable = false, name = "date_added")
	LocalDateTime dateAdded;

	protected R_UserInterest() {
	}

	public R_UserInterest(User user, InterestEntity interest, LocalDateTime dateAdded) {
		this.user = user;
		this.interest = interest;
		this.dateAdded = dateAdded;
	}
}
