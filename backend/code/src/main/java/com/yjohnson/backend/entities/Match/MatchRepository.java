package com.yjohnson.backend.entities.Match;

import com.yjohnson.backend.entities.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<MatchEntity, Long> {


	@Query(value = "SELECT m FROM MatchEntity m, User u WHERE m.matcher = u || m.matchee = u", nativeQuery = true)
	List<MatchEntity> findAllWhereUserPresent(User matcher);
}