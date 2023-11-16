package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRequestRepository;

@Component
@Transactional
public class CustomerRequestService extends Service<CustomerRequest, Long> {

    @Autowired
    private CustomerRequestRepository cusRepository;

    @Override
    protected JpaRepository<CustomerRequest, Long> getRepository () {
        // TODO Auto-generated method stub
        return cusRepository;
    }

}
