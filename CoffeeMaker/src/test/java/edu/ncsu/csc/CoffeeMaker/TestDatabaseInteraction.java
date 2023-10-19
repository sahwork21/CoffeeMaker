package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Test class for the Database and Java object interaction Uses the Hibernate
 * API to make calls between database and Java files
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {
    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * RecipeService API caller for testing
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Test the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.addIngredient( new Ingredient( "Coffee", 2, false ) );
        r.addIngredient( new Ingredient( "Milk", 2, false ) );
        r.addIngredient( new Ingredient( "Sugar", 2, false ) );
        r.addIngredient( new Ingredient( "Chocolate", 2, false ) );
        r.setPrice( 3 );
        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getIngredientByName( "Coffee" ), dbRecipe.getIngredientByName( "Coffee" ) );
        assertEquals( r.getIngredientByName( "Sugar" ), dbRecipe.getIngredientByName( "Sugar" ) );
        assertEquals( r.getIngredientByName( "Milk" ), dbRecipe.getIngredientByName( "Milk" ) );
        assertEquals( r.getIngredientByName( "Chocolate" ), dbRecipe.getIngredientByName( "Chocolate" ) );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        final Recipe r1 = recipeService.findByName( "Mocha" );

        // Assert that the recipes are there after completing
        assertNotNull( r1 );

        // Make sure all the fields are there
        assertEquals( r1.getName(), dbRecipe.getName() );
        assertEquals( r1.getIngredientByName( "Coffee" ), dbRecipe.getIngredientByName( "Coffee" ) );
        assertEquals( r1.getIngredientByName( "Sugar" ), dbRecipe.getIngredientByName( "Sugar" ) );
        assertEquals( r1.getIngredientByName( "Milk" ), dbRecipe.getIngredientByName( "Milk" ) );
        assertEquals( r1.getIngredientByName( "Chocolate" ), dbRecipe.getIngredientByName( "Chocolate" ) );

        // Find a null recipe
        final Recipe rnull = recipeService.findByName( "Coffee" );

        assertNull( rnull );

        // Make another recipe and find it
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.addIngredient( new Ingredient( "Coffee", 2, false ) );
        r2.addIngredient( new Ingredient( "Milk", 1, false ) );
        r2.addIngredient( new Ingredient( "Sugar", 1, false ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1, false ) );
        r2.setPrice( 5 );

        recipeService.save( r2 );

        final List<Recipe> dbRecipes2 = recipeService.findAll();

        // Assert that the sizes have changed
        assertEquals( 2, dbRecipes2.size() );

        final Recipe r3 = dbRecipes2.get( 0 );
        final Recipe r4 = dbRecipes2.get( 1 );

        assertEquals( r2.getName(), r4.getName() );
        assertEquals( r2.getIngredientByName( "Coffee" ), r4.getIngredientByName( "Coffee" ) );
        assertEquals( r2.getIngredientByName( "Sugar" ), r4.getIngredientByName( "Sugar" ) );
        assertEquals( r2.getIngredientByName( "Milk" ), r4.getIngredientByName( "Milk" ) );
        assertEquals( r2.getIngredientByName( "Chocolate" ), r4.getIngredientByName( "Chocolate" ) );

        assertEquals( r.getName(), r3.getName() );
        assertEquals( r.getIngredientByName( "Coffee" ), r3.getIngredientByName( "Coffee" ) );
        assertEquals( r.getIngredientByName( "Sugar" ), r3.getIngredientByName( "Sugar" ) );
        assertEquals( r.getIngredientByName( "Milk" ), r3.getIngredientByName( "Milk" ) );
        assertEquals( r.getIngredientByName( "Chocolate" ), r3.getIngredientByName( "Chocolate" ) );
        assertEquals( r.getPrice(), r3.getPrice() );

        // Edit a recipe in memory, and save it to the database.
        // Ensure a recipe is UPDATED instead of created.
        r2.setPrice( 15 );
        r2.setIngredient( new Ingredient( "Sugar", 2, false ) );
        recipeService.save( r2 );

        final Recipe recipeWithEdit = recipeService.findAll().get( 1 );

        assertEquals( 2, recipeService.count() );

        // Verify fields are updated
        assertEquals( 15, (int) recipeWithEdit.getPrice() );
        assertEquals( 2, recipeWithEdit.getIngredientByName( "Sugar" ).getAmount() );

        // Verify unchanged fields remain
        assertEquals( r2.getName(), recipeWithEdit.getName() );
        assertEquals( r2.getIngredientByName( "Coffee" ), recipeWithEdit.getIngredientByName( "Coffee" ) );
        assertEquals( r2.getIngredientByName( "Milk" ), recipeWithEdit.getIngredientByName( "Milk" ) );
        assertEquals( r2.getIngredientByName( "Chocolate" ), recipeWithEdit.getIngredientByName( "Chocolate" ) );

        /*
         * Now test deleting recipes from the database
         */
        // Try deleting a new Recipe that does not exist
        // Name is unset along with prices
        recipeService.delete( new Recipe() );

        // Make sure the count did not change and mocha and coffee are still
        // there
        assertEquals( 2, recipeService.count() );

        final List<Recipe> rList = recipeService.findAll();

        assertEquals( r, rList.get( 0 ) );
        assertEquals( r2, rList.get( 1 ) );

        // Now try deleting Mocha from our database

        recipeService.delete( r );
        assertEquals( 1, recipeService.count() );

        final List<Recipe> r2List = recipeService.findAll();
        assertEquals( r2, r2List.get( 0 ) );

        // Now try deleting it again and ensure nothing changes
        recipeService.delete( r );
        assertEquals( 1, recipeService.count() );

        assertEquals( r2, recipeService.findAll().get( 0 ) );

        // Now add Mocha back then delete everything
        recipeService.save( r );

        recipeService.deleteAll();

        // Make sure that the database is really empty
        assertEquals( 0, recipeService.count() );
        assertThrows( IndexOutOfBoundsException.class, () -> recipeService.findAll().get( 0 ) );

    }

    /**
     * Tests finding recipes through the Service object by ID Makes sure the
     * CRUD operations still work with IDs
     *
     * @author Sean Hinton (sahinto2)
     */
    @Test
    @Transactional
    public void testRecipesID () {

        // Make some recipes that the Hibernate API will set
        final Recipe r1 = new Recipe();
        r1.setName( "Mocha" );
        r1.addIngredient( new Ingredient( "Coffee", 5, false ) );
        r1.setPrice( 4 );

        final Recipe r2 = new Recipe();
        r2.setName( "Cappucino" );

        r2.setPrice( 4 );
        r2.addIngredient( new Ingredient( "Coffee", 5, false ) );

        final Recipe r3 = new Recipe();
        r3.setName( "Espresso" );
        r3.addIngredient( new Ingredient( "Coffee", 5, false ) );
        r3.setPrice( 4 );

        // Make sure the ids are null to start
        assertNull( r1.getId() );
        assertNull( r2.getId() );
        assertNull( r3.getId() );

        // Now save it
        final List<Recipe> recipesToAdd = new ArrayList<Recipe>();
        recipesToAdd.add( r1 );
        recipesToAdd.add( r2 );
        recipesToAdd.add( r3 );

        recipeService.saveAll( recipesToAdd );
        // Now get them back and save the ids

        final List<Recipe> recipes = recipeService.findAll();

        final long r1id = recipes.get( 0 ).getId();
        final long r2id = recipes.get( 1 ).getId();
        final long r3id = recipes.get( 2 ).getId();

        // Test that r3 is there because we will delete it soon
        assertEquals( r3, recipeService.findById( r3id ) );
        assertTrue( recipeService.existsById( r3id ) );

        // Delete r3 just as a test
        recipeService.delete( r3 );

        // Now try finding by id
        assertEquals( r1, recipeService.findById( r1id ) );
        assertEquals( r2, recipeService.findById( r2id ) );
        assertNull( recipeService.findById( r3id ) );

        // Now try invalid operations by finding non stored recipes
        assertNull( recipeService.findById( null ) );
        assertNull( recipeService.findById( (long) -1 ) );

        // Now make sure exists by works as intended
        assertTrue( recipeService.existsById( r1id ) );
        assertTrue( recipeService.existsById( r2id ) );
        assertFalse( recipeService.existsById( r3id ) );
        assertFalse( recipeService.existsById( (long) -1 ) );

        // Now delete everything and make sure none of this is found
        recipeService.deleteAll();
        assertFalse( recipeService.existsById( r1id ) );
        assertFalse( recipeService.existsById( r2id ) );
        assertFalse( recipeService.existsById( r3id ) );
        assertFalse( recipeService.existsById( (long) -1 ) );

        assertNull( recipeService.findById( r1id ) );
        assertNull( recipeService.findById( r2id ) );
        assertNull( recipeService.findById( r3id ) );

    }
}
