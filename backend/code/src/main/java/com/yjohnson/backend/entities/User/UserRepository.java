package com.yjohnson.backend.entities.User;

import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findUserByEmail(String email);
	Optional<User> findUserByUsername(String username);
}