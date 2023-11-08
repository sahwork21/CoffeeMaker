package edu.ncsu.csc.CoffeeMaker.repositories;

import edu.ncsu.csc.CoffeeMaker.models.Customer;

/**
 * Customer Repository for saving the Customer objects to the server and getting
 * the Customer classes.
 */
public interface CustomerRepository extends UserRepository<Customer> {
    /**
     * Returns the customer class by the name and returns it as a Customer
     * object
     *
     * @param username
     *            the username we are looking for
     * @return a Customer object
     */
    @Override
    public Customer findByUsername ( final String username );
}
