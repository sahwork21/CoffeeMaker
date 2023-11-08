package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Tests for the user class
 */
class UserTest {

    /**
     * Test the getters and setters for a user along with validation
     */
    @Test
    public void testGetSetValidate () {
        final Customer c1 = new Customer();
        final Manager m1 = new Manager();
        final Staff s1 = new Staff();
        final AbstractUser u1 = new AbstractUser( "", "", null );

        assertThrows( IllegalArgumentException.class, () -> c1.setUsername( null ) );
        assertThrows( IllegalArgumentException.class, () -> m1.setUsername( null ) );
        assertThrows( IllegalArgumentException.class, () -> s1.setUsername( null ) );
        assertThrows( IllegalArgumentException.class, () -> u1.setUsername( null ) );

        u1.setRoleType( Role.CUSTOMER );
        c1.setRoleType( Role.CUSTOMER );
        m1.setRoleType( Role.MANAGER );
        s1.setRoleType( Role.BARISTA );

        // set role
        assertEquals( Role.CUSTOMER, c1.getRoleType() );
        assertEquals( Role.MANAGER, m1.getRoleType() );
        assertEquals( Role.BARISTA, s1.getRoleType() );
        assertEquals( Role.CUSTOMER, u1.getRoleType() );

        assertFalse( c1.checkUser() );
        assertFalse( m1.checkUser() );
        assertFalse( s1.checkUser() );
        assertFalse( u1.checkUser() );

        // Set names and check them

        c1.setUsername( "c1" );
        m1.setUsername( "m1" );
        s1.setUsername( "s1" );
        u1.setUsername( "u1" );

        assertEquals( "c1", c1.getUsername() );
        assertEquals( "m1", m1.getUsername() );
        assertEquals( "s1", s1.getUsername() );
        assertEquals( "u1", u1.getUsername() );

        assertFalse( c1.checkUser() );
        assertFalse( m1.checkUser() );
        assertFalse( s1.checkUser() );
        assertFalse( u1.checkUser() );

        // Set passwords

        c1.setPassword( "passc" );
        m1.setPassword( "passm" );
        s1.setPassword( "passs" );
        u1.setPassword( "passu" );

        assertEquals( "passc", c1.getPassword() );
        assertEquals( "passm", m1.getPassword() );
        assertEquals( "passs", s1.getPassword() );
        assertEquals( "passu", u1.getPassword() );

        assertTrue( c1.checkUser() );
        assertTrue( m1.checkUser() );
        assertTrue( s1.checkUser() );
        assertTrue( u1.checkUser() );

        // Test all the toStrings
        assertEquals( "Username: c1 Role: CUSTOMER", c1.toString() );
        assertEquals( "Username: m1 Role: MANAGER", m1.toString() );
        assertEquals( "Username: s1 Role: BARISTA", s1.toString() );
        assertEquals( "Username: u1 Role: CUSTOMER", u1.toString() );
    }

}
