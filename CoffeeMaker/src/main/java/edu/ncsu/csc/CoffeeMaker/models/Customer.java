package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * The class representation of the Customers in the system. Customers should be
 * able to order recipes, view those orders, and pick them up. When they make an
 * order an order object will be linked to this Customer in the database. When
 * they pick up an order the order will be removed from their list and go to the
 * history of all orders.
 */
@Entity
public class Customer extends AbstractUser {

    /**
     * Generic customer generator. The role is fixed
     */
    public Customer () {
        super( "", "", Role.CUSTOMER );
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
    public Customer ( final String username, final String password ) {
        super( username, password, Role.CUSTOMER );
    }

}
