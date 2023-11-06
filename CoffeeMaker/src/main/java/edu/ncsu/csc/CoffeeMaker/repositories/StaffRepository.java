package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Staff Repository for saving the Staff objects to the server and getting the
 * Staff classes.
 */
public interface StaffRepository extends UserRepository<Staff> {

    /**
     * Returns the staff class by the name and returns it as a Staff object
     *
     * @param username
     *            the username we are looking for
     * @return a Staff object
     */
    @Override
    public Staff findByUsername ( final String username );

    /**
     * Returns a list of Staff members and only staff based on their user types
     *
     * @param roleType
     *            the type of user we are looking for
     * @return a list of Staff objects
     */
    public List<Staff> findByRoleType ( final Role roleType );
}
