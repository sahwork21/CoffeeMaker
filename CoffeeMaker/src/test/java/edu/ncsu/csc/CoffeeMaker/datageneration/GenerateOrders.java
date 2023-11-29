package edu.ncsu.csc.CoffeeMaker.datageneration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;
import edu.ncsu.csc.CoffeeMaker.services.CustomerRequestService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class GenerateOrders {

    /**
     * Objects for the CRUD ops
     */
    @Autowired
    private CustomerRequestService crs;

    @Autowired
    private CustomerService        cs;

    @Autowired
    private RecipeService          rs;

    @Transactional
    @Test
    public void testCustomerRequestService () {
        final Recipe r1 = new Recipe();
        r1.setName( "Cafe" );
        r1.setPrice( 4 );
        r1.addIngredient( new Ingredient( "Coffee", 1, false ) );

        final Customer c1 = new Customer();

        c1.setUsername( "user" );
        c1.setPassword( "pass" );

        final CustomerRequest cr1 = new CustomerRequest();

        cr1.setCustomer( c1 );
        cr1.setRecipe( r1.getName() );
        cr1.setStatus( OrderState.UNFULFILLED );

        c1.addOrder( cr1 );

        cr1.setPlacedAt();
        // Make sure you link up all the objects correctly

        rs.save( r1 );

        // Then save and make sure everything appears in database
        crs.save( cr1 );

        assertEquals( 1, cs.findAll().size() );
        assertEquals( 1, crs.findAll().size() );
        assertEquals( 1, rs.findAll().size() );

        // Assert that everything is correct for fields of every object
        final CustomerRequest savedCrs1 = crs.findAll().get( 0 );
        final Customer savedCs1 = cs.findByUsername( "user" );
        final Recipe savedRs1 = rs.findByName( "Cafe" );

        assertEquals( 1, savedCs1.getOrders().size() );
        assertEquals( "Cafe", savedCs1.getOrders().get( 0 ).getRecipe() );
        assertNotNull( savedCs1.getOrders().get( 0 ).getPlacedAt() );
        // assertEquals( 4, (int) savedCs1.getOrders().get( 0
        // ).getRecipe().getPrice() );

        assertEquals( savedCs1.getUsername(), savedCrs1.getCustomer().getUsername() );
        assertTrue( savedRs1.getName().equals( savedCrs1.getRecipe() ) );
        assertTrue( savedRs1.equals( r1 ) );

        // Make sure we can get the orders by state
        assertEquals( 1, crs.findAll().size() );
        assertEquals( 1, crs.findByStatus( OrderState.UNFULFILLED ).size() );
        assertEquals( 0, crs.findByStatus( OrderState.READY_TO_PICKUP ).size() );
        assertEquals( 0, crs.findByStatus( OrderState.HISTORY ).size() );

        // Delete by state
        crs.deleteByStatus( OrderState.UNFULFILLED );
        assertEquals( 0, crs.findAll().size() );
        assertEquals( 0, crs.findByStatus( OrderState.UNFULFILLED ).size() );
    }

}
