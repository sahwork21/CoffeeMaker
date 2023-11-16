package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRequestRepository;

/**
 * The service class that will make queries and use the repo to manage
 * CustomerRequests (orders) Some special operations for this service will get
 * orders by status, user, and all
 */
@Component
@Transactional
public class CustomerRequestService extends Service<CustomerRequest, Long> {

    /**
     * The facilitator of the CRUD operations for this class
     */
    @Autowired
    private CustomerRequestRepository cusRepository;

    @Override
    protected JpaRepository<CustomerRequest, Long> getRepository () {
        // TODO Auto-generated method stub
        return cusRepository;
    }

}
