package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Testing file for API that creates and object and verifies that it exists
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * Spring's testing framework to handle web servers
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Recipe service to get our database objects
     */
    @Autowired
    private RecipeService         recService;

    /**
     * Ingredient service to get our database objects
     */
    @Autowired
    private IngredientService     ingService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests that API calls work
     *
     * @throws Exception
     *             throws an exception when the API does not work
     */
    @Test
    @Transactional
    public void testAPI () throws Exception {
        // Grabbed the list of JSON object from the database as a String
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Mocha should not be present at the start
        if ( !recipe.contains( "Mocha" ) ) {

            // Add the Mocha recipe to the database
            final Recipe r = new Recipe();
            r.setName( "Mocha" );
            final Ingredient chocolate = new Ingredient( "Chocolate", 1, false );
            final Ingredient coffee = new Ingredient( "Coffee", 1, false );
            final Ingredient milk = new Ingredient( "Milk", 1, false );
            r.addIngredient( chocolate );
            r.addIngredient( coffee );
            r.addIngredient( milk );
            r.setPrice( 4 );

            // Post the recipe to the databases by changing the recipe object
            // into a JSON string
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

            // Make sure the ingredients got saved too
            final String response = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() )
                    .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

            assertTrue( response.contains( "Chocolate" ) );
            assertTrue( response.contains( "Coffee" ) );
            assertTrue( response.contains( "Milk" ) );

            // Get the recipes list again and make sure that the JSON file
            // contains the Mocha we just made and added
            final String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                    .andReturn().getResponse().getContentAsString();

            assertTrue( recipe2.contains( "Mocha" ) );

            // Create an inventory of 50 ingredients of supply each.
            final Inventory inventory = new Inventory();
            inventory.addIngredient( new Ingredient( "Chocolate", 50, false ) );
            inventory.addIngredient( new Ingredient( "Coffee", 50, false ) );
            inventory.addIngredient( new Ingredient( "Milk", 50, false ) );

            // Use the API to put to inventory. This will increment its current
            // inventory by the values of the passed inventory.
            mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

            // Order a Mocha now that the item exists and we can change the
            // inventory
            final String name = "Mocha";

            // Use the post operation to make coffee
            // This should also create a new action

            // Print out our inventory before hand so we know things changed
            mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                    .getResponse().getContentAsString();

            final String action = mvc
                    .perform( post( "/api/v1/makecoffee/" + name ).contentType( MediaType.APPLICATION_JSON )
                            .content( TestUtils.asJsonString( 15 ) ) )
                    .andExpect( status().isOk() ).andDo( print() ).andReturn().getResponse().getContentAsString();

            // Affirm that the action was not null
            assertTrue( action.contains( "success" ) && action.contains( "11" ) );

            // Also affirm that the inventory decreased
            mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                    .getResponse().getContentAsString();

            // If purchasing coffee worked properly then we will have seen the
            // inventory in the console decrease

            // Use the API to test deleting from the Recipes

            // Now try deleting a recipe that doesn't exist
            mvc.perform( delete( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON ) )
                    .andExpect( status().isNotFound() );
            // Now make sure Mocha is still appearing
            assertTrue( mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                    .andReturn().getResponse().getContentAsString().contains( "Mocha" ) );

            // Now delete the Mocha recipe
            mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON ) )
                    .andExpect( status().isOk() );

            // And make sure it is not in the string
            assertFalse( mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                    .andReturn().getResponse().getContentAsString().contains( "Mocha" ) );
            // TO ENSURE THIS WORKS LOOK AT THE LAST CONSOLE RESPONSE AND MAKE
            // SURE BODY IS AN EMPTY ARRAY
        }
        else {
            // If this is reached tests are not independent and need to reset
            // the database before each run
            fail( "Mocha should not be found in the database initially" );
        }
    }

    /**
     * Test that the APIRecipeController works with multiple Recipes and methods
     * are covered
     *
     * @throws Exception
     *             throws when the API does not work properly
     */
    @Test
    @Transactional
    public void testAPIMultipleRecipes () throws Exception {

        // Grabbed the list of JSON object from the database as a String

        // Mocha should not be present at the start

        // Add the Mocha recipe to the database
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        final Ingredient chocolate = new Ingredient( "Chocolate", 1, false );
        final Ingredient coffee = new Ingredient( "Coffee", 1, false );
        final Ingredient milk = new Ingredient( "Milk", 1, false );
        final Ingredient sugar = new Ingredient( "Sugar", 1, false );
        r.addIngredient( chocolate );
        r.addIngredient( coffee );
        r.addIngredient( milk );
        r.addIngredient( sugar );
        r.setPrice( 4 );

        // Post the recipe to the databases by changing the recipe object
        // into a JSON string
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        // Get the recipes list again and make sure that the JSON file
        // contains the Mocha we just made and added
        final String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( recipe2.contains( "Mocha" ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Cappucino" );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 5, false );
        final Ingredient coffee2 = new Ingredient( "Coffee", 5, false );
        final Ingredient milk2 = new Ingredient( "Milk", 5, false );
        final Ingredient sugar2 = new Ingredient( "Sugar", 5, false );
        r2.addIngredient( chocolate2 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.setPrice( 5 );

        // Now try adding another recipe
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        // Now make sure that all the recipes are appearing in the list and
        // that we can get individual recipes
        final String list = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( list.contains( "Mocha" ) );
        assertTrue( list.contains( "Cappucino" ) );

        // Now get individual recipes
        final String mocha = mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( mocha.contains( "Mocha" ) );
        assertFalse( mocha.contains( "Cappucino" ) );

        final String cappucino = mvc.perform( get( "/api/v1/recipes/Cappucino" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertFalse( cappucino.contains( "Mocha" ) );
        assertTrue( cappucino.contains( "Cappucino" ) );

        // Now add 2 more coffees and confirm the fourth can be added due
        // to size limit being gone
        final Recipe r3 = new Recipe();
        final Ingredient chocolate3 = new Ingredient( "Chocolate", 4, false );
        final Ingredient coffee3 = new Ingredient( "Coffee", 4, false );
        final Ingredient milk3 = new Ingredient( "Milk", 4, false );
        final Ingredient sugar3 = new Ingredient( "Sugar", 4, false );
        r3.setName( "Americano" );
        r3.addIngredient( chocolate3 );
        r3.addIngredient( coffee3 );
        r3.addIngredient( milk3 );
        r3.addIngredient( sugar3 );
        final Recipe r4 = new Recipe();
        r4.setName( "Espresso" );
        r4.addIngredient( chocolate );
        r4.addIngredient( coffee );
        r4.addIngredient( milk );
        r4.addIngredient( sugar );
        r4.setPrice( 1 );
        // This is okay
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        // This will cause an insufficient storage response
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        // Now check the JSON string for the contained coffee recipes
        final String recipes3 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( recipes3.contains( "Mocha" ) );
        assertTrue( recipes3.contains( "Cappucino" ) );
        assertTrue( recipes3.contains( "Americano" ) );
        assertFalse( recipes3.contains( "Espresso" ) );

        // Now check that we can find the recipes that were saved and do not
        // find Espresso
        mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/recipes/Cappucino" ) ).andDo( print() ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/recipes/Americano" ) ).andDo( print() ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/recipes/Espresso" ) ).andDo( print() ).andExpect( status().isNotFound() );

        // For extra coverage make sure that we cannot add duplicates or
        // edit non existent recipes
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isConflict() );

        // Now delete these recipes and make sure that they are deleted
        // Then add the Espresso and make sure it appears
        mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final String recipes4 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertFalse( recipes4.contains( "Mocha" ) );
        assertTrue( recipes4.contains( "Cappucino" ) );
        assertTrue( recipes4.contains( "Americano" ) );
        assertFalse( recipes4.contains( "Espresso" ) );

        // Now just delete everything and make sure it's gone
        mvc.perform( delete( "/api/v1/recipes/Cappucino" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( delete( "/api/v1/recipes/Americano" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final String recipes6 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertFalse( recipes6.contains( "Mocha" ) );
        assertFalse( recipes6.contains( "Cappucino" ) );
        assertFalse( recipes6.contains( "Americano" ) );
        assertFalse( recipes6.contains( "Espresso" ) );

    }

    /**
     * Tests that buying coffee works when multiple recipes are in the database
     * and alternate flows when a recipe may not exist or ingredients are not
     * there
     *
     * @author Sean Hinton (sahinto2)
     * @throws Exception
     *             throws when you use the API incorrectly
     */
    @Test
    @Transactional
    public void testAPICoffeeMultipleRecipes () throws Exception {
        // First add 3 recipes just like the test above
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        final Ingredient chocolate = new Ingredient( "Chocolate", 1, false );
        final Ingredient coffee = new Ingredient( "Coffee", 1, false );
        final Ingredient milk = new Ingredient( "Milk", 1, false );
        final Ingredient sugar = new Ingredient( "Sugar", 1, false );
        r.addIngredient( chocolate );
        r.addIngredient( coffee );
        r.addIngredient( milk );
        r.addIngredient( sugar );
        r.setPrice( 4 );

        final Recipe r2 = new Recipe();
        r2.setName( "Cappucino" );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 5, false );
        final Ingredient coffee2 = new Ingredient( "Coffee", 5, false );
        final Ingredient milk2 = new Ingredient( "Milk", 5, false );
        final Ingredient sugar2 = new Ingredient( "Sugar", 5, false );
        r2.addIngredient( chocolate2 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.setPrice( 5 );

        final Recipe r3 = new Recipe();
        r3.setName( "Americano" );
        final Ingredient chocolate3 = new Ingredient( "Chocolate", 4, false );
        final Ingredient coffee3 = new Ingredient( "Coffee", 4, false );
        final Ingredient milk3 = new Ingredient( "Milk", 4, false );
        final Ingredient sugar3 = new Ingredient( "Sugar", 4, false );
        r3.addIngredient( chocolate3 );
        r3.addIngredient( coffee3 );
        r3.addIngredient( milk3 );
        r3.addIngredient( sugar3 );
        r3.setPrice( 4 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        // Make sure the inventory is empty
        final String mt = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( mt.contains( "ingredients\":[]" ) );

        // Now make sure you can order nothing when the inventory is empty
        final String invalid1 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();

        // We paid enough but an error occurred because there were not enough
        // ingredients. This message is hidden in some JSON file
        assertTrue( invalid1.contains( "failed" ) && invalid1.contains( "Not enough inventory" ) );

        // Now add enough inventory to make Mocha only

        final Inventory inventory = new Inventory();
        final Ingredient chocolate4 = new Ingredient( "Chocolate", 3, false );
        final Ingredient coffee4 = new Ingredient( "Coffee", 3, false );
        final Ingredient milk4 = new Ingredient( "Milk", 3, false );
        final Ingredient sugar4 = new Ingredient( "Sugar", 3, false );
        inventory.addIngredient( chocolate4 );
        inventory.addIngredient( coffee4 );
        inventory.addIngredient( milk4 );
        inventory.addIngredient( sugar4 );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

        // Now try buying a Cappucino and ensure it failed.
        // Then buy a Mocha and ensure it works and ingredients decreases

        final String invalid2 = mvc
                .perform( post( "/api/v1/makecoffee/Cappucino" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid2.contains( "failed" ) && invalid2.contains( "Not enough inventory" ) );

        final String valid1 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // A success will return our change amount and success in a JSON file
        assertTrue( valid1.contains( "success" ) && valid1.contains( "\"message\":\"1\"" ) );

        // Now make sure the inventory amount decreased
        final String invent = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( invent.contains( "\"name\":\"Chocolate\",\"isInventory\":true,\"amount\":2,\"inventory\":true" ) );
        assertTrue( invent.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":2,\"inventory\":true" ) );
        assertTrue( invent.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":2,\"inventory\":true" ) );
        assertTrue( invent.contains( "\"name\":\"Sugar\",\"isInventory\":true,\"amount\":2,\"inventory\":true" ) );

        // Now try ordering things the incorrect way
        // Not enough money, invalid recipes, null recipes
        final String invalid4 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 0 ) ) )
                .andDo( print() ).andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid4.contains( "failed" ) && invalid4.contains( "Not enough money paid" ) );

        mvc.perform( post( "/api/v1/makecoffee/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 5 ) ) ).andDo( print() ).andExpect( status().isNotFound() )
                .andReturn().getResponse().getContentAsString();

        final String invalid6 = mvc
                .perform( post( "/api/v1/makecoffee/Expresso" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid6.contains( "failed" ) && invalid6.contains( "No recipe selected" ) );

        final String invalid7 = mvc
                .perform( post( "/api/v1/makecoffee/null" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid7.contains( "failed" ) && invalid7.contains( "No recipe selected" ) );

        // Now add enough inventory to buy all the recipes
        final Inventory inventory2 = new Inventory();
        final Ingredient chocolate5 = new Ingredient( "Chocolate", 38, false );
        final Ingredient coffee5 = new Ingredient( "Coffee", 38, false );
        final Ingredient milk5 = new Ingredient( "Milk", 38, false );
        final Ingredient sugar5 = new Ingredient( "Sugar", 38, false );
        inventory2.addIngredient( chocolate5 );
        inventory2.addIngredient( coffee5 );
        inventory2.addIngredient( milk5 );
        inventory2.addIngredient( sugar5 );

        // This should bump up the total to 40

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory2 ) ) ).andExpect( status().isOk() );

        // Then make sure we can buy all the drinks
        final String valid2 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 4 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( valid2.contains( "success" ) && valid2.contains( "\"message\":\"0\"" ) );

        final String invent2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue(
                invent2.contains( "\"name\":\"Chocolate\",\"isInventory\":true,\"amount\":39,\"inventory\":true" ) );
        assertTrue( invent2.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":39,\"inventory\":true" ) );
        assertTrue( invent2.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":39,\"inventory\":true" ) );
        assertTrue( invent2.contains( "\"name\":\"Sugar\",\"isInventory\":true,\"amount\":39,\"inventory\":true" ) );

        final String valid3 = mvc
                .perform( post( "/api/v1/makecoffee/Cappucino" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( valid3.contains( "success" ) && valid3.contains( "\"message\":\"0\"" ) );

        final String invent3 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue(
                invent3.contains( "\"name\":\"Chocolate\",\"isInventory\":true,\"amount\":34,\"inventory\":true" ) );
        assertTrue( invent3.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":34,\"inventory\":true" ) );
        assertTrue( invent3.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":34,\"inventory\":true" ) );
        assertTrue( invent3.contains( "\"name\":\"Sugar\",\"isInventory\":true,\"amount\":34,\"inventory\":true" ) );

        final String valid4 = mvc
                .perform( post( "/api/v1/makecoffee/Americano" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 4 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( valid4.contains( "success" ) && valid4.contains( "\"message\":\"0\"" ) );

        final String invent4 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue(
                invent4.contains( "\"name\":\"Chocolate\",\"isInventory\":true,\"amount\":30,\"inventory\":true" ) );
        assertTrue( invent4.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":30,\"inventory\":true" ) );
        assertTrue( invent4.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":30,\"inventory\":true" ) );
        assertTrue( invent4.contains( "\"name\":\"Sugar\",\"isInventory\":true,\"amount\":30,\"inventory\":true" ) );

        // Now delete a recipe and try buying it
        mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final String invalid8 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 4 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid8.contains( "failed" ) && invalid8.contains( "No recipe selected" ) );

        // Make sure you can still order from the other 2
        final String valid5 = mvc
                .perform( post( "/api/v1/makecoffee/Americano" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 4 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( valid5.contains( "success" ) && valid5.contains( "\"message\":\"0\"" ) );

        final String valid6 = mvc
                .perform( post( "/api/v1/makecoffee/Cappucino" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( valid6.contains( "success" ) && valid6.contains( "\"message\":\"0\"" ) );

        // Now delete everything and make sure you can't order anything
        mvc.perform( delete( "/api/v1/recipes/Cappucino" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( delete( "/api/v1/recipes/Americano" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final String invalid9 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 4 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid9.contains( "failed" ) && invalid9.contains( "No recipe selected" ) );

        final String invalid10 = mvc
                .perform( post( "/api/v1/makecoffee/Cappucino" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 5 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid10.contains( "failed" ) && invalid10.contains( "No recipe selected" ) );

        final String invalid11 = mvc
                .perform( post( "/api/v1/makecoffee/Americano" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 4 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( invalid11.contains( "failed" ) && invalid11.contains( "No recipe selected" ) );

    }

    /**
     * Test editing a recipe using API endpoint operations
     *
     * @throws Exception
     *             throws if you make an API error
     */
    @Test
    @Transactional
    public void testEditRecipe () throws Exception {
        Assertions.assertEquals( 0, recService.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Mocha" );
        r1.setPrice( 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3, false ) );
        r1.addIngredient( new Ingredient( "Milk", 1, false ) );
        r1.addIngredient( new Ingredient( "Sugar", 1, false ) );

        // Put this into the database
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        assertEquals( 1, recService.count() );

        // Create our inventory
        final Inventory inventory = new Inventory();
        final Ingredient chocolate4 = new Ingredient( "Chocolate", 50, false );
        final Ingredient coffee4 = new Ingredient( "Coffee", 50, false );
        final Ingredient milk4 = new Ingredient( "Milk", 50, false );
        final Ingredient sugar4 = new Ingredient( "Sugar", 50, false );
        inventory.addIngredient( chocolate4 );
        inventory.addIngredient( coffee4 );
        inventory.addIngredient( milk4 );
        inventory.addIngredient( sugar4 );

        assertEquals( 3, ingService.count() );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

        assertEquals( 7, ingService.count() );

        // Now buy from it then edit the price and buy again
        final String change1 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 55 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // We should get 5 in change and the amounts for coffee, milk, and sugar
        // will change
        assertTrue( change1.contains( "success" ) && change1.contains( "5" ) );

        final String invent1 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue(
                invent1.contains( "\"name\":\"Chocolate\",\"isInventory\":true,\"amount\":50,\"inventory\":true" ) );
        assertTrue( invent1.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":47,\"inventory\":true" ) );
        assertTrue( invent1.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":49,\"inventory\":true" ) );
        assertTrue( invent1.contains( "\"name\":\"Sugar\",\"isInventory\":true,\"amount\":49,\"inventory\":true" ) );

        // Now change up the price and ingredients of our Mocha recipe
        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 150 );
        r2.addIngredient( new Ingredient( "Coffee", 40, false ) );
        r2.addIngredient( new Ingredient( "Chocolate", 50, false ) );

        mvc.perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andDo( print() ).andExpect( status().isOk() );

        // Ingredients should go down but recipes stays at 1
        assertEquals( 1, recService.count() );
        assertEquals( 6, ingService.count() );

        // Now go buy again

        final String change2 = mvc
                .perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 200 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // We should get 5 in change and the amounts for coffee, milk, and sugar
        // will change
        assertTrue( change2.contains( "success" ) && change2.contains( "50" ) );

        final String invent2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( invent2.contains( "\"name\":\"Chocolate\",\"isInventory\":true,\"amount\":0,\"inventory\":true" ) );
        assertTrue( invent2.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":7,\"inventory\":true" ) );
        assertTrue( invent2.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":49,\"inventory\":true" ) );
        assertTrue( invent2.contains( "\"name\":\"Sugar\",\"isInventory\":true,\"amount\":49,\"inventory\":true" ) );

        // Now take the invalid flows

        // Mismatched name
        final Recipe r3 = new Recipe();
        r3.setName( "Cappucino" );
        r3.setPrice( 150 );
        r3.addIngredient( new Ingredient( "Coffee", 40, false ) );
        r3.addIngredient( new Ingredient( "Chocolate", 50, false ) );

        final String response1 = mvc
                .perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r3 ) ) )
                .andDo( print() ).andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        assertTrue( response1.contains(
                "Mocha could not be edited since the input recipe is invalid in price, mismatched names, or ingredient amounts" ) );

        // No ingredients
        final Recipe r4 = new Recipe();
        r4.setName( "Mocha" );
        r4.setPrice( 150 );
        r4.addIngredient( new Ingredient( "Coffee", 0, false ) );
        r4.addIngredient( new Ingredient( "Chocolate", 0, false ) );

        final String response2 = mvc
                .perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r4 ) ) )
                .andDo( print() ).andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        assertTrue( response2.contains(
                "Mocha could not be edited since the input recipe is invalid in price, mismatched names, or ingredient amounts" ) );

        // Negative price
        r3.setName( "Mocha" );
        r3.setPrice( -1 );

        final String response3 = mvc
                .perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r3 ) ) )
                .andDo( print() ).andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();

        assertTrue( response3.contains(
                "Mocha could not be edited since the input recipe is invalid in price, mismatched names, or ingredient amounts" ) );

        // Now get a not found error
        // Delete Mocha and try editing it
        mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertEquals( 0, recService.count() );
        assertEquals( 4, ingService.count() );
        final String response4 = mvc
                .perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r2 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertTrue( response4.contains( "Mocha does not exist" ) );

    }

}
