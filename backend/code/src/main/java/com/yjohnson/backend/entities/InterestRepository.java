package com.yjohnson.backend.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InterestRepository extends CrudRepository<Interest, Long> {
	Optional<Interest> findInterestsByName(String name);
}