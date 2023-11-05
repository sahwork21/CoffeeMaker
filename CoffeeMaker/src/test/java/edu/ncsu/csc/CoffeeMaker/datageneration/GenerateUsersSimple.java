package edu.ncsu.csc.CoffeeMaker.datageneration;


import static org.junit.Assert.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Customer;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
=======
import edu.ncsu.csc.CoffeeMaker.models.Manager;

import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.ManagerService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class GenerateUsersSimple {

    /**
     * Repo to generate the CRUD interface for Customer
     */
    @Autowired
    private CustomerService cs;

    /**
     * Repo to generate the CRUD interface for Manager
     */
    @Autowired
    private ManagerService  ms;

    @BeforeEach
    public void setup () {
        cs.deleteAll();
        ms.deleteAll();
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
    @Transactional
    public void testCustomerGeneration () {
        final Customer c = new Customer( "jcharles", "password" );
        final Customer empty = new Customer();
        assertEquals( "", empty.getUserName() );
        assertEquals( "", empty.getPassword() );
        assertEquals( Role.CUSTOMER, empty.getUserType() );
        assertEquals( 0, cs.findAll().size() );

        empty.setUserType( Role.MANAGER );

        cs.save( c );
        cs.save( empty );

        assertEquals( 2, cs.findAll().size() );

        final Customer savedC = cs.findByUsername( "jcharles" );
        assertNotNull( savedC );

        assertEquals( c.getUserName(), savedC.getUserName() );
        assertEquals( c.getUserType(), savedC.getUserType() );
        assertEquals( c.getId(), savedC.getId() );
        assertEquals( c.getPassword(), savedC.getPassword() );
        assertEquals( "jcharles", savedC.getUserName() );

        assertTrue( savedC.checkPassword( "password" ) );
        assertFalse( savedC.checkPassword( "wrong" ) );


        assertEquals( Role.CUSTOMER, savedC.getUserType() );
        assertNotNull( cs.findByUsername( "jcharles" ) );
        assertNull( cs.findByUsername( "nothing" ) );


        System.out.println( TestUtils.asJsonString( savedC ) );

        // Check that passwords match for a login

        // assertFalse( TestUtils.asJsonString( savedC ).contains( "password" )
        // );
    }

    /**
     * Test that customers get generated properly
     */
    @Test
    @Transactional
    public void testManagerGeneration () {
        final Manager m = new Manager( "jcharles", "password" );
        assertEquals( 0, ms.findAll().size() );

        ms.save( m );

        assertEquals( 1, ms.findAll().size() );

        final Manager savedM = ms.findByUsername( "jcharles" );
        assertNotNull( savedM );

        assertEquals( m.getUserName(), savedM.getUserName() );
        assertEquals( m.getUserType(), savedM.getUserType() );
        assertEquals( m.getId(), savedM.getId() );
        assertEquals( m.getPassword(), savedM.getPassword() );

        System.out.println( TestUtils.asJsonString( savedM ) );

        // assertFalse( TestUtils.asJsonString( savedC ).contains( "password" )
        // );
    }

}
