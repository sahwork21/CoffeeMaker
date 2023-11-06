package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;

/**
 * Service class for providing the Repository that carries out the CRUD
 * operations
 */
@Component
@Transactional
public class StaffService extends UserService<Staff> {

    /** Provides the CRUD operations for the Customer object */
    @Autowired
    private StaffRepository staffRepo;

    /**
     * Returns the interface responsible for CRUD operations.
     *
     * @return the encapsulated customer repo for CRUD operations.
     */
    @Override
    public StaffRepository getRepository () {
        return staffRepo;
    }

    /**
     * Finds the customer by the username
     *
     * @return the Staff object with the matching username
     */
    @Override
    public Staff findByUsername ( final String username ) {
        return staffRepo.findByUsername( username );
    }

}
