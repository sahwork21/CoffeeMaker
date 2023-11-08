package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * The class representation of the manager in the system. Manager should be able
 * to create new baristas, view order histories and etc. A list of managers will
 * be put into the system directly.
 */
@Entity
@JsonIgnoreProperties ( value = { "password" } )
public class Staff extends AbstractUser {

    /**
     * Default constructor for Spring
     */
    public Staff () {

    }

    /**
     * Generic customer generator. The role is fixed
     *
     * @param role
     *            the role of the Staff we are creating they can be a Barist or
     *            Manager
     */
    public Staff ( final Role role ) {
        super( "", "", role );
    }

    /**
     * Parametrized constructor that will set the username and password. Use the
     * AbstractUser super parent constructor for now, but we will eventually n
     *
     * @param username
     *            the username of the staff
     * @param password
     *            the password of the staff
     * @param role
     *            the role of the Staff member we are creating
     */
    public Staff ( final String username, final String password, final Role role ) {
        super( username, password, role );
    }

    /**
     * Verify that this is in fact a staff with the correct staff roles Cannot
     * allow people to create users with wrong roles
     *
     * @return true if the role is a barista or manager false otherwise
     */
    @Override
    public boolean checkUser () {
        if ( Role.BARISTA != getRoleType() && Role.MANAGER != getRoleType() ) {
            return false;
        }

        return super.checkUser();
    }

}
