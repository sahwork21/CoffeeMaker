package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
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

    /**
     * Finds all the Staff members by the user type
     *
     * @return a list of the staff members by barista or manager
     */
    public List<Staff> findByRoleType ( final Role roleType ) {
        return staffRepo.findByRoleType( roleType );
    }

}
