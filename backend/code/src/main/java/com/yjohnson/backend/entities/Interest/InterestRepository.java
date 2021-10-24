package com.yjohnson.backend.entities.Interest;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InterestRepository extends CrudRepository<InterestEntity, Long> {
	Optional<InterestEntity> findByName(String name);
}
