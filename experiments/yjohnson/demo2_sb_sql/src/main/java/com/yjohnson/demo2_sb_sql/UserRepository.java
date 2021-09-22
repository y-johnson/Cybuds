package com.yjohnson.demo2_sb_sql;

import org.springframework.data.repository.CrudRepository;

import com.yjohnson.demo2_sb_sql.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
}