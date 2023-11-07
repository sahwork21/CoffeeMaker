package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * This class provides the UserService with the necessary CRUD operations.
 * Operations provided by the JpaRepository.
 *
 * @param <E>
 *            generic type of the user that will be saved.
 */
public interface UserRepository <E extends AbstractUser> extends JpaRepository<AbstractUser, Long> {

    /**
     * Finds a user by the field username in the repo
     *
     * @param username
     *            of the User
     * @return null if no user is found, an object if the user is found.
     */
    public AbstractUser findByUsername ( String username );

    /**
     * Finds a list of users by the type of role
     *
     * @param roleType
     *            the type of users we are looking for
     * @return a list of users with matching type
     */
    public List<AbstractUser> findByRoleType ( Role roleType );

}
