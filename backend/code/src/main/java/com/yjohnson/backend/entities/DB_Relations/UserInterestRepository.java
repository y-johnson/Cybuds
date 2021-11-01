package com.yjohnson.backend.entities.DB_Relations;

import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInterestRepository extends CrudRepository<R_UserInterest, Long> {
	List<R_UserInterest> findAllByUser(User user);
	Optional<R_UserInterest> findByUserAndInterest(User user, InterestEntity interest);
}
