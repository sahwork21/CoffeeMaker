package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;
import edu.ncsu.csc.CoffeeMaker.services.CustomerRequestService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
class APICustomerRequestTest {

    @Autowired
    private MockMvc                mvc;

    @Autowired
    private WebApplicationContext  context;

    @Autowired
    private CustomerService        cusService;

    @Autowired
    private CustomerRequestService orderService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        cusService.deleteAll();
        orderService.deleteAll();

    }

    /**
     * Test the get by status api call
     *
     * @throws Exception
     *             if you mess a call up
     */
    @Transactional
    @Test
    public void testGetStatus () throws Exception {

        // Create some orders and test that we can get them
        final Customer c = new Customer( "cus1", "p1" );
        cusService.save( c );

        // Save an order
        final CustomerRequest o = new CustomerRequest();
        o.setCustomer( c );
        o.setRecipe( "caf" );
        o.setStatus( OrderState.UNFULFILLED );

        orderService.save( o );

        // Assert things are there
        assertEquals( 1, orderService.findByStatus( OrderState.UNFULFILLED ).size() );

        // Now make the API call and get the returned list
        final String os = mvc.perform( get( "/api/v1/orders/unfulfilled" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();
        assertTrue( os.contains( "UNFULFILLED" ) );

    }

    /**
     * Test that we can move an order from state to state
     *
     * @throws Exception
     *             if something goes wrong
     */
    @Transactional
    @Test
    public void testStateChanger () throws Exception {
        // Create some orders and test that we can get them
        final Customer c = new Customer( "cus1", "p1" );
        cusService.save( c );

        // Save an order
        final CustomerRequest o = new CustomerRequest();
        o.setCustomer( c );
        o.setRecipe( "caf" );
        o.setStatus( OrderState.UNFULFILLED );

        orderService.save( o );

        assertEquals( 1, orderService.findByStatus( OrderState.UNFULFILLED ).size() );

        // Find the order's id

        // Fulfill this order
        mvc.perform( put( "/api/v1/orders/fulfill" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o.getId() ) ) ).andExpect( status().isOk() );

        // Make sure the order moved state to state
        assertEquals( 0, orderService.findByStatus( OrderState.UNFULFILLED ).size() );
        assertEquals( 1, orderService.findByStatus( OrderState.READY_TO_PICKUP ).size() );

    }

}
