package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;
import edu.ncsu.csc.CoffeeMaker.services.CustomerRequestService;

/**
 * Tests for the CustomerRequest class and its service class to save objects
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CustomerRequestTest {

    @Autowired
    private CustomerRequestService crs;

    @BeforeEach
    public void setUp () {
        crs.deleteAll();

        assertEquals( 0, crs.findAll().size() );
    }

    @Test
    public void testCustomerRequest () {
        final Customer c1 = new Customer( "user", "pass" );
        final Recipe r1 = new Recipe();
        final Ingredient i1 = new Ingredient( "coffee", 5, false );
        final Ingredient i2 = new Ingredient( "sugar", 2, false );
        r1.addIngredient( i1 );
        r1.addIngredient( i2 );
        r1.setName( "Sweet Coffee" );
        r1.setPrice( 3 );
        final CustomerRequest cr1 = new CustomerRequest( c1, "Sweet Coffee", 5 );

        assertEquals( c1, cr1.getCustomer() );
        assertEquals( "Sweet Coffee", cr1.getRecipe() );
        assertEquals( 5, cr1.getPayment() );

        try {
            cr1.setPayment( -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            // expected
        }

        assertEquals( OrderState.UNFULFILLED, cr1.getStatus() );

        try {
            cr1.setStatus( OrderState.HISTORY );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }
        try {
            cr1.setStatus( OrderState.UNFULFILLED );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }

        cr1.setStatus( OrderState.READY_TO_PICKUP );
        assertEquals( OrderState.READY_TO_PICKUP, cr1.getStatus() );
        try {
            cr1.setStatus( OrderState.READY_TO_PICKUP );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }
        try {
            cr1.setStatus( OrderState.UNFULFILLED );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }

        cr1.setStatus( OrderState.HISTORY );
        assertEquals( OrderState.HISTORY, cr1.getStatus() );
        try {
            cr1.setStatus( OrderState.HISTORY );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }
        try {
            cr1.setStatus( OrderState.UNFULFILLED );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }
        try {
            cr1.setStatus( OrderState.READY_TO_PICKUP );
        }
        catch ( final IllegalStateException ise ) {
            // expected
        }
    }

}
