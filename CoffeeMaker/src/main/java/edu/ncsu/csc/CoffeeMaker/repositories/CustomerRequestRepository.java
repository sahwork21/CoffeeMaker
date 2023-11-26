package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;

/**
 * extension of the Jpa repo to get the CRUD operations for a Customer Request.
 * Used in the service class to facilitate CRUD operations
 */
public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Long> {

    /**
     * Find the collection of recipes that match the input status
     *
     * @param status
     *            the status of the orders we want
     * @return a list of orders with correct status
     */
    List<CustomerRequest> findByStatus ( final OrderState status );

    /**
     * Delete all the recipes that match this status. Useful for clearing our
     * order history
     *
     * @param status
     *            the status of order we want to delete
     */
    void deleteByStatus ( final OrderState status );

}
