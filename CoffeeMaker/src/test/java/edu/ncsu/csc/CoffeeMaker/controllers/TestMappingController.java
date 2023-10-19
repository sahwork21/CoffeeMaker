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

        final String index = mvc.perform( get( "/index/" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( index.contains( "<title>Welcome to the CSC326 CoffeeMaker</title>" ) );

        final String recipe = mvc.perform( get( "/recipe/" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( recipe.contains( "<title>Add a Recipe</title>" ) );

        final String delete = mvc.perform( get( "/deleterecipe/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( delete.contains( "<title>Delete Recipes</title>" ) );

        final String inventory = mvc.perform( get( "/inventory/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( inventory.contains( "<title>Inventory</title>" ) );

        final String ingredient = mvc.perform( get( "/ingredient/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( ingredient.contains( "<title>Ingredient</title>" ) );

        final String makecoffee = mvc.perform( get( "/makecoffee/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( makecoffee.contains( "<title>View Recipes</title>" ) );


        final String editrecipe = mvc.perform( get( "/editrecipe/" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( editrecipe.contains( "<title>Edit Recipes</title>" ) );

    }

}