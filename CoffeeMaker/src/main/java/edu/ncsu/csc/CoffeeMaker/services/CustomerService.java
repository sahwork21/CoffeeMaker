package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;

/**
 * Service class for providing the Repository that carries out the CRUD
 * operations
 */
@Component
@Transactional
public class CustomerService extends UserService<Customer> {
    /** Provides the CRUD operations for the Customer object */
    @Autowired
    private CustomerRepository customerRepo;

    /**
     * Returns the interface responsible for CRUD operations.
     *
     * @return the encapsulated customer repo for CRUD operations.
     */
    @Override
    public CustomerRepository getRepository () {
        return customerRepo;
    }

    /**
     * Finds the customer by the username
     *
     * @return the Customer object with the matching username
     */
    @Override
    public Customer findByUsername ( final String username ) {

        return customerRepo.findByUsername( username );
    }

}
