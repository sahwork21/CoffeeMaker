package edu.ncsu.csc.CoffeeMaker.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Testing file for the MappingController. Makes sure that the proper html pages
 * are GET with the class
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
class TestMappingController {

    /** Controller for testing */

    // private MappingController mapper;

    /** Our mock model view controller */
    private MockMvc               mvc;

    /**
     * Spring's testing framework to handle web servers
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Check that the proper html pages are added by checking their titles
     *
     *
     * @throws Exception
     *             when you get an html page
     */
    @Test
    @Transactional
    public void testMappingController () throws Exception {
        // Test that index has the proper title

        final String recipe = mvc.perform( get( "/addRecipe/" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( recipe.contains( "<title>Add Recipe</title>" ) );

        final String delete = mvc.perform( get( "/deleteRecipe/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( delete.contains( "<title>Delete Recipe</title>" ) );

        final String inventory = mvc.perform( get( "/addInventory/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( inventory.contains( "<title>Add Inventory</title>" ) );

        final String ingredient = mvc.perform( get( "/addIngredient/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( ingredient.contains( "<title>Add Ingredient</title>" ) );

        final String makecoffee = mvc.perform( get( "/makecoffee/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( makecoffee.contains( "<title>View Recipes</title>" ) );

        final String editrecipe = mvc.perform( get( "/editrecipe/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( editrecipe.contains( "<title>Edit Recipe</title>" ) );

        // Test the user controllers
        final String signin = mvc.perform( get( "/signin" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( signin.contains( "<title>Login page" ) );

        final String signup = mvc.perform( get( "/signup" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( signup.contains( "<title>Signup page" ) );

        // Test the roles can access their pages
        final String barista = mvc.perform( get( "/barista" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( barista.contains( "<title>Barista Menu" ) );

        final String customer = mvc.perform( get( "/customer" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( customer.contains( "<title>Customer Menu" ) );

        final String manager = mvc.perform( get( "/manager" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( manager.contains( "<title>Manager Menu" ) );

        final String order = mvc.perform( get( "/customer/order" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( order.contains( "<title>Order Menu" ) );

        // Test the html pages would be able to get their javascripts
        final String signinjs = mvc.perform( get( "/signin.js" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( signinjs.contains( "SignInController" ) );

        final String signupjs = mvc.perform( get( "/signup.js" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( signupjs.contains( "SignUpController" ) );

        final String customerjs = mvc.perform( get( "/customer.js" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( customerjs.contains( "CustomerController" ) );

        final String orderjs = mvc.perform( get( "/customer/order.js" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( orderjs.contains( "OrderController" ) );

        final String baristajs = mvc.perform( get( "/barista.js" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( baristajs.contains( "BaristaController" ) );

        final String managerjs = mvc.perform( get( "/manager.js" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( managerjs.contains( "ManagerController" ) );

        final String managerbars = mvc.perform( get( "/manageBaristas" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( managerbars.contains( "Manage Barista Menu" ) );

        final String managerbarsjs = mvc.perform( get( "/manageBaristas.js" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( managerbarsjs.contains( "ManageBaristasController" ) );

    }

}
