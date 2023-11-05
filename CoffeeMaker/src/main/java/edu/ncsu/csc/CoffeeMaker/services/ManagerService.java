package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.repositories.ManagerRepository;

/**
 * Service class for providing the Repository that carries out the CRUD
 * operations
 */
@Component
@Transactional
public class ManagerService extends UserService<Manager> {
    /** Provides the CRUD operations for the Customer object */
    @Autowired
    private ManagerRepository managerRepo;

    /**
     * Returns the interface responsible for CRUD operations.
     *
     * @return the encapsulated customer repo for CRUD operations.
     */
    @Override
    public ManagerRepository getRepository () {
        return managerRepo;
    }

    /**
     * Finds the customer by the username
     *
     * @return the Customer object with the matching username
     */
    @Override
    public Manager findByUsername ( final String username ) {

        return managerRepo.findByUsername( username );
    }

}
