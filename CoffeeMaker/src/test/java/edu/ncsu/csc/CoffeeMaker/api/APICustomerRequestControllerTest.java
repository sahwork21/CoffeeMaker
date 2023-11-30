package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;
import edu.ncsu.csc.CoffeeMaker.services.CustomerRequestService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import net.minidev.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APICustomerRequestControllerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc                mvc;

    @Autowired
    private WebApplicationContext  context;

    @Autowired
    private CustomerRequestService customerRequestService;

    @Autowired
    private RecipeService          recipeService;

    @Autowired
    private CustomerService        customerService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        customerRequestService.deleteAll();
        recipeService.deleteAll();
        customerService.deleteAll();
    }

    @Test
    @Transactional
    public void testListUnfulfilledOrders () throws Exception {
        final MvcResult result = mvc.perform( get( "/api/v1/orders/unfulfilled" ) ).andExpect( status().isOk() )
                .andReturn();

        final String responseString = result.getResponse().getContentAsString();
        assertNotNull( responseString );
    }

    @Test
    @Transactional
    public void testListOrders () throws Exception {
        mvc.perform( get( "/api/v1/orders" ) ).andExpect( status().isOk() )
                .andExpect( result -> assertNotNull( result.getResponse().getContentAsString() ) );
    }

    @Test
    @Transactional
    public void testGetOrdersHistory () throws Exception {
        mvc.perform( get( "/api/v1/orders/history" ) ).andExpect( status().isOk() ).andExpect( result -> {
            final String content = result.getResponse().getContentAsString();
            assertNotNull( content );

        } );
    }

    @Test
    @Transactional
    public void testDeleteOrderHistory () throws Exception {
        mvc.perform( delete( "/api/v1/orders/history" ) ).andExpect( status().isOk() ).andExpect( result -> assertTrue(
                result.getResponse().getContentAsString().contains( "Successfully cleared order history" ) ) );

    }

    // @Test
    // @Transactional
    // public void testCreateOrderErrorCustomer () throws Exception {
    // // Create and save a customer
    // final Customer customer = new Customer( "testCustomer", "password123" );
    // customerService.save( customer );
    //
    // // Create and save a recipe
    // final Recipe recipe = new Recipe();
    // recipe.setName( "testRecipe" );
    // // Set other necessary fields for the recipe
    // recipeService.save( recipe );
    //
    // // Create a CustomerRequest object
    // final CustomerRequest order = new CustomerRequest();
    // order.setCustomer( customer );
    // order.setRecipe( recipe.getName() );
    // order.setStatus( OrderState.UNFULFILLED ); // Set the initial state
    //
    // final String orderJson = "{" + "\"customer\": {\"id\": " +
    // customer.getId() + "}," + "\"recipe\": \""
    // + recipe.getName() + "\"," + "\"status\": \"" +
    // OrderState.UNFULFILLED.name() + "\"" + "}";
    //
    // // Perform POST request to create the order
    // mvc.perform( post( "/api/v1/orders" ).contentType(
    // MediaType.APPLICATION_JSON ).content( orderJson ) )
    // .andExpect( status().is4xxClientError() );
    //
    // }

    @Test
    @Transactional
    public void testCreateOrderErrorCustomer () throws Exception {
        final Customer c = new Customer( "cus1", "p1" );
        customerService.save( c );

        final Recipe recipe = new Recipe();
        recipe.setPrice( 100 );
        recipe.setName( "testRecipe" );
        recipeService.save( recipe );

        final CustomerRequest req = new CustomerRequest();
        req.setCustomer( c );
        req.setRecipe( recipe.getName() );
        req.setStatus( OrderState.UNFULFILLED );
        System.out.println( getOrderRequestJSON() );

        mvc.perform(
                post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON ).content( getOrderRequestJSON() ) )
                .andExpect( status().isOk() );
    }

    private String getOrderRequestJSON () {
        final JSONObject orderRequest = new JSONObject();
        orderRequest.put( "username", "cus1" );
        orderRequest.put( "recipeName", "testRecipe" );
        orderRequest.put( "payment", 115 );

        return orderRequest.toJSONString();

    }

    @Test
    @Transactional
    public void testFullFillOrder () throws Exception {
        final Customer c = new Customer( "cus2", "p2" );
        customerService.save( c );

        final Recipe recipe = new Recipe();
        recipe.setPrice( 100 );
        recipe.setName( "testRecipe" );
        recipeService.save( recipe );

        final CustomerRequest req = new CustomerRequest();
        req.setCustomer( c );
        req.setRecipe( recipe.getName() );
        req.setStatus( OrderState.UNFULFILLED );

        customerRequestService.save( req );

        mvc.perform( put( "/api/v1/orders/fulfill" ).contentType( MediaType.APPLICATION_JSON )
                .content( req.getId().toString() ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testPickUpOrder () throws Exception {
        final Customer c = new Customer( "cus2", "p2" );
        customerService.save( c );

        final Recipe recipe = new Recipe();
        recipe.setPrice( 100 );
        recipe.setName( "testRecipe" );
        recipeService.save( recipe );

        final CustomerRequest req = new CustomerRequest();
        req.setCustomer( c );
        req.setRecipe( recipe.getName() );
        req.setStatus( OrderState.READY_TO_PICKUP );

        customerRequestService.save( req );

        mvc.perform( put( "/api/v1/orders/pickup" ).contentType( MediaType.APPLICATION_JSON )
                .content( req.getId().toString() ) ).andExpect( status().isOk() );
    }

    @Test
    @Transactional
    public void testGetCustomersOrder () throws Exception {
        final Customer c = new Customer( "cus2", "p2" );
        customerService.save( c );

        final Recipe recipe = new Recipe();
        recipe.setPrice( 100 );
        recipe.setName( "testRecipe" );
        recipeService.save( recipe );

        final CustomerRequest req = new CustomerRequest();
        req.setCustomer( c );
        req.setRecipe( recipe.getName() );
        req.setStatus( OrderState.READY_TO_PICKUP );

        customerRequestService.save( req );

        mvc.perform( get( "/api/v1/orders/" + c.getUsername() ).contentType( MediaType.APPLICATION_JSON )
                .content( c.getUsername() ) ).andExpect( status().isOk() );
    }

}
