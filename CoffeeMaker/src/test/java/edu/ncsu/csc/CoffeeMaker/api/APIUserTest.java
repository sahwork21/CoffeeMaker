package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void testCreateuser () throws Exception {
        // Create a user of every type and ensure saving works
        final Customer c = new Customer( "cus1", "p1" );
        final AbstractUser u = new AbstractUser();

        u.setUsername( "u1" );

        u.setPassword( "pass" );
        u.setRoleType( Role.CUSTOMER );
        System.out.println( TestUtils.asJsonString( u ) );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.findAll().size() );
        assertEquals( "u1", service.findByUsername( "u1" ).getUsername() );

    }

}
