package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    /**
     * recipe service object that saves to database
     */
    @Autowired
    private RecipeService     service;

    /**
     * Ingredient service object that checks the database
     */
    @Autowired
    private IngredientService iservice;

    /**
     * Clears tables before every test
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests checkRecipe, and that true is returned only when all values are
     * zero.
     */
    @Test
    @Transactional
    public void testCheckRecipe () {
        // Check case of all 0
        final Recipe recipe = new Recipe();
        final Ingredient chocolate = new Ingredient( "Chocolate", 0, false );
        final Ingredient coffee = new Ingredient( "Coffee", 0, false );
        final Ingredient milk = new Ingredient( "Milk", 0, false );
        final Ingredient sugar = new Ingredient( "Sugar", 0, false );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( coffee );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );

        assertTrue( recipe.checkRecipe() );
        // Check case of one non-zero
        chocolate.setAmount( 1 );
        recipe.setIngredient( chocolate );
        assertFalse( recipe.checkRecipe() );

        chocolate.setAmount( 0 );
        recipe.setIngredient( chocolate );

        milk.setAmount( 1 );
        recipe.setIngredient( milk );
        assertFalse( recipe.checkRecipe() );

        milk.setAmount( 0 );
        recipe.setIngredient( milk );
        sugar.setAmount( 1 );

        recipe.setIngredient( sugar );
        assertFalse( recipe.checkRecipe() );

        // Check case of all non-zero
        coffee.setAmount( 1 );
        chocolate.setAmount( 1 );
        milk.setAmount( 1 );
        sugar.setAmount( 5 );

        recipe.setIngredient( coffee );
        recipe.setIngredient( chocolate );
        recipe.setIngredient( milk );
        recipe.setIngredient( sugar );
        assertFalse( recipe.checkRecipe() );

    }

    /**
     * Tests updateRecipe, ensuring all fields are correctly set with example
     * recipe.
     */
    /***
     * @Test
     * @Transactional public void testUpdateRecipe () { final Recipe
     *                recipeTemplate = new Recipe(); final Ingredient chocolate
     *                = new Ingredient( "Chocolate", 10, false ); final
     *                Ingredient coffee = new Ingredient( "Coffee", 5, false );
     *                final Ingredient milk = new Ingredient( "Milk", 7, false
     *                ); final Ingredient sugar = new Ingredient( "Sugar", 0,
     *                false ); recipeTemplate.addIngredient( chocolate );
     *                recipeTemplate.addIngredient( coffee );
     *                recipeTemplate.addIngredient( milk );
     *                recipeTemplate.addIngredient( sugar );
     *
     *                final Recipe recipe1 = new Recipe();
     *                recipe1.addIngredient(milk); sugar.setAmount( 5 );
     *                recipe1.addIngredient( sugar ); recipe1.setPrice( 50 );
     *                recipe1.updateRecipe( recipeTemplate );
     *
     *                assertEquals( 10, (int) recipe1.getChocolate() );
     *                assertEquals( 5, (int) recipe1.getCoffee() );
     *                assertEquals( 7, (int) recipe1.getMilk() ); assertEquals(
     *                0, (int) recipe1.getSugar() );
     *
     *                }
     */

    /**
     * Tests equals, and that all the expected fields are used when comparing
     * recipes
     */
    @Test
    @Transactional
    public void testRecipeEquals () {
        final Recipe recipe = new Recipe();
        final Ingredient chocolate = new Ingredient( "Chocolate", 0, false );
        final Ingredient coffee = new Ingredient( "Coffee", 0, false );
        final Ingredient milk = new Ingredient( "Milk", 0, false );
        final Ingredient sugar = new Ingredient( "Sugar", 0, false );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( coffee );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );

        assertTrue( recipe.equals( recipe ) );
        assertFalse( recipe.equals( null ) );
        assertFalse( recipe.equals( new Object() ) );

        final Recipe recipe2 = new Recipe();
        recipe2.addIngredient( chocolate );
        recipe2.addIngredient( coffee );
        recipe2.addIngredient( milk );
        recipe2.addIngredient( sugar );

        assertTrue( recipe.equals( recipe2 ) );
        assertTrue( recipe2.equals( recipe ) );

        recipe2.setName( "ThisRecipeIsDifferent" );
        recipe2.setPrice( 15 );
        assertFalse( recipe.equals( recipe2 ) );
    }

    /**
     * Tests toString to check that the correct string is returned. Null if name
     * is null, String otherwise.
     */
    @Test
    @Transactional
    public void testRecipeToString () {
        final Recipe recipe = new Recipe();
        System.out.println( recipe.getName() );
        assertTrue( recipe.toString().isEmpty() );

        recipe.setName( "ARecipe" );
        assertEquals( "ARecipe", recipe.getName() );
    }

    @Test
    @Transactional
    public void testHashcode () {
        final Recipe recipe = new Recipe();
        recipe.setName( "ARecipe" );
        assertNotNull( recipe.hashCode() );

        final Recipe differentRecipe = new Recipe();
        differentRecipe.setName( "UsedInHash" );

        assertNotEquals( recipe.hashCode(), differentRecipe.hashCode() );
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 0, false );
        final Ingredient coffee = new Ingredient( "Coffee", 1, false );
        final Ingredient milk = new Ingredient( "Milk", 0, false );
        final Ingredient sugar = new Ingredient( "Sugar", 0, false );
        r1.addIngredient( chocolate );
        r1.addIngredient( coffee );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 1, false );
        final Ingredient coffee2 = new Ingredient( "Coffee", 1, false );
        final Ingredient milk2 = new Ingredient( "Milk", 1, false );
        final Ingredient sugar2 = new Ingredient( "Sugar", 1, false );
        r2.setPrice( 1 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.addIngredient( chocolate2 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match" + " the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 0, false );
        final Ingredient coffee = new Ingredient( "Coffee", 1, false );
        final Ingredient milk = new Ingredient( "Milk", 0, false );
        final Ingredient sugar = new Ingredient( "Sugar", 0, false );
        r1.addIngredient( chocolate );

        r1.addIngredient( milk );
        r1.addIngredient( sugar );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 1, false );
        final Ingredient milk2 = new Ingredient( "Milk", 1, false );
        final Ingredient sugar2 = new Ingredient( "Sugar", 1, false );
        r2.setPrice( 1 );
        r2.addIngredient( chocolate2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );

        // final List<Recipe> recipes = List.of( r1, r2 );

        try {
            final Ingredient coffee2 = new Ingredient( "Coffee", -12, false );
            r2.addIngredient( coffee2 );
            Assertions.assertEquals( 0, service.count(), "A recipe was able to be made with negative ingredients" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof IllegalArgumentException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one " + "recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in " + "the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes " + "in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should " + "remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no " + "Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        assertEquals( 4, iservice.count() );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, retrieved.getIngredientByName( "Coffee" ).getAmount() );
        Assertions.assertEquals( 1, retrieved.getIngredientByName( "Milk" ).getAmount() );
        Assertions.assertEquals( 1, retrieved.getIngredientByName( "Sugar" ).getAmount() );
        Assertions.assertEquals( 0, retrieved.getIngredientByName( "Chocolate" ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't " + "duplicate it" );
        assertEquals( 4, iservice.count() );

        // Now change up the ingredients and make sure they get changed and the
        // old ones are dropped from the table
        final Recipe r2 = new Recipe();

        r2.setPrice( 15 );
        r2.setName( "Coffee" );
        r2.addIngredient( new Ingredient( "Coffee", 50, false ) );
        r2.addIngredient( new Ingredient( "Syrup", 10, false ) );
        r2.addIngredient( new Ingredient( "Milk", 5, true ) );
        r2.addIngredient( new Ingredient( "Cinnamon", 2, false ) );
        r2.addIngredient( new Ingredient( "Pumpkin Spice", 15, false ) );

        // Save the ingredients we need to remove since they are old instances
        final List<Ingredient> deletes = r1.getIngredients();

        // Now edit r1 then save it
        r1.editRecipe( r2 );
        service.save( r1 );

        // Delete all the ingredients in our list by id since ids are unique
        for ( final Ingredient i : deletes ) {
            iservice.delete( i );
        }

        // Make sure only 1 recipe is there and 5 ingredients are there
        assertEquals( 1, service.count() );
        assertEquals( 5, iservice.count() );

        // Now make sure the new Ingredient are all there
        final Recipe newRecipe = service.findByName( "Coffee" );
        assertEquals( 15, newRecipe.getPrice() );
        assertEquals( 5, newRecipe.getIngredients().size() );
        assertEquals( 50, newRecipe.getIngredientByName( "Coffee" ).getAmount() );
        assertEquals( 10, newRecipe.getIngredientByName( "Syrup" ).getAmount() );
        assertEquals( 5, newRecipe.getIngredientByName( "Milk" ).getAmount() );
        assertEquals( 2, newRecipe.getIngredientByName( "Cinnamon" ).getAmount() );
        assertEquals( 15, newRecipe.getIngredientByName( "Pumpkin Spice" ).getAmount() );

        assertFalse( newRecipe.getIngredientByName( "Coffee" ).getIsInventory() );
        assertFalse( newRecipe.getIngredientByName( "Syrup" ).getIsInventory() );
        assertFalse( newRecipe.getIngredientByName( "Milk" ).getIsInventory() );
        assertFalse( newRecipe.getIngredientByName( "Cinnamon" ).getIsInventory() );
        assertFalse( newRecipe.getIngredientByName( "Pumpkin Spice" ).getIsInventory() );

        // Make sure all our ingredients are in the repo and the deleted ones
        // are not
        for ( final Ingredient i : newRecipe.getIngredients() ) {
            assertNotNull( iservice.findById( i.getId() ) );
            assertEquals( i.getAmount(), iservice.findById( i.getId() ).getAmount() );
            assertFalse( i.getIsInventory() );
        }

        for ( final Ingredient i : deletes ) {
            assertNull( iservice.findById( i.getId() ) );

        }

    }

    @Test
    public void testEditRecipe2 () {
        final Recipe r = createRecipe( "Mocha", 50, 3, 1, 1, 1 );

        final Recipe r2 = new Recipe();
        r2.addIngredient( new Ingredient( "Coffee", 5, false ) );
        r2.addIngredient( new Ingredient( "Milk", 4, false ) );
        r2.addIngredient( new Ingredient( "Pumpkin Spice", 3, false ) );
        r2.addIngredient( new Ingredient( "Syrup", 2, false ) );
        r2.addIngredient( new Ingredient( "Water", 1, false ) );
        r2.setName( "Mocha" );
        r2.setPrice( 15 );

        // Now edit the recipe and make sure it gets saved
        r.editRecipe( r2 );

        assertEquals( r.getPrice(), r2.getPrice() );

        for ( int i = 0; i < r.getIngredients().size(); i++ ) {
            assertEquals( r.getIngredients().get( i ).getAmount(), r2.getIngredients().get( i ).getAmount() );
            assertEquals( r.getIngredients().get( i ).getName(), r2.getIngredients().get( i ).getName() );
        }

        // Assert the exceptions work
        // Edit a recipe with a mismatched name
        final Recipe r3 = new Recipe();

        r3.addIngredient( new Ingredient( "Coffee", 5, false ) );
        r3.addIngredient( new Ingredient( "Milk", 4, false ) );
        r3.addIngredient( new Ingredient( "Pumpkin Spice", 3, false ) );
        r3.addIngredient( new Ingredient( "Syrup", 2, false ) );
        r3.addIngredient( new Ingredient( "Water", 1, false ) );
        r3.setName( "Mismatch name" );
        r3.setPrice( 15 );

        final Exception e1 = assertThrows( IllegalArgumentException.class, () -> r.editRecipe( r3 ) );
        assertEquals( "Names do not match so we cannot edit the recipe", e1.getMessage() );

        // Edit a recipe with a negative price amount
        r3.setName( "Mocha" );
        r3.setPrice( -1 );

        final Exception e2 = assertThrows( IllegalArgumentException.class, () -> r.editRecipe( r3 ) );
        assertEquals( "Cannot edit recipe since input recipe has invalid price", e2.getMessage() );

        // Edit a recipe with a recipe that is empty
        final List<Ingredient> ing = new ArrayList<Ingredient>();
        ing.add( new Ingredient( "Coffee", 0, false ) );
        ing.add( new Ingredient( "Milk", 0, false ) );
        ing.add( new Ingredient( "Sugar", 0, false ) );

        r3.setIngredients( ing );
        assertEquals( 3, r3.getIngredients().size() );

        final Exception e3 = assertThrows( IllegalArgumentException.class, () -> r.editRecipe( r3 ) );
        assertEquals( "Input recipe does not have at least one recipe with an amount greater than zero",
                e3.getMessage() );
    }

    /**
     * Create a recipe to test
     *
     * @param name
     *            to set
     * @param price
     *            to set
     * @param coffee
     *            amount to set
     * @param milk
     *            amount to set
     * @param sugar
     *            amount to set
     * @param chocolate
     *            amount to set
     * @return a Recipe object
     */
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        final Ingredient chocolate1 = new Ingredient( "Chocolate", chocolate, false );
        final Ingredient coffee1 = new Ingredient( "Coffee", coffee, false );
        final Ingredient milk1 = new Ingredient( "Milk", milk, false );
        final Ingredient sugar1 = new Ingredient( "Sugar", sugar, false );
        recipe.addIngredient( chocolate1 );
        recipe.addIngredient( coffee1 );
        recipe.addIngredient( milk1 );
        recipe.addIngredient( sugar1 );
        return recipe;
    }

}
