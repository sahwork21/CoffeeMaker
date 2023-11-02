package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;

/**
 * This class provides the UserService with the necessary CRUD operations.
 * Operations provided by the JpaRepository.
 */
public interface UserRepository extends JpaRepository<AbstractUser, Long> {

    /**
     * Finds a user by the field username in the repo
     *
     * @param username
     *            of the User
     * @return null if no user is found, an object if the user is found.
     */
    AbstractUser findByUsername ( String username );

}
