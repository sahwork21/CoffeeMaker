package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * The Parent Class of all users that have an account in the system. Every user
 * has a username, password, and Role Passwords should not be returned to the
 * frontend with a JSON Roles determine what privileges the user has.
 */

@Entity
@JsonIgnoreProperties ( value = { "password" } )
public abstract class AbstractUser extends DomainObject {
    /** User id */
    @Id
    @GeneratedValue
    private Long         id;

    /**
     * Username for the user to identify who they are in the system
     */
    @Column ( name = "name" )
    private final String username;

    /**
     * Password for the user to allow login with the username. This value should
     * be hashed. Do not return it in the JSON files
     */
    private final String password;

    /** The user's role and privileges */
    private final Role   roleType;

    /** Constructor empty constructor */
    public AbstractUser () {
        username = "";
        password = "";
        roleType = null;
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

    

}
