package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Testing the manager class
 *
 *
 */
public class ManagerTest {

    /**
     * Checks the users with incorrect role for manager class
     */
    @Test
    public void testCheckUserWithCorrectRole () {
        final Manager manager = new Manager( "admin", "adminpass" );
        manager.setRoleType( Role.MANAGER );
        assertTrue( "Manager should have the correct role", manager.checkUser() );
    }

    /**
     * Checks the users with incorrect role for manager class
     */
    @Test
    public void testCheckUserWithIncorrectRole () {
        final Manager manager = new Manager( "admin", "adminpass" );
        // Simulate incorrect role scenario
        manager.setRoleType( Role.CUSTOMER ); // Assuming there's a method to
                                              // change the role
        assertFalse( "Manager with incorrect role should return false", manager.checkUser() );
    }

    /**
     * Tests the parameterless constructor of the Manager class.
     */
    @Test
    public void testManagerDefaultConstructor () {
        final Manager manager = new Manager();
        manager.setRoleType( Role.MANAGER );
        assertNull( "Username should be null", manager.getUsername() );
        assertNull( "Password should be null", manager.getPassword() );
        assertEquals( Role.MANAGER, manager.getRoleType() );
    }

    /**
     * Tests the parameterized constructor of the Manager class.
     */
    @Test
    public void testManagerParameterizedConstructor () {
        final String username = "root";
        final String password = "rootpass";
        final Manager manager = new Manager( username, password );
        assertEquals( "Username should match the one provided", username, manager.getUsername() );
        assertEquals( "Password should match the one provided", password, manager.getPassword() );
        assertEquals( "Role should be set to MANAGER", Role.MANAGER, manager.getRoleType() );
    }

}
