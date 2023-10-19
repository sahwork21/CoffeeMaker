package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Test class for the Recipe API
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        final Ingredient ingredient = new Ingredient( "Coffee", 10, false );
        r.addIngredient( ingredient );
        r.setName( "Mocha" );
        r.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient ingredient = new Ingredient( "Coffee", 10, false );
        recipe.addIngredient( ingredient );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();

        final Recipe r1 = createRecipe( name, 50, ingredients );
        final Ingredient ingredient = new Ingredient( "Coffee", 10, false );
        r1.addIngredient( ingredient );
        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, ingredients );
        final Ingredient ingredient2 = new Ingredient( "Coffee", 10, false );
        r2.addIngredient( ingredient2 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Coffee", 3, false ) );
        ingredients.add( new Ingredient( "Milk", 1, false ) );
        ingredients.add( new Ingredient( "Sugar", 1, false ) );

        final Recipe r1 = createRecipe( "Coffee", 50, ingredients );
        service.save( r1 );
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add( new Ingredient( "Coffee", 3, false ) );
        ingredients2.add( new Ingredient( "Milk", 1, false ) );
        ingredients2.add( new Ingredient( "Sugar", 1, false ) );
        final Recipe r2 = createRecipe( "Mocha", 50, ingredients2 );

        // Now try adding in a Recipe without ingredients which is illegal
        final Recipe r5 = new Recipe();
        r5.setPrice( 15 );
        r5.setName( "Illegal" );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r5 ) ) ).andExpect( status().isBadRequest() );

        final List<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add( new Ingredient( "Coffee", 3, false ) );
        ingredients3.add( new Ingredient( "Milk", 1, false ) );
        ingredients3.add( new Ingredient( "Sugar", 1, false ) );
        service.save( r2 );

        final Recipe r3 = createRecipe( "Latte", 60, ingredients3 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final List<Ingredient> ingredients4 = new ArrayList<Ingredient>();
        ingredients4.add( new Ingredient( "Coffee", 3, false ) );
        ingredients4.add( new Ingredient( "Milk", 1, false ) );
        ingredients4.add( new Ingredient( "Sugar", 1, false ) );
        final Recipe r4 = createRecipe( "Hot Chocolate", 75, ingredients4 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(),

                "Creating a fourth recipe should not get saved since the limiter is not gone" );

        // Now try editing some of these recipes
        // Incorrectly edit with a negative price, mismatch name, and no
        // ingredients.
        final List<Ingredient> ing5 = new ArrayList<Ingredient>();
        ing5.add( new Ingredient( "Coffee", 3, false ) );
        final Recipe l1 = createRecipe( "Coffee", -1, ing5 );
        final Recipe l2 = createRecipe( "NotCoff", 10, ing5 );
        final Recipe l3 = new Recipe();
        l3.setName( "Coffee" );
        l3.setPrice( 10 );

        // Every put should not work
        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( l1 ) ) ).andExpect( status().isBadRequest() );
        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( l2 ) ) ).andExpect( status().isBadRequest() );
        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( l3 ) ) ).andExpect( status().isBadRequest() );

        // Get a not found
        mvc.perform( put( "/api/v1/recipes/nothing" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( l3 ) ) ).andExpect( status().isNotFound() );

        // Now do it correctly
        final Recipe edit = createRecipe( "Coffee", 10, ing5 );
        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( edit ) ) ).andExpect( status().isOk() );

    }

    private Recipe createRecipe ( final String name, final Integer price, final List<Ingredient> ingredients ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        for ( final Ingredient i : ingredients ) {
            recipe.addIngredient( i );
        }

        return recipe;
    }

}
