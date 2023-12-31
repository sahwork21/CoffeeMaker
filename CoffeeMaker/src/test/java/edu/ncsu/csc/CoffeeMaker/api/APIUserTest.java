package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
class APIUserTest {

    @Autowired
    private MockMvc                   mvc;

    @Autowired
    private WebApplicationContext     context;

    @Autowired
    private UserService<AbstractUser> service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();

    }

    /**
     * Test that you can create a user and all the incorrect outcomes
     *
     * @throws Exception
     *             throws if you mess up
     */
    @Test
    @Transactional
    public void testCreateuser () throws Exception {
        // Create a user of every type and ensure saving works
        final Customer c = new Customer( "cus1", "p1" );
        final Manager m = new Manager( "man1", "m1" );
        final Staff s = new Staff( "bar1", "b1", Role.BARISTA );
        final AbstractUser u = new AbstractUser();

        u.setUsername( "u1" );

        u.setPassword( "pass" );
        u.setRoleType( Role.CUSTOMER );
        System.out.println( TestUtils.asJsonString( u ) );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c ) ) ).andExpect( status().isOk() );
        assertEquals( 2, service.findAll().size() );
        assertEquals( "cus1", service.findByUsername( "cus1" ).getUsername() );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( m ) ) ).andExpect( status().isOk() );
        assertEquals( 3, service.findAll().size() );
        assertEquals( "man1", service.findByUsername( "man1" ).getUsername() );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( s ) ) ).andExpect( status().isOk() );
        assertEquals( 4, service.findAll().size() );
        assertEquals( "bar1", service.findByUsername( "bar1" ).getUsername() );

        // Now just make sure we can find every object and fields match up
        final AbstractUser savedU = service.findByUsername( "u1" );
        final AbstractUser savedC = service.findByUsername( "cus1" );
        final AbstractUser savedM = service.findByUsername( "man1" );
        final AbstractUser savedS = service.findByUsername( "bar1" );

        assertEquals( savedU.getUsername(), u.getUsername() );
        assertEquals( savedC.getUsername(), c.getUsername() );
        assertEquals( savedM.getUsername(), m.getUsername() );
        assertEquals( savedS.getUsername(), s.getUsername() );

        assertEquals( savedU.getRoleType(), Role.CUSTOMER );
        assertEquals( savedC.getRoleType(), Role.CUSTOMER );
        assertEquals( savedM.getRoleType(), Role.MANAGER );
        assertEquals( savedS.getRoleType(), Role.BARISTA );
        assertEquals( savedU.getRoleType(), u.getRoleType() );
        assertEquals( savedC.getRoleType(), c.getRoleType() );
        assertEquals( savedM.getRoleType(), m.getRoleType() );
        assertEquals( savedS.getRoleType(), s.getRoleType() );

        assertTrue( savedU.checkPassword( u.getPassword() ) );
        assertTrue( savedC.checkPassword( c.getPassword() ) );
        assertTrue( savedM.checkPassword( m.getPassword() ) );
        assertTrue( savedS.checkPassword( s.getPassword() ) );

        assertFalse( savedU.checkPassword( "wrong" ) );
        assertFalse( savedC.checkPassword( "wrong" ) );
        assertFalse( savedM.checkPassword( "wrong" ) );
        assertFalse( savedS.checkPassword( "wrong" ) );

        // Now test that illegal operations work
        // Try making duplicate
        final Manager dm1 = new Manager( "bar1", "dhfjka" );
        final Manager dm2 = new Manager( "cus1", "fdsafhj" );
        final Manager dm3 = new Manager( "man1", "fdsafhj" );
        final String response1 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( dm1 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        final String response2 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( dm2 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        final String response3 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( dm3 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        assertEquals( 4, service.findAll().size() );
        assertTrue( response1.contains( "Invalid username or password" ) );
        assertTrue( response2.contains( "Invalid username or password" ) );
        assertTrue( response3.contains( "Invalid username or password" ) );

        final Customer dc1 = new Customer( "man1", "dhfjka" );
        final Customer dc2 = new Customer( "cus1", "fdsafhj" );
        final Customer dc3 = new Customer( "bar1", "fdsafhj" );
        final String response4 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( dc1 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        final String response5 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( dc2 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        final String response6 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( dc3 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        assertEquals( 4, service.findAll().size() );

        assertTrue( response4.contains( "Invalid username or password" ) );
        assertTrue( response5.contains( "Invalid username or password" ) );
        assertTrue( response6.contains( "Invalid username or password" ) );

        final Staff ds1 = new Staff( "bar1", "dhfjka", Role.BARISTA );
        final Staff ds2 = new Staff( "cus1", "fdsafhj", Role.BARISTA );
        final Staff ds3 = new Staff( "cus1", "fdsafhj", Role.BARISTA );
        final String response7 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ds1 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        assertEquals( 4, service.findAll().size() );
        final String response8 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ds2 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        final String response9 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ds3 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        assertEquals( 4, service.findAll().size() );

        assertTrue( response7.contains( "Invalid username or password" ) );
        assertTrue( response8.contains( "Invalid username or password" ) );
        assertTrue( response9.contains( "Invalid username or password" ) );

        // Now enter some invalid users
        final AbstractUser invalid1 = new AbstractUser( "", "pass", Role.CUSTOMER );
        final AbstractUser invalid2 = new AbstractUser( "user", "", Role.CUSTOMER );
        final AbstractUser invalid3 = new AbstractUser( "user", "pass", null );

        final String response10 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( invalid1 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        final String response11 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( invalid2 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        final String response12 = mvc
                .perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( invalid3 ) ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        assertEquals( 4, service.findAll().size() );

        assertTrue( response10.contains( "Invalid username or password" ) );
        assertTrue( response11.contains( "Invalid username or password" ) );
        assertTrue( response12.contains( "Invalid username or password" ) );

        assertNotNull( service.findByUsername( "man1" ) );
        assertNotNull( service.findByUsername( "cus1" ) );
        assertNotNull( service.findByUsername( "bar1" ) );
        assertNotNull( service.findByUsername( "u1" ) );

        assertEquals( 2, service.findByRoleType( Role.CUSTOMER ).size() );
        assertEquals( 1, service.findByRoleType( Role.BARISTA ).size() );
        assertEquals( 1, service.findByRoleType( Role.MANAGER ).size() );

    }

    /**
     * Test that we can login to the right user
     *
     * @throws Exception
     *             if you make a wrong call
     */
    @Test
    @Transactional
    public void testUserLogin () throws Exception {
        // Make some new users of every type

        final Customer c = new Customer( "cus1", "p1" );
        final Manager m = new Manager( "man1", "m1" );
        final Staff s = new Staff( "bar1", "b1", Role.BARISTA );

        service.create( s );
        service.create( c );

        service.create( m );

        assertEquals( 3, service.findAll().size() );

        // Now see that logging in works for every one of them
        final String response1 = mvc
                .perform( post( "/api/v1/users/login/cus1" ).contentType( MediaType.APPLICATION_JSON ).content( "p1" ) )
                .andExpect( status().isOk() ).andDo( print() ).andReturn().getResponse().getContentAsString();
        assertTrue( response1.contains( "\"username\":\"cus1\"" ) );
        assertTrue( response1.contains( "\"roleType\":\"CUSTOMER\"" ) );

        final String response2 = mvc
                .perform( post( "/api/v1/users/login/man1" ).contentType( MediaType.APPLICATION_JSON ).content( "m1" ) )
                .andExpect( status().isOk() ).andDo( print() ).andReturn().getResponse().getContentAsString();
        assertTrue( response2.contains( "\"username\":\"man1\"" ) );
        assertTrue( response2.contains( "\"roleType\":\"MANAGER\"" ) );

        final String response3 = mvc
                .perform( post( "/api/v1/users/login/bar1" ).contentType( MediaType.APPLICATION_JSON ).content( "b1" ) )
                .andExpect( status().isOk() ).andDo( print() ).andReturn().getResponse().getContentAsString();
        assertTrue( response3.contains( "\"username\":\"bar1\"" ) );
        assertTrue( response3.contains( "\"roleType\":\"BARISTA\"" ) );

        // Now try doing illegal things like logging in to users that do not
        // exist
        final AbstractUser u2 = new Staff();
        u2.setUsername( "band" );
        u2.setPassword( "thisisasuperlongpasswordthatnobodywouldevenconceiveaspossible" );
        u2.setRoleType( Role.BARISTA );
        final String response5 = mvc
                .perform( post( "/api/v1/users/login/band" ).contentType( MediaType.APPLICATION_JSON )
                        .content( "thisisasuperlongpasswordthatvenconceiveaspossible" ) )
                .andExpect( status().isBadRequest() ).andDo( print() ).andReturn().getResponse().getContentAsString();
        assertTrue( response5.contains( "Invalid username or password" ) );

        // Now save the user
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u2 ) ) ).andExpect( status().isOk() );

        assertEquals( 4, service.findAll().size() );

        // Now log in with the wrong passcode and again with the right one
        // final String response6 = mvc
        // .perform( post(
        // "/api/v1/users/login/band/thisfjdskabodywouldevenconceiveaspossible"
        // )
        // .contentType( MediaType.APPLICATION_JSON )
        // .content(
        // "thisisasuperlongpasswordthatnobodywouldevenconceiveaspossible" ) )
        // .andExpect( status().isNotFound() ).andDo( print()
        // ).andReturn().getResponse().getContentAsString();
        // assertTrue( response6.contains( "Invalid username or password" ) );
        //
        // final String response7 = mvc
        // .perform( get(
        // "/api/v1/users/band/thisisasuperlongpasswordthatnobodywouldevenconceiveaspossible"
        // ) )
        // .andExpect( status().isOk() ).andDo( print()
        // ).andReturn().getResponse().getContentAsString();
        // assertTrue( response7.contains( "\"username\":\"band\"" ) );
        // assertTrue( response7.contains( "\"roleType\":\"BARISTA\"" ) );

    }

    /**
     * Test the ability to get the baristas
     *
     * @throws Exception
     *             if you make an incorrect API call
     */
    @Test
    @Transactional
    public void testGetBaristas () throws Exception {
        final Customer c1 = new Customer( "cus1", "p1" );
        final Manager m1 = new Manager( "man1", "m1" );
        final Staff s1 = new Staff( "bar1", "b1", Role.BARISTA );
        final Customer c2 = new Customer( "cus2", "p1" );
        final Manager m2 = new Manager( "man2", "m1" );
        final Staff s2 = new Staff( "bar2", "b1", Role.BARISTA );
        final Customer c3 = new Customer( "cus3", "p1" );
        final Manager m3 = new Manager( "man3", "m1" );
        final Staff s3 = new Staff( "bar3", "b1", Role.BARISTA );

        // Now do a post operation to save all of these
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( m1 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( s1 ) ) ).andExpect( status().isOk() );

        final String response1 = mvc.perform( get( "/api/v1/users/barista" ) ).andExpect( status().isOk() )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        assertTrue( response1.contains( "\"username\":\"bar1\"" ) );
        assertTrue( response1.contains( "\"roleType\":\"BARISTA\"" ) );

        assertFalse( response1.contains( "\"username\":\"bar2\"" ) );

        assertFalse( response1.contains( "\"username\":\"bar3\"" ) );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c2 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( m2 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( s2 ) ) ).andExpect( status().isOk() );

        final String response2 = mvc.perform( get( "/api/v1/users/barista" ) ).andExpect( status().isOk() )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        assertTrue( response2.contains( "\"username\":\"bar1\"" ) );
        assertTrue( response2.contains( "\"roleType\":\"BARISTA\"" ) );

        assertTrue( response2.contains( "\"username\":\"bar2\"" ) );

        assertFalse( response2.contains( "\"username\":\"bar3\"" ) );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c3 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( m3 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( s3 ) ) ).andExpect( status().isOk() );

        final String response3 = mvc.perform( get( "/api/v1/users/barista" ) ).andExpect( status().isOk() )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        assertTrue( response3.contains( "\"username\":\"bar1\"" ) );
        assertTrue( response3.contains( "\"roleType\":\"BARISTA\"" ) );

        assertTrue( response3.contains( "\"username\":\"bar2\"" ) );

        assertTrue( response3.contains( "\"username\":\"bar3\"" ) );

    }

    /**
     * Test that we can run our demo user generation
     *
     * @throws Exception
     *             if you make an illegal call
     */
    @Test
    @Transactional
    public void testDemoGeneration () throws Exception {
        mvc.perform( post( "/api/v1/generateusers" ) ).andExpect( status().isOk() );

        // Check that 3 users are there and we can log in to each
        assertEquals( 3, service.findAll().size() );

        assertEquals( 1, service.findByRoleType( Role.BARISTA ).size() );
        assertEquals( 1, service.findByRoleType( Role.CUSTOMER ).size() );
        assertEquals( 1, service.findByRoleType( Role.MANAGER ).size() );

        mvc.perform(
                post( "/api/v1/users/login/Manager" ).contentType( MediaType.APPLICATION_JSON ).content( "Manager" ) )
                .andExpect( status().isOk() );
        mvc.perform(
                post( "/api/v1/users/login/Barista" ).contentType( MediaType.APPLICATION_JSON ).content( "Barista" ) )
                .andExpect( status().isOk() );
        mvc.perform(
                post( "/api/v1/users/login/Customer" ).contentType( MediaType.APPLICATION_JSON ).content( "Customer" ) )
                .andExpect( status().isOk() );
    }

}
