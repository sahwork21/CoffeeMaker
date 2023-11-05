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
public class Manager extends AbstractUser {

    /**
     * Generic customer generator. The role is fixed
     */
    public Manager () {
        super( "", "", Role.MANAGER );
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

}
