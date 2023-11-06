package edu.ncsu.csc.CoffeeMaker.repositories;

import edu.ncsu.csc.CoffeeMaker.models.Staff;

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

}
