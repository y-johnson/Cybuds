package com.yjohnson.backend.entities.User;

import com.yjohnson.backend.entities.DB_Relations.R_UserInterest;
import com.yjohnson.backend.entities.Group.GroupEntity;
import com.yjohnson.backend.entities.Group.GroupType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a given user to the database. This method sanitizes the input to a degree; the fields will be trimmed, names will be capitalized in
     * Title case (e.g. "marTHa" -> "Martha"), the username and email will be lowercase and the phone number will be reduced to a max of 10 digits.
     * <p>
     * If the given object contains repeated unique fields, then those fields are returned alongside a CONFLICT status code.
     *
     * @param newUser the user object to insert into the database.
     * @return a response entity with the created {@code User} object (CREATED) or the conflicting value (CONFLICT).
     */
    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        newUser.setFirstName(StringUtils.trimWhitespace(StringUtils.capitalize(newUser.getFirstName().toLowerCase())));
        newUser.setMiddleName(StringUtils.trimWhitespace(StringUtils.capitalize(newUser.getMiddleName().toLowerCase())));
        newUser.setLastName(StringUtils.trimWhitespace(StringUtils.capitalize(newUser.getLastName().toLowerCase())));
        newUser.setUsername(StringUtils.trimAllWhitespace(newUser.getUsername().toLowerCase()));
        newUser.setEmail(StringUtils.trimAllWhitespace(newUser.getEmail().toLowerCase()));
        newUser.setPhoneNumber(StringUtils.deleteAny(newUser.getPhoneNumber(), "-()/_-+ ").substring(0, 10));

        if (userRepository.findUserByEmail(newUser.getEmail()).isPresent()) {               // 1
            return new ResponseEntity<>(newUser.getEmail(), HttpStatus.CONFLICT);
        } else if (userRepository.findUserByUsername(newUser.getUsername()).isPresent()) {  // 2
            return new ResponseEntity<>(newUser.getUsername(), HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(userRepository.save(newUser), HttpStatus.CREATED);  // 3
        }
    }


    /**
     * Retrieves a {@code User} from the database whose ID or username matches the given path variable. Only one of the two is required.
     *
     * @param id       the id of the user to retrieve
     * @param username the username of the user to retrieve
     * @return the {@code User} object that corresponds with the path variable (OK) or an empty body (BAD REQUEST or NOT FOUND).
     */
    @GetMapping(value = {"/{id}", "/{username}"})
    public ResponseEntity<?> getUser(@PathVariable Optional<Long> id, @PathVariable Optional<String> username) {
        Optional<User> optionalUser;
        if (id.isPresent()) optionalUser = userRepository.findById(id.get());     // 1
        else if (username.isPresent()) optionalUser = userRepository.findUserByUsername(username.get());   // 1
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        /* Don't even ask me, this is apparently the best way to handle Optionals */
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes the {@code User} that corresponds to the given ID from the database.
     *
     * @param id the ID of the {@code User} to be deleted.
     * @return a response entity with the deleted {@code User} object (OK) or an empty body (BAD REQUEST or NOT FOUND).
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Optional<Long> id) {
        if (id.isPresent()) {
            try {
                Optional<User> optionalUser = userRepository.findById(id.get());      // 1
                if (optionalUser.isPresent()) {
                    User deleted = optionalUser.get().clone();
                    userRepository.delete(optionalUser.get());                  // 2
                    return new ResponseEntity<>(deleted, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates the {@code User} that corresponds to the given ID path variable with the values of the request body. It is not possible to update the
     * ID, group relations, or interests via this method. Values that do not need to be updated can be omitted.
     *
     * @param valuesToUpdate a {@code User}-like JSON that holds the values to update.
     * @param id             the ID of the {@code User} to be updated.
     * @return a response entity with the updated {@code User} object (OK) or an empty body (BAD REQUEST, CONFLICT, or NOT FOUND).
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@RequestBody Optional<User> valuesToUpdate, @PathVariable Optional<Long> id) {
        try {
            if (valuesToUpdate.isPresent() && id.isPresent()) {
                Optional<User> fromDB = userRepository.findById(id.get());     // 1
                return fromDB.map(user -> new ResponseEntity<>(
                        userRepository.save(user.updateContents(valuesToUpdate.get())),  // 2
                        HttpStatus.OK
                )).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves all the {@code User} objects in the database.
     *
     * @return all the users in the database
     */
    @GetMapping
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();    //1
    }


    //@ tag?
	/*@GetMapping("/{id}/match")
	public ResponseEntity<?> match(@PathVariable Optional<Long> id, @RequestBody GroupType choice) { //will the current user be passed through volley?
		Iterable<User> All = getAllUsers(); //probably should move this for efficiency
		if (id.isPresent()) {
			Optional<User> current = userRepository.findById(id.get());
			if(current.isPresent()) {
				int same = 0;
				User temp = null;
				for (User Bob : All) {
					if (current.get().choice == Bob.choice && current.get().id != Bob.id) { //comparing identifiers?
						int t = add(CurrentUser, Bob) //helper method
						if (t > same) {
							same = t;
							temp = Bob;
						}
					}
				}
			} return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		return temp; //how to send this user back to frontend?
	}*/

    /**
     * Sorts all user profiles that have the same characteristic as the choice of the current user.
     * Calls add() which counts the number of characterists that are shared.
     * Returns user with the most shared characteristics.
     *
     * @param id user id
     * @param choice  the identifier the current user wanted to match with
     * @return user profile with the most in common with current user
     */
    @GetMapping("/{id}/match")
    public ResponseEntity<?> match(@PathVariable Optional<Long> id, @RequestBody GroupType choice) {
        if (id.isPresent()) {
            Optional<User> current = userRepository.findById(id.get());
            if (current.isPresent()) {
                int same = 0;
                int t=0;
                User temp = null;
                Iterable<User> All = getAllUsers();
                for (User Bob : All) {
                    if (Bob.getId() != current.get().getId()) {

                        switch (choice) {
                            case STUDENT_CLASS:
                                if (Bob.classification == current.get().classification){
                                t = add(current.get(), Bob);
                            }
                            break;

                            case COLLEGE:
                                Iterable<GroupEntity> college1 = current.get().getColleges();
                                Iterable<GroupEntity> college2 = Bob.getColleges();
                                for (GroupEntity c1 : college1) {
                                    for (GroupEntity c2 : college2) {
                                        if (c1 == c2) {
                                            t = add(current.get(), Bob);
                                        }
                                    }
                                }
                                break;

                            case STUDENT_MAJOR:
                                Iterable<GroupEntity> major1 = current.get().getMajors();
                                Iterable<GroupEntity> major2 = Bob.getMajors();
                                for (GroupEntity g1 : major1) {
                                    for (GroupEntity g2 : major2) {
                                        if (g1 == g2) {
                                            t = add(current.get(), Bob);
                                        }
                                    }
                                }
                                break;

                            default:
                                break;
                        }


                        if (t > same) {
                            same = t;
                            temp = Bob;
                        }
                    }
                }            return new ResponseEntity<>(temp,HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


        /**
         * Helper method tracks the number of characteristics that are the same between two users.
         * @param one user one
         * @param two, user two
         * @return, returns int of the number of shared characteristics
         */
        public int add (User one, User two){
            int i = 0;
            //iterate through all interests-sees if any match
            for (R_UserInterest r1 : one.interestedIn) {
                for (R_UserInterest r2 : two.interestedIn) {
                    if (r1 == r2) ++i;
                }
            }
            //iterate through all majors-counts if any match
            Iterable<GroupEntity> major1 = one.getMajors();
            Iterable<GroupEntity> major2 = two.getMajors();
            for (GroupEntity g1 : major1) {
                for (GroupEntity g2 : major2) {
                    if (g1 == g2) ++i;
                }
            }

            //iterate through all colleges-counts if any match
            Iterable<GroupEntity> college1 = one.getColleges();
            Iterable<GroupEntity> college2 = two.getColleges();
            for (GroupEntity c1 : college1) {
                for (GroupEntity c2 : college2) {
                    if (c1 == c2) ++i;
                }
            }

            //another point if the students are the same class year
            if (one.classification == two.classification) {
                i++;
            }

            return i;
        }
    }