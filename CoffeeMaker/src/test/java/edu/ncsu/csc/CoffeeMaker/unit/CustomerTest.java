package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests for the Customer class and its service class to save objects
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class CustomerTest {

    @Autowired
    private CustomerService       cs;

    @Autowired
    private UserService<Customer> us;

    @BeforeEach
    public void setUp () {
        cs.deleteAll();
        us.deleteAll();

        assertEquals( 0, us.findAll().size() );
        assertEquals( 0, cs.findAll().size() );
    }

    /**
     * Test the class construction and saving of the objects
     */
    @Test
    @Transactional
    public void testServiceClass () {

        final Customer c1 = new Customer();
        c1.setUsername( "C1" );
        c1.setPassword( "pass" );
        c1.setRoleType( Role.CUSTOMER );
        final Customer c2 = new Customer( "C2", "pass" );

        assertEquals( "C1", c1.getUsername() );
        assertEquals( "pass", c1.getPassword() );
        assertEquals( Role.CUSTOMER, c1.getRoleType() );

        assertEquals( "C2", c2.getUsername() );
        assertEquals( "pass", c2.getPassword() );
        assertEquals( Role.CUSTOMER, c2.getRoleType() );

        // Save both of these with different services
        cs.create( c2 );
        us.create( c1 );

        assertEquals( 2, cs.findAll().size() );
        assertEquals( 2, us.findAll().size() );

        // Now get them back and check their data types
        final Customer savedC1 = cs.findByUsername( "C1" );
        final Customer savedC2 = cs.findByUsername( "C2" );

        final AbstractUser savedC1ab = us.findByUsername( "C1" );
        final AbstractUser savedC2ab = us.findByUsername( "C2" );

        // Check that usernames and role types were saved

        assertEquals( "C1", savedC1.getUsername() );
        assertEquals( "C2", savedC2.getUsername() );
        assertNotEquals( "pass", savedC1.getPassword() );
        assertNotEquals( "pass", savedC2.getPassword() );
        assertEquals( Role.CUSTOMER, savedC1.getRoleType() );
        assertEquals( Role.CUSTOMER, savedC2.getRoleType() );
        assertTrue( savedC1.checkPassword( "pass" ) );
        assertTrue( savedC2.checkPassword( "pass" ) );

        assertEquals( "C1", savedC1ab.getUsername() );
        assertEquals( "C2", savedC2ab.getUsername() );
        assertNotEquals( "pass", savedC1ab.getPassword() );
        assertNotEquals( "pass", savedC2ab.getPassword() );
        assertEquals( Role.CUSTOMER, savedC1ab.getRoleType() );
        assertEquals( Role.CUSTOMER, savedC2ab.getRoleType() );
        assertTrue( savedC1ab.checkPassword( "pass" ) );
        assertTrue( savedC2ab.checkPassword( "pass" ) );

    }

    /**
     * Test that the validation works
     */
    @Test
    public void testValidation () {
        final Customer c1 = new Customer( "", "pass" );
        final Customer c2 = new Customer( "user", "" );
        final Customer c3 = new Customer( "user", "pass" );
        c3.setRoleType( Role.BARISTA );

        final Customer c4 = new Customer( "user", "pass" );
        c4.setRoleType( null );

        final Customer c5 = new Customer( "user", "pass" );

        final AbstractUser u = us.findByUsername( "user" );

        assertFalse( c1.checkUser() );
        assertFalse( c2.checkUser() );
        assertFalse( c3.checkUser() );
        assertFalse( c4.checkUser() );
        assertTrue( c5.checkUser() );
    }

}
