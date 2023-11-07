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
public class Manager extends Staff {

    /**
     * Generic customer generator for spring
     */
    public Manager () {

    }

    /**
     * Parametrized constructor that will set the username and password. Use the
     * AbstractUser super parent constructor for now, but we will eventually n
     *
     * @param username
     *            the username of the customer
     * @param password
     *            the password of the customer
     */
    public Manager ( final String username, final String password ) {
        super( username, password, Role.MANAGER );
    }

    /**
     * Verify that this is in fact a manager with the correct manager role
     * Cannot allow people to create users with wrong roles
     *
     * @return true if the role is a manager false otherwise
     */
    @Override
    public boolean checkUser () {
        if ( Role.MANAGER != getRoleType() ) {
            return false;
        }

        return super.checkUser();
    }

}
