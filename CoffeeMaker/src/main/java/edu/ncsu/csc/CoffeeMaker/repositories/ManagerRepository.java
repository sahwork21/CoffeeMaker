package edu.ncsu.csc.CoffeeMaker.repositories;

import edu.ncsu.csc.CoffeeMaker.models.Manager;

/**
 * Manager Repository for saving the Manager objects to the server and getting
 * the Manager classes.
 */
public interface ManagerRepository extends UserRepository<Manager> {
    /**
     * Returns the manager class by the name and returns it as a manager object
     *
     * @param username
     *            the username we are looking for
     * @return a manager object
     */
    @Override
    public Manager findByUsername ( final String username );
}
