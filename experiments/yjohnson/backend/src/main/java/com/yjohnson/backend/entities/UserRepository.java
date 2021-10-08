package com.yjohnson.backend.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findUserByEmail(String email);
	Optional<User> findUserByUsername(String username);

}