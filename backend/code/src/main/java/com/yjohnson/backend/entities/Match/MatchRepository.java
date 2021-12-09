package com.yjohnson.backend.entities.Match;

import com.yjohnson.backend.entities.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MatchRepository extends CrudRepository<MatchEntity, Long> {

	List<MatchEntity> findByMatcherAndMatchee(User matcher, User matchee);

	@Query(value = "SELECT DISTINCT * FROM match_entity m, users u WHERE m.matcher_id = u.id OR m.matchee_id = u.id ORDER BY m.score DESC ",
			nativeQuery = true)
	Set<MatchEntity> findAllWhereUserPresentDescScore(User matcher);
}