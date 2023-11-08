package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * The Parent Class of all users that have an account in the system. Every user
 * has a username, password, and Role Passwords should not be returned to the
 * frontend with a JSON Roles determine what privileges the user has.
 */

@Entity
@Table ( name = "users" )
public class AbstractUser extends DomainObject {
    /** User id */
    @Id
    @GeneratedValue
    private Long   id;

    /**
     * Username for the user to identify who they are in the system
     */
    private String username;

    /**
     * Password for the user to allow login with the username. This value should
     * be hashed. Do not return it in the JSON files Use the transient field so
     * the front end does not expose the password
     */
    private String password;

    /** The user's role and privileges */
    private Role   roleType;

    /** Constructor empty constructor for Spring */
    public AbstractUser () {

    }

    /**
     * Constructor to be used by subclasses to set all the fields
     *
     * @param username
     *            the username of the user to identify them
     * @param password
     *            the password that we will hash then save
     * @param roleType
     *            the role of the user
     */
    public AbstractUser ( final String username, final String password, final Role roleType ) {

        this.username = username;
        this.password = password;
        this.roleType = roleType;

    }

    /**
     * Get the username of this user
     *
     * @return the user's username
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set the username of the user. We need a non empty username
     *
     * @param userName
     *            the user's username that will be linked to them
     */
    public void setUsername ( final String userName ) {
        if ( userName == null ) {
            throw new IllegalArgumentException( "Invalid name." );
        }
        this.username = userName;

    }

    /*
     * private void hashPassword ( final String password ) { final
     * PasswordEncoder encoder = new BCryptPasswordEncoder(); this.password =
     * encoder.encode( password ); }
     */

    /*
     * Check if the input password and the stored password are matching
     * @param password2 the password input to compare to our password
     * @return true if the passwords match false if not
     */
    /*
     * public boolean matchPassword ( final String password2 ) { final
     * PasswordEncoder encoder = new BCryptPasswordEncoder(); return
     * encoder.matches( password2, password ); }
     */

    /**
     * Set and encrypt the password to save
     *
     * @param password
     *            the password with the associated user to hash
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * gets the password of the specific user
     *
     * @return the encrypted password of this object
     */
    public String getPassword () {
        return this.password;
    }

    /**
     * get the role of the user
     *
     * @return roleType role of the user.
     */
    public Role getRoleType () {
        return roleType;
    }

    /**
     * Set the role type of the user
     *
     * @param rtype
     *            the type of the role
     */
    public void setRoleType ( final Role rtype ) {
        this.roleType = rtype;
    }

    @Override
    public Long getId () {
        return id;

    }

    /**
     * See if the passwords match between what is saved and what is input This
     * method should be used by the login API
     *
     * @param passwordInput
     *            the input by the frontend to login to this user
     * @return true if the passwords match false if not
     */
    public boolean checkPassword ( final String passwordInput ) {
        final PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches( passwordInput, password );
    }

    /**
     * Checks that the user is a valid user with a username, password, and role.
     *
     * @return true if this is a valid user false otherwise
     */
    public boolean checkUser () {
        // No null or empty usernames
        if ( "".equals( username ) || username == null ) {
            return false;
        }

        // No empty passwords
        // You should probably check before saving since saving encrypts the
        // password
        if ( "".equals( password ) || password == null ) {
            return false;
        }

        // Role type required
        if ( roleType == null ) {
            return false;
        }

        return true;
    }

    /**
     * To String that tells us what this user has
     *
     */
    @Override
    public String toString () {
        return "Username: " + username + " Role: " + roleType;
    }

}
