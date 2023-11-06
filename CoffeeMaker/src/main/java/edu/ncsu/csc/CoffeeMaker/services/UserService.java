package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * Handler of the CRUD operations involving all users in the system. We have all
 * the functions of the Service class and the ability to find a user by
 * username.
 *
 * @param <E>
 *            generic for the User class we want
 */
@Component
@Transactional
public class UserService <E extends AbstractUser> extends Service<AbstractUser, Long> {

    /**
     * Repository object that provides us the operations
     */
    @Autowired
    private UserRepository<E> userRepository;

    @Override
    protected JpaRepository<AbstractUser, Long> getRepository () {

        return userRepository;
    }

    /**
     * Find a user with the matching username
     *
     * @param username
     *            username of the user we are looking for
     * @return found user, null if no user with the username exists
     */
    public AbstractUser findByUsername ( final String username ) {
        return userRepository.findByUsername( username );

    }

    /**
     * Save the user but encrypt the password first
     */
    @Override
    public void save ( final AbstractUser user ) {
        final PasswordEncoder pe = new BCryptPasswordEncoder();

        final String encrypted = pe.encode( user.getPassword() );

        user.setPassword( encrypted );

        // We have encrypted the password now just call the parent save method
        super.save( user );
    }

    /**
     * Finds all the Staff members by the user type
     *
     * @return a list of the staff members by barista or manager
     */
    public List<AbstractUser> findByRoleType ( final Role roleType ) {
        return userRepository.findByRoleType( roleType );
    }

}
