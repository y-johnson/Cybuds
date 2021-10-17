package com.yjohnson.backend.entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRepository extends CrudRepository<Interests, Long> {
	Optional<Interests> findInterestsByName(String name);
}