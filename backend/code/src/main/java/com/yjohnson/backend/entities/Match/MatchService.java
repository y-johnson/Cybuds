package com.yjohnson.backend.entities.Match;

import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupType;
import com.yjohnson.backend.entities.User.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {
	private final MatchRepository matchRepository;

	public MatchService(MatchRepository matchRepository) {
		this.matchRepository = matchRepository;
	}

	public Iterable<MatchEntity> matchUser(User currentUser, Iterable<User> allUsers) {
		Set<MatchEntity> set = new TreeSet<>(Comparator.comparingInt(MatchEntity::getScore).reversed());
		allUsers.forEach(user -> {
			if (!currentUser.equals(user)) {
				List<MatchEntity> list = matchRepository.findByMatcherAndMatchee(currentUser, user);
				if (!list.isEmpty()) {
					for (MatchEntity m : list) {
						m.refreshScore();
					}
					matchRepository.saveAll(list);
				} else {
					matchRepository.save(new MatchEntity(
							currentUser,
							user
					));
				}
			}
		});
		return matchRepository.findAllWhereUserPresentDescScore(currentUser);
	}

	public Iterable<MatchEntity> matchUserByChoice(GroupType choice, User currentUser, Iterable<User> allUsers) {
		return matchRepository.saveAll(populateMatchSet(currentUser, choice, allUsers));
	}

	private TreeSet<MatchEntity> populateMatchSet(User currentUser, GroupType choice, Iterable<User> allUsers) {
		TreeSet<MatchEntity> set = new TreeSet<>(Comparator.comparingInt(MatchEntity::getScore).reversed());
		allUsers.forEach(user -> {
			if (!currentUser.equals(user)) {

				switch (choice) {
					case STUDENT_CLASS:
						if (currentUser.getClassification() == user.getClassification()) {
							set.add(new MatchEntity(currentUser, user));
						}
						break;
					case COLLEGE:
					case STUDENT_MAJOR:
						/* Combines majors and colleges into one list and finds the intersection between them */
						Set<GroupEntity> groups = new HashSet<>();
						Set<GroupEntity> against = new HashSet<>();

						groups.addAll(currentUser.getColleges());
						groups.addAll(currentUser.getMajors());

						against.addAll(user.getColleges());
						against.addAll(user.getMajors());

						groups.retainAll(against);
						if (groups.size() > 0) set.add(new MatchEntity(currentUser, user));
						break;
					default:
						break;
				}
			}
		});
		return set;
	}
//
//	/**
//	 * Helper method tracks the number of characteristics that are the same between two users.
//	 *
//	 * @param one  user one
//	 * @param two, user two
//	 *
//	 * @return returns int of the number of shared characteristics
//	 */
//	public int aggregateMatchScore(User one, User two) {
//		int i = 0;
//
//		/* Same year */
//		if (one.getClassification() == two.getClassification()) i++;
//
//		/* Iterates through all interests; counts if any match */
//		for (R_UserInterest r1 : one.getInterests()) {
//			for (R_UserInterest r2 : two.getInterests()) {
//				if (r1 == r2) ++i;
//			}
//		}
//
//		/* Combines majors and colleges into one list and finds the intersection between them */
//		Set<GroupEntity> groups = new HashSet<>();
//		Set<GroupEntity> against = new HashSet<>();
//
//		groups.addAll(one.getColleges());
//		groups.addAll(one.getMajors());
//
//		against.addAll(two.getColleges());
//		against.addAll(two.getMajors());
//
//		groups.retainAll(against);
//		i += groups.size();
//
//		return i;
//	}

	public Optional<User> matchUserRandomlyByChoice(GroupType choice, User currentUser, Iterable<User> allUsers) {

		TreeSet<MatchEntity> set = populateMatchSet(currentUser, choice, allUsers);

		int toGet = new Random().nextInt(set.size());
		int i = 0;

		for (MatchEntity m : set) {
			if (i == toGet) return Optional.of(m.getMatchee());
			++i;
		}
		return Optional.empty();
	}

	public Optional<User> matchUserRandomly(User currentUser, Iterable<User> allUsers) {
		LinkedList<User> list = new LinkedList<>();
		allUsers.forEach(list::add);

		int size = list.size();
		Random r = new Random();

		Optional<User> matchingUser = Optional.empty();
		while (!list.isEmpty()) {
			int index = r.nextInt(size);
			matchingUser = Optional.ofNullable(list.get(index));
			if (!matchingUser.isPresent() || matchingUser.get().getId().equals(currentUser.getId())) {
				list.remove(index);
			} else return matchingUser;
		}

		return matchingUser;
	}
}

/* https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values */
class MapUtil {
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
}