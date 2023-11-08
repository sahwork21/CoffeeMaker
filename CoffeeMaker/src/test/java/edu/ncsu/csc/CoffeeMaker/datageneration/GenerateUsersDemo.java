package edu.ncsu.csc.CoffeeMaker.datageneration;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.ManagerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class GenerateUsersDemo {

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

    /**
     * Repo to test CRUD operations for Staff classes
     */
    @Autowired
    private StaffService    ss;

    /**
     * Create some users for us in our demo
     */
    @Test
    @Transactional
    void testCreateUsers () {
        ms.deleteAll();
        ss.deleteAll();
        cs.deleteAll();

        ss.save( new Staff( "Staff", "Staff", Role.BARISTA ) );
        ms.save( new Manager( "Manager", "Manager" ) );
        cs.save( new Customer( "Customer", "Customer" ) );

        // Make sure enough of the classes exist
        assertEquals( 3, ss.findAll().size() );
    }

}
