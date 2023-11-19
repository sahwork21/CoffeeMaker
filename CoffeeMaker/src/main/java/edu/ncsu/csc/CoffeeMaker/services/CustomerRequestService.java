package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;
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

        return cusRepository;
    }

    /**
     * Find the collection of orders by what state they are in This will be
     * useful for baristas and managers to view current and past orders
     *
     * @param status
     *            the state of orders we are looking for
     * @return a list of orders with the same status as what was called
     */
    public List<CustomerRequest> findByStatus ( final OrderState status ) {
        return cusRepository.findByStatus( status );
    }

    /**
     * Use the repo to mass delete by status. We will take an order status and
     * then delete all of those customer requests. Used for history deletion
     *
     * @param status
     *            the state of the orders we want to delete
     */
    public void deleteByStatus ( final OrderState status ) {
        cusRepository.deleteByStatus( status );
    }

}
