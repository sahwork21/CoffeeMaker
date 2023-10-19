
package edu.ncsu.csc.CoffeeMaker.datageneration;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateAndDeleteIngredients {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeService     recipeService;

    @Autowired
    private InventoryService  inventoryService;

    /**
     * This is a test to make sure that when we add a recipe then remove it the
     * ingredients associated get deleted.
     */
    @Test
    @Transactional
    public void testCreateAndDeleteIngredientsRecipe () {
        ingredientService.deleteAll();
        recipeService.deleteAll();

        final Ingredient i1 = new Ingredient( "Coffee", 5, false );

        final Ingredient i2 = new Ingredient( "Milk", 3, true );

        final Recipe r = new Recipe();
        r.setName( "Cafe" );
        r.setPrice( 15 );
        r.addIngredient( i1 );
        r.addIngredient( i2 );

        // Saving should add two new ingredients
        recipeService.save( r );
        ingredientService.save( new Ingredient( "Coffee", 5, false ) );

        Assert.assertEquals( 3, ingredientService.count() );
        Assert.assertEquals( 1, recipeService.count() );

        // Now delete and watch the amounts drop
        recipeService.delete( r );
        Assert.assertEquals( 1, ingredientService.count() );
        Assert.assertEquals( 0, recipeService.count() );

    }

    /**
     * Do the same thing but with the Inventory object this time
     */
    @Test
    @Transactional
    public void testCreateAndDeleteIngredientsInventory () {
        ingredientService.deleteAll();
        inventoryService.deleteAll();

        final Ingredient i1 = new Ingredient( "Coffee", 5, false );

        final Ingredient i2 = new Ingredient( "Milk", 3, true );

        // Now add these ingredients to a new Inventory and save it
        final Inventory inv = new Inventory();
        inv.addIngredient( i2 );
        inv.addIngredient( i1 );
        inventoryService.save( inv );
        ingredientService.save( new Ingredient( "Coffee", 5, true ) );
        // Now ensure that amounts increased
        Assert.assertEquals( 3, ingredientService.count() );
        Assert.assertEquals( 1, inventoryService.count() );

        // Now delete and watch the amounts drop
        inventoryService.delete( inv );
        Assert.assertEquals( 1, ingredientService.count() );
        Assert.assertEquals( 0, inventoryService.count() );

    }
}
