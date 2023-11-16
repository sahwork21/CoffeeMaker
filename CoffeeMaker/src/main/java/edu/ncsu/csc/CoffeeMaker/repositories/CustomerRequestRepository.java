package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;

/**
 * extension of the Jpa repo to get the CRUD operations for a Customer Request.
 * Used in the service class to facilitate CRUD operations
 */
public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Long> {

}
