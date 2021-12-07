package com.yjohnson.backend.entities.Match;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "MatchEntity")
public class MatchEntity implements Serializable {
	@ManyToOne
	@JsonProperty("matching_user")
	private User matcher;
	@ManyToOne
	@JsonProperty("matched_user")
	private User matchee;
	@JsonProperty("last_updated")
	private LocalDateTime lastUpdated;
	@Column(nullable = false)
	private int score;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	public MatchEntity(User matcher, User matchee, int score) {
		this.matcher = matcher;
		this.matchee = matchee;
		this.score = score;
		this.lastUpdated = LocalDateTime.now();
	}

	public MatchEntity(User matcher, User matchee) {
		this.matcher = matcher;
		this.matchee = matchee;
		refreshScore();
	}

	/**
	 * Calculates the match score of two users. The score calculation is based on the number of matching interests, groups, and classifications.
	 * <p>
	 * This method is equivalent to {@code entity.setScore(calculateScore())}.
	 *
	 * @return the calculated match score of the two users.
	 */
	public int refreshScore() {
		final int score = calculateScore();
		setScore(score);
		return score;
	}

	/**
	 * Calculates the match score of two users. This method does not assign the score to this object, it merely returns it. The score calculation is
	 * based on the number of matching interests, groups, and classifications.
	 *
	 * @return the calculated match score of the two users.
	 */
	public int calculateScore() {
		int i = 0;

		/* Same year */
		if (this.matcher.getClassification() == this.matchee.getClassification()) i++;

		/* Iterates through all interests; counts if any match */
		for (R_UserInterest r1 : this.matcher.getInterests()) {
			for (R_UserInterest r2 : this.matchee.getInterests()) {
				if (r1 == r2) ++i;
			}
		}

		/* Combines majors and colleges into this.matcher list and finds the intersection between them */
		Set<GroupEntity> groups = new HashSet<>();
		Set<GroupEntity> against = new HashSet<>();

		groups.addAll(this.matcher.getColleges());
		groups.addAll(this.matcher.getMajors());

		against.addAll(this.matchee.getColleges());
		against.addAll(this.matchee.getMajors());

		groups.retainAll(against);
		i += groups.size();
		return i;
	}

	public MatchEntity() {
	}

	public int getScore() {
		return score;
	}

	/**
	 * This method updates both the score with the provided value and the {@code lastUpdated} variable with the time at the moment this method was
	 * executed. This should be the only way to update the score. Note that this score is not guaranteed to be constant or permanently the same value
	 * throughout the object's lifetime.
	 *
	 * @param score the score to update this match with.
	 */
	public void setScore(int score) {
		this.lastUpdated = LocalDateTime.now();
		this.score = score;
	}

	public User getMatcher() {
		return matcher;
	}

	public User getMatchee() {
		return matchee;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}