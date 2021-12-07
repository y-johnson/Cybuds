package com.yjohnson.backend.entities.User;

import com.yjohnson.backend.entities.DB_Relations.R_UserGroup;
import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.DB_Relations.UserGroupRepository;
import com.yjohnson.backend.entities.DB_Relations.UserInterestRepository;
import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupRepository;
import com.yjohnson.backend.entities.Interest.InterestEntity;
import com.yjohnson.backend.entities.Interest.InterestRepository;
import com.yjohnson.backend.exceptions.CybudsActionResultsInConflictException;
import com.yjohnson.backend.exceptions.CybudsEntityByIdNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
	public final UserGroupService ugService = new UserGroupService();
	public final UserInterestService uiService = new UserInterestService();

	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final UserGroupRepository userGroupRepository;
	private final InterestRepository interestRepository;
	private final UserInterestRepository userInterestRepository;

	public UserService(UserRepository userRepository, GroupRepository groupRepository, UserGroupRepository userGroupRepository,
	                   InterestRepository interestRepository, UserInterestRepository userInterestRepository) {
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.userGroupRepository = userGroupRepository;
		this.interestRepository = interestRepository;
		this.userInterestRepository = userInterestRepository;
	}

	/**
	 * Retrieves a {@code User} from the database whose ID or username matches the given identifier. The given String is first parsed as a {@code
	 * Long} for ID and, if not parseable, as a username.
	 *
	 * @param identifier the id or username of the user to retrieve
	 *
	 * @return the {@code User} object that corresponds with the identifier.
	 */
	public Optional<User> getUserByString(String identifier) {
		Optional<User> optionalUser;
		try {
			// Treat it as a Long first (id)
			Long id = Long.parseLong(identifier);
			optionalUser = userRepository.findById(id);     // 1
		} catch (NumberFormatException e) {
			// If it is not a long, treat it as a String (username)
			optionalUser = userRepository.findByUsername(identifier);   // 1
		}
		return optionalUser;
	}

	/**
	 * Deletes the {@code User} that corresponds to the given ID from the database.
	 *
	 * @param id the ID of the {@code User} to be deleted.
	 *
	 * @return a {@code Optional} with a clone of the deleted {@code User} object.
	 */
	public Optional<User> deleteUserByID(Long id) throws CloneNotSupportedException {
		Optional<User> optionalUser = getUserByID(id);
		if (optionalUser.isPresent()) {
			User deleted = optionalUser.get().clone();
			userRepository.delete(optionalUser.get());                  // 2
			return Optional.of(deleted);
		}
		return Optional.empty();
	}

	/**
	 * Retrieves a {@code User} from the database whose ID matches the given ID.
	 *
	 * @param id the ID of the user to retrieve
	 *
	 * @return the {@code User} object that corresponds with the ID.
	 */
	public Optional<User> getUserByID(Long id) {
		return userRepository.findById(id);
	}

	/**
	 * Updates the {@code User} with the values of the given {@code User} object. It is not possible to update the ID, group relations, or interests
	 * via this method. Values that do not need to be updated can be omitted.
	 *
	 * @param valuesToUpdate a {@code User} that holds the values to update.
	 * @param user           the {@code User} to be updated.
	 *
	 * @return the updated {@code User} object.
	 */
	public User saveUpdatedUser(User user, User valuesToUpdate) {
		return userRepository.save(user.updateContents(valuesToUpdate));
	}

	/**
	 * Retrieves all the {@code User} objects in the database.
	 *
	 * @return all the users in the database
	 */
	public Iterable<User> getAllUsersFromDB() {
		return userRepository.findAll();
	}

	class UserGroupService {
		/**
		 * Retrieves all {@code R_UserGroup} objects that a user (identified by the given ID) is a part of.
		 *
		 * @param id id of the user to search for.
		 *
		 * @return an optional iterable collection of {@code R_UserGroup} that the user participates in.
		 */
		public Optional<Iterable<R_UserGroup>> getGroupsOfUserByID(Long id) {
			Optional<User> optionalUser = getUserByID(id);
			Optional<Iterable<R_UserGroup>> groups = Optional.empty();
			if (optionalUser.isPresent())
				groups = Optional.of(optionalUser.get().getGroups());
			return groups;
		}

		public R_UserGroup addRelationToUser(Long uid, Long gid) throws CybudsEntityByIdNotFoundException, CybudsActionResultsInConflictException {

			Optional<User> user = getUserByID(uid);
			Optional<GroupEntity> optionalGroup = groupRepository.findById(gid);// 2
			if (!user.isPresent() || !optionalGroup.isPresent()) throw new CybudsEntityByIdNotFoundException();
			if (userGroupRepository.findByUserAndGroup(user.get(), optionalGroup.get()).isPresent())
				throw new CybudsActionResultsInConflictException();
			R_UserGroup relation = userGroupRepository.save(
					new R_UserGroup(
							user.get(),
							optionalGroup.get(),
							LocalDateTime.now()
					)); //4

			user.get().getGroups().add(relation);
			optionalGroup.get().members.add(relation);
			userRepository.save(user.get()); // 5
			groupRepository.save(optionalGroup.get()); // 6
			return relation;
		}

		public R_UserGroup deleteRelationForUser(Long uid, Long gid) throws CybudsEntityByIdNotFoundException,
				CybudsActionResultsInConflictException {
			Optional<User> user = getUserByID(uid);
			Optional<GroupEntity> optionalGroup = groupRepository.findById(gid);// 2
			if (!user.isPresent() || !optionalGroup.isPresent()) throw new CybudsEntityByIdNotFoundException();
			Optional<R_UserGroup> relation = userGroupRepository.findByUserAndGroup(user.get(), optionalGroup.get());
			if (!relation.isPresent()) throw new CybudsEntityByIdNotFoundException();

			user.get().getGroups().remove(relation.get());
			optionalGroup.get().members.remove(relation.get());
			userGroupRepository.delete(relation.get()); //4
			userRepository.save(user.get()); // 5
			groupRepository.save(optionalGroup.get()); // 6
			return relation.get();
		}
	}

	class UserInterestService {
		/**
		 * Retrieves all {@code R_UserInterest} objects that a user (identified by the given ID) is a part of.
		 *
		 * @param id id of the user to search for.
		 *
		 * @return an optional iterable collection of {@code R_UserInterest} that the user participates in.
		 */
		public Optional<Iterable<R_UserInterest>> getInterestsOfUserByID(Long id) {
			Optional<User> optionalUser = getUserByID(id);
			Optional<Iterable<R_UserInterest>> interests = Optional.empty();
			if (optionalUser.isPresent())
				interests = Optional.of(optionalUser.get().getInterests());
			return interests;
		}

		public R_UserInterest addRelationToUser(Long uid, Long gid) throws CybudsEntityByIdNotFoundException,
				CybudsActionResultsInConflictException {
			Optional<User> user = getUserByID(uid);
			Optional<InterestEntity> optionalInterest = interestRepository.findById(gid);// 2
			if (!user.isPresent() || !optionalInterest.isPresent()) throw new CybudsEntityByIdNotFoundException();
			if (userInterestRepository.findByUserAndInterest(user.get(), optionalInterest.get()).isPresent())
				throw new CybudsActionResultsInConflictException();
			R_UserInterest relation = userInterestRepository.save(
					new R_UserInterest(
							user.get(),
							optionalInterest.get(),
							LocalDateTime.now()
					)); //4

			user.get().getInterests().add(relation);
			optionalInterest.get().interested.add(relation);
			userRepository.save(user.get()); // 5
			interestRepository.save(optionalInterest.get()); // 6
			return relation;
		}

		public R_UserInterest deleteRelationForUser(Long uid, Long gid) throws CybudsEntityByIdNotFoundException,
				CybudsActionResultsInConflictException {
			Optional<User> user = getUserByID(uid);
			Optional<InterestEntity> optionalInterest = interestRepository.findById(gid);// 2
			if (!user.isPresent() || !optionalInterest.isPresent()) throw new CybudsEntityByIdNotFoundException();
			Optional<R_UserInterest> relation = userInterestRepository.findByUserAndInterest(user.get(), optionalInterest.get());
			if (!relation.isPresent()) throw new CybudsEntityByIdNotFoundException();

			user.get().getInterests().remove(relation.get());
			optionalInterest.get().interested.remove(relation.get());
			userInterestRepository.delete(relation.get()); //4
			userRepository.save(user.get()); // 5
			interestRepository.save(optionalInterest.get()); // 6
			return relation.get();
		}
	}


}