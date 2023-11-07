package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

class StaffTest {

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor () {
        final Staff staff = new Staff();
        assertNull( "Username should be null", staff.getUsername() );
        assertNull( "Password should be null", staff.getPassword() );
        assertNull( "Role should be null", staff.getRoleType() );
    }

    /**
     * Test the parameterized construtor.
     */
    @Test
    public void testParameterConstructor () {
        final Staff barista = new Staff( "john", "johnie!23", Role.BARISTA );
        assertEquals( "john", barista.getUsername() );
        assertEquals( "johnie!23", barista.getPassword() );
        assertEquals( Role.BARISTA, barista.getRoleType() );

        final Staff manager = new Staff( "BigBoy", "iamthebestmanager", Role.MANAGER );
        assertEquals( "BigBoy", manager.getUsername() );
        assertEquals( "iamthebestmanager", manager.getPassword() );
        assertEquals( Role.MANAGER, manager.getRoleType() );
    }

    /**
     * Tests the constructor with role fixed
     */
    @Test
    public void testDefaultConstructorWithRole () {
        final Staff barista = new Staff( Role.BARISTA );
        assertEquals( "", barista.getUsername() );
        assertEquals( "", barista.getPassword() );
        assertEquals( Role.BARISTA, barista.getRoleType() );

        final Staff manager = new Staff( Role.MANAGER );
        assertEquals( "", manager.getUsername() );
        assertEquals( "", manager.getPassword() );
        assertEquals( Role.MANAGER, manager.getRoleType() );
    }

    /**
     * Test the checkUser method for a valid role.
     */
    @Test
    public void testCheckUserValidRole () {
        final Staff staffBarista = new Staff( "baristaUser", "baristaPass", Role.BARISTA );
        assertTrue( "checkUser should return true for a valid role of BARISTA", staffBarista.checkUser() );

        final Staff staffManager = new Staff( "managerUser", "managerPass", Role.MANAGER );
        assertTrue( "checkUser should return true for a valid role of MANAGER", staffManager.checkUser() );
    }

    /**
     * Test the checkUser method for an invalid role
     */
    @Test
    public void testCheckUserInvalidRole () {
        final Staff staff = new Staff( "user", "pass", Role.CUSTOMER );
        assertFalse( "checkUser should return false for a Customer role", staff.checkUser() );
    }

}
