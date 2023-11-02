package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

/**
 * The class representation of the Customers in the system. Customers should be
 * able to order recipes, view those orders, and pick them up. When they make an
 * order an order object will be linked to this Customer in the database. When
 * they pick up an order the order will be removed from their list and go to the
 * history of all orders.
 */
@Entity
public class Customer extends AbstractUser {

}
