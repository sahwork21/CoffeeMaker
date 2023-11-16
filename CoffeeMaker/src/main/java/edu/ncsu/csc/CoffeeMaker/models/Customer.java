package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * The class representation of the Customers in the system. Customers should be
 * able to order recipes, view those orders, and pick them up. When they make an
 * order an order object will be linked to this Customer in the database. When
 * they pick up an order the order will be removed from their list and go to the
 * history of all orders.
 */
@Entity
@JsonIgnoreProperties ( value = { "password" } )
public class Customer extends AbstractUser {

    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Order> orders;

    /**
     * Generic customer generator. The role is fixed
     */
    public Customer () {

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

    /**
     * Verify that this is in fact a customer with the correct Customer role
     * Cannot allow people to create users with wrong roles
     *
     * @return true if the role is a customer false otherwise
     */
    @Override
    public boolean checkUser () {
        if ( Role.CUSTOMER != getRoleType() ) {
            return false;
        }

        return super.checkUser();
    }

}
