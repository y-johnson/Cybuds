package com.yjohnson.backend.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findUserByEmail(String email);

	List<User> findUserByUsername(String username);

}