package com.yjohnson.backend.entities.DB_Relations;

import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends CrudRepository<R_UserGroup, Long> {
	List<R_UserGroup> findAllByUser(User user);
	List<R_UserGroup> findByUserAndGroup(User user, GroupEntity group);
}
