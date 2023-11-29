package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;

class CustomerRequestTest {

    @Test
    public void testSetCustomer () {

        final Customer cus = new Customer( "cus1", "p1" );
        final CustomerRequest request = new CustomerRequest();

        request.setCustomer( cus );
        assertEquals( "cus1", request.getCustomer().getUsername() );
        assertEquals( "p1", request.getCustomer().getPassword() );

    }

    @Test
    public void testSetRecipe () {
        final Customer cus = new Customer( "cus1", "p1" );
        final CustomerRequest request = new CustomerRequest( cus, "Mocha", 5 );

        assertEquals( "Mocha", request.getRecipe() );

        request.setRecipe( "Latte" );

        assertEquals( "Latte", request.getRecipe() );
    }

    @Test
    public void testSetPayment () {
        final Customer cus = new Customer( "cus1", "p1" );
        final CustomerRequest request = new CustomerRequest( cus, "Mocha", 5 );
        assertEquals( 5, request.getPayment() );

        request.setPayment( 10 );

        assertEquals( 10, request.getPayment() );
    }

    @Test
    public void testSetStatus () {
        final Customer cus = new Customer( "cus1", "p1" );
        final CustomerRequest request = new CustomerRequest( cus, "Mocha", 5 );
        assertEquals( OrderState.UNFULFILLED, request.getStatus() );

        request.setStatus( OrderState.READY_TO_PICKUP );

        assertEquals( OrderState.READY_TO_PICKUP, request.getStatus() );
    }

    @Test
    public void testDefaultConstructor () {
        // Test default constructor
        final CustomerRequest defaultRequest = new CustomerRequest();
        assertNull( defaultRequest.getCustomer() );
        assertNull( defaultRequest.getRecipe() );
        assertEquals( 0, defaultRequest.getPayment() );
        assertEquals( null, defaultRequest.getStatus() );
    }

    @Test
    public void testSetId () {
        final Customer cus = new Customer( "cus1", "p1" );
        final CustomerRequest request = new CustomerRequest( cus, "Mocha", 5 );

        assertNull( request.getId() );

        // Set an ID using setId() method
        request.setId( 54321L );

        assertEquals( Long.valueOf( 54321L ), request.getId() );
    }

}
