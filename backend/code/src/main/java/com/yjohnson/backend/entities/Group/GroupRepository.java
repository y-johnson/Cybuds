package com.yjohnson.backend.entities.Group;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
	Optional<GroupEntity> findGroupByName(String name);
}
