package edu.ncsu.csc.CoffeeMaker.datageneration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class GenerateUsersSimple {

    /**
     * Repo to generate the CRUD interface for Customer
     */
    @Autowired
    private CustomerService cs;

    @BeforeEach
    public void setup () {
        cs.deleteAll();
    }

    /**
     * Test that Users get saved to the table of all users and a table divided
     * by roles.
     */
    @Test
    @Transactional
    public void testUserGeneration () {

    }

    /**
     * Test that customers get generated properly
     */
    @Test
    // @Transactional
    public void testCustomerGeneration () {
        final Customer c = new Customer( "jcharles", "password" );
        assertEquals( 0, cs.findAll().size() );

        cs.save( c );

        assertEquals( 1, cs.findAll().size() );
    }

}
