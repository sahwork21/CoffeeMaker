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
import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.ManagerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class GenerateUsersSimple {

    /**
     * Repo to generate the CRUD interface for Customer
     */
    @Autowired
    private CustomerService           cs;

    /**
     * Repo to generate the CRUD interface for Manager
     */
    @Autowired
    private ManagerService            ms;

    /**
     * Repo to test CRUD operations for Staff classes
     */
    @Autowired
    private StaffService              ss;

    /**
     * Repo object to get CRUD operations for users as AbstractUsers This test
     * is important for the API endpoints.
     */
    @Autowired
    private UserService<AbstractUser> us;

    @BeforeEach
    public void setup () {
        cs.deleteAll();
        ms.deleteAll();
        us.deleteAll();
        ss.deleteAll();
    }

    /**
     * Test that Users get saved to the table of all users and a table divided
     * by roles.
     */
    @Test
    @Transactional
    public void testUserGeneration () {
        final Customer c = new Customer( "customer", "password12" );
        final Manager m = new Manager( "manager", "password34" );
        us.save( m );
        us.save( c );

        assertEquals( 2, us.findAll().size() );

        // Now check that the customer and manager that are saved retained their
        // information
        final AbstractUser savedM = us.findByUsername( "manager" );
        final AbstractUser savedC = us.findByUsername( "customer" );

        assertNotNull( savedM );
        assertNotNull( savedC );
        assertNull( us.findByUsername( "newuser" ) );

        assertEquals( "manager", savedM.getUserName() );

        assertEquals( Role.MANAGER, savedM.getUserType() );

        assertEquals( "customer", savedC.getUserName() );
        assertEquals( Role.CUSTOMER, savedC.getUserType() );

        assertTrue( savedM.checkPassword( "password34" ) );
        assertTrue( savedC.checkPassword( "password12" ) );
        assertFalse( savedM.checkPassword( "password12" ) );
        assertFalse( savedC.checkPassword( "password34" ) );

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
        assertNotNull( savedC.getId() );

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

    /**
     * Test that we can find by role types are generated properly
     */
    @Test
    @Transactional
    public void testGeneration () {
        final Staff s1 = new Staff( "bar1", "p1", Role.BARISTA );
        final Staff s2 = new Staff( "bar2", "p2", Role.BARISTA );
        final Manager m1 = new Manager( "man1", "m1" );
        final Customer c1 = new Customer( "cus1", "c1" );
        final Customer c2 = new Customer( "cus2", "c2" );
        final Customer c3 = new Customer( "cus3", "c3" );
        ss.save( s1 );
        ss.save( s2 );
        ms.save( m1 );
        cs.save( c1 );
        cs.save( c2 );
        cs.save( c3 );

        // Check that the length of all users is the proper length
        assertEquals( 6, us.findAll().size() );
        assertEquals( "bar1", us.findByUsername( "bar1" ).getUserName() );
        assertEquals( "bar2", us.findByUsername( "bar2" ).getUserName() );
        assertEquals( "man1", us.findByUsername( "man1" ).getUserName() );
        assertEquals( "cus1", us.findByUsername( "cus1" ).getUserName() );
        assertEquals( "cus2", us.findByUsername( "cus2" ).getUserName() );
        assertEquals( "cus3", us.findByUsername( "cus3" ).getUserName() );

        // Test the find by role method
        assertEquals( 3, us.findByRoleType( Role.CUSTOMER ).size() );
        assertEquals( 2, us.findByRoleType( Role.BARISTA ).size() );
        assertEquals( 1, us.findByRoleType( Role.MANAGER ).size() );

    }
}
