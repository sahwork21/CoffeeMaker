package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    /**
     * The list of orders for this object
     */
    @JsonBackReference
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<CustomerRequest> orders;

    /**
     * Generic customer generator. The role is fixed
     */
    public Customer () {
        orders = new ArrayList<CustomerRequest>();
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

    /**
     * Get the list of orders for a Customer
     *
     * @return the orders for this customer
     */
    public List<CustomerRequest> getOrders () {
        return orders;

    }

    /**
     * Set the list of orders to contain the input orders
     *
     * @param orders
     *            the list of orders to set for this customer
     */
    public void setOrders ( final List<CustomerRequest> orders ) {
        this.orders = orders;
    }

    /**
     * Add an order object to the customer's orders
     *
     * @param order
     *            an order object to add to the list of orders
     */
    public void addOrder ( final CustomerRequest order ) {
        orders.add( order );
    }

    /**
     * Remove an order from the list upon completion
     *
     * @param req
     *            the order to remove from the orders list
     */
    public void removeOrder ( final CustomerRequest req ) {
        orders.remove( req );

    }

}
