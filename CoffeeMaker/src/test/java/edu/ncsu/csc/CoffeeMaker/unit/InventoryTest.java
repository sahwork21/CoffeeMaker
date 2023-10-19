package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Test class for the Inventory object
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {
    /**
     * Inventory Service to get the inventory and save it
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * Set up our database with some basic ingredients first
     */
    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Coffee", 500, false ) );
        ivt.addIngredient( new Ingredient( "Milk", 500, false ) );
        ivt.addIngredient( new Ingredient( "Chocolate", 500, false ) );
        ivt.addIngredient( new Ingredient( "Sugar", 500, false ) );

        assertEquals( 4, ivt.getIngredients().size() );
        assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount() );
        assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount() );
        assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount() );
        assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount() );

        assertEquals( "Coffee", ivt.getIngredients().get( 0 ).getName() );
        assertEquals( "Milk", ivt.getIngredients().get( 1 ).getName() );
        assertEquals( "Chocolate", ivt.getIngredients().get( 2 ).getName() );
        assertEquals( "Sugar", ivt.getIngredients().get( 3 ).getName() );

        assertTrue( ivt.getIngredients().get( 0 ).getIsInventory() );
        assertTrue( ivt.getIngredients().get( 0 ).getIsInventory() );
        assertTrue( ivt.getIngredients().get( 0 ).getIsInventory() );
        assertTrue( ivt.getIngredients().get( 0 ).getIsInventory() );

        inventoryService.save( ivt );
    }

    /**
     * Test using up inventory works properly
     */
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        recipe.addIngredient( new Ingredient( "Chocolate", 10, false ) );
        recipe.addIngredient( new Ingredient( "Milk", 20, false ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5, false ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1, false ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, i.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 480, i.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 495, i.getIngredient( "Sugar" ).getAmount() );
        Assertions.assertEquals( 499, i.getIngredient( "Coffee" ).getAmount() );
    }

    /**
     * Test valid values for ingredientsds
     */
    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Coffee", 5, false ) );
        ingredients.add( new Ingredient( "Milk", 3, false ) );
        ingredients.add( new Ingredient( "Sugar", 7, false ) );
        ingredients.add( new Ingredient( "Chocolate", 2, false ) );

        ivt.addIngredients( ingredients );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, ivt.getIngredient( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, ivt.getIngredient( "Milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, ivt.getIngredient( "Sugar" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, ivt.getIngredient( "Chocolate" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    /**
     * Test invalid coffee value
     */
    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        final List<Ingredient> ingredients = new ArrayList<Ingredient>();

        ingredients.add( new Ingredient( "Milk", 3, false ) );
        ingredients.add( new Ingredient( "Sugar", 7, false ) );
        ingredients.add( new Ingredient( "Chocolate", 2, false ) );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 497, ivt.getIngredient( "Milk" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 493, ivt.getIngredient( "Sugar" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 498, ivt.getIngredient( "Chocolate" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for coffee should result in no changes -- chocolate" );
        }
    }

    /**
     * Test invalid milk value
     */
    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Coffee", 5, false ) );

        ingredients.add( new Ingredient( "Sugar", 7, false ) );
        ingredients.add( new Ingredient( "Chocolate", 2, false ) );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 495, ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredient( "Milk" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 493, ivt.getIngredient( "Sugar" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 498, ivt.getIngredient( "Chocolate" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for milk should result in no changes -- chocolate" );
        }

    }

    /**
     * Test invalid sugar value
     */
    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Coffee", 5, false ) );
        ingredients.add( new Ingredient( "Milk", 3, false ) );

        ingredients.add( new Ingredient( "Chocolate", 2, false ) );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 495, ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 497, ivt.getIngredient( "Milk" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredient( "Sugar" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 498, ivt.getIngredient( "Chocolate" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for sugar should result in no changes -- chocolate" );
        }

    }

    /**
     * Test invalid chocolate value
     */
    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Coffee", 5, false ) );
        ingredients.add( new Ingredient( "Milk", 3, false ) );
        ingredients.add( new Ingredient( "Sugar", 7, false ) );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 495, ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 497, ivt.getIngredient( "Milk" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 493, ivt.getIngredient( "Sugar" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredient( "Chocolate" ).getAmount(),
                    "Trying to update the Inventory with an non-existant value for chocolate should result in no changes -- chocolate" );
        }

    }

    //
    /**
     * This tests to see if the inventory can check if there are enough of each
     * ingredient to make the recipe.
     */
    @Test
    @Transactional
    public void testEnoughIngredients () {
        final Recipe recipe = new Recipe();
        recipe.setName( "coffee!" );
        recipe.addIngredient( new Ingredient( "Coffee", 3, false ) );
        recipe.addIngredient( new Ingredient( "Milk", 1, false ) );
        recipe.addIngredient( new Ingredient( "Sugar", 3, false ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 2, false ) );
        recipe.setPrice( 0 );
        final Inventory ivt = inventoryService.getInventory();
        ivt.setIngredient( new Ingredient( "Coffee", 1, false ) );
        ivt.setIngredient( new Ingredient( "Milk", 1, false ) );
        ivt.setIngredient( new Ingredient( "Sugar", 3, false ) );
        ivt.setIngredient( new Ingredient( "Chocolate", 2, false ) );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        final Inventory ivt2 = inventoryService.getInventory();
        ivt.setIngredient( new Ingredient( "Coffee", 3, false ) );
        ivt.setIngredient( new Ingredient( "Milk", 0, false ) );
        ivt.setIngredient( new Ingredient( "Sugar", 3, false ) );
        ivt.setIngredient( new Ingredient( "Chocolate", 2, false ) );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        final Inventory ivt3 = inventoryService.getInventory();
        ivt.setIngredient( new Ingredient( "Coffee", 3, false ) );
        ivt.setIngredient( new Ingredient( "Milk", 1, false ) );
        ivt.setIngredient( new Ingredient( "Sugar", 2, false ) );
        ivt.setIngredient( new Ingredient( "Chocolate", 2, false ) );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        final Inventory ivt4 = inventoryService.getInventory();
        ivt.setIngredient( new Ingredient( "Coffee", 3, false ) );
        ivt.setIngredient( new Ingredient( "Milk", 1, false ) );
        ivt.setIngredient( new Ingredient( "Sugar", 3, false ) );
        ivt.setIngredient( new Ingredient( "Chocolate", 1, false ) );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        final Inventory ivt5 = inventoryService.getInventory();
        ivt.setIngredient( new Ingredient( "Coffee", 3, false ) );
        ivt.setIngredient( new Ingredient( "Milk", 1, false ) );
        ivt.setIngredient( new Ingredient( "Sugar", 3, false ) );
        ivt.setIngredient( new Ingredient( "Chocolate", 2, false ) );
        Assertions.assertTrue( ivt.enoughIngredients( recipe ) );

    }

    /**
     * This tests the checks methods for each ingredient, while also being able
     * to check the toString
     */
    @Test
    @Transactional
    public void testChecks () {
        final Inventory ivt = inventoryService.getInventory();
        ivt.setIngredient( ivt.checkIngredient( "Coffee", "1" ) );
        ivt.setIngredient( ivt.checkIngredient( "Milk", "1" ) );
        ivt.setIngredient( ivt.checkIngredient( "Sugar", "1" ) );
        ivt.setIngredient( ivt.checkIngredient( "Chocolate", "1" ) );
        Assertions.assertEquals( "Inventory with ingredients [Ingredient [name=Coffee, amount="
                + ivt.getIngredient( "Coffee" ).getAmount() + "], " + "Ingredient [name=Milk, amount="
                + ivt.getIngredient( "Milk" ).getAmount() + "], " + "Ingredient [name=Chocolate, amount="
                + ivt.getIngredient( "Chocolate" ).getAmount() + "], " + "Ingredient [name=Sugar, amount="
                + ivt.getIngredient( "Sugar" ).getAmount() + "]]", ivt.toString() );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Coffee", "-1" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Coffee", "hello" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Milk", "-1" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Milk", "hello" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Sugar", "-1" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Sugar", "hello" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Chocolate", "-1" ) ) );
        Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.setIngredient( ivt.checkIngredient( "Chocolate", "hello" ) ) );
    }

    /**
     * Tests the exceptions thrown by the Inventory class
     */
    @Test
    @Transactional
    public void testExceptions () {

        // Make a new Inventory with invalid ingredient amounts
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Coffee", 5, false ) );
        ingredients.add( new Ingredient( "Milk", 3, false ) );

        ingredients.add( new Ingredient( "Chocolate", 2, false ) );

        // Check an ingredient that does not exist
        final Inventory ivt = inventoryService.getInventory();
        assertThrows( IllegalArgumentException.class, () -> ivt.checkIngredient( "NOTHING", "0" ) );

        // Create an inventory with not enough ingredients
        ingredients.remove( 0 );
        ingredients.remove( 0 );
        ingredients.remove( 0 );

        ingredients.add( new Ingredient( "Coffee", 5, false ) );
        ingredients.add( new Ingredient( "Milk", 3, false ) );
        ingredients.add( new Ingredient( "Chocolate", 2, false ) );

        final Inventory ivt2 = new Inventory( ingredients );
        ingredients.set( 0, new Ingredient( "Coffee", 99, false ) );
        final Recipe r = new Recipe();
        r.addIngredient( ingredients.get( 0 ) );
        r.addIngredient( ingredients.get( 1 ) );
        r.addIngredient( ingredients.get( 2 ) );

        // There is not enough coffee
        assertFalse( ivt2.useIngredients( r ) );

        assertEquals( 5, ivt2.getIngredient( "Coffee" ).getAmount() );
        assertEquals( 3, ivt2.getIngredient( "Milk" ).getAmount() );
        assertEquals( 2, ivt2.getIngredient( "Chocolate" ).getAmount() );
        // Add another ingredient to the recipe that is not contained
        r.addIngredient( new Ingredient( "Pumpkin Spice", 1, false ) );
        assertFalse( ivt2.useIngredients( r ) );

        assertEquals( 5, ivt2.getIngredient( "Coffee" ).getAmount() );
        assertEquals( 3, ivt2.getIngredient( "Milk" ).getAmount() );
        assertEquals( 2, ivt2.getIngredient( "Chocolate" ).getAmount() );

        // Now add enough ingredients so we can buy the drink
        ivt2.addIngredient( new Ingredient( "Pumpkin Spice", 2, false ) );
        ivt2.setIngredient( new Ingredient( "Coffee", 100, false ) );
        assertTrue( ivt2.useIngredients( r ) );

        assertEquals( 1, ivt2.getIngredient( "Coffee" ).getAmount() );
        assertEquals( 0, ivt2.getIngredient( "Milk" ).getAmount() );
        assertEquals( 0, ivt2.getIngredient( "Chocolate" ).getAmount() );
        assertEquals( 1, ivt2.getIngredient( "Pumpkin Spice" ).getAmount() );

        // Try to illegally set or add ingredients with negative amount
        assertThrows( IllegalArgumentException.class,
                () -> ivt2.setIngredient( new Ingredient( "Coffee", -1, false ) ) );
        assertEquals( 1, ivt2.getIngredient( "Coffee" ).getAmount() );
        assertThrows( IllegalArgumentException.class,
                () -> ivt2.addIngredient( new Ingredient( "Syrup", -1, false ) ) );

    }

    /**
     * Test to get the Inventory from MySQL
     */
    @Test
    @Transactional
    public void testGetInventory () {
        final Inventory ivt = inventoryService.getInventory();

    }

    /**
     * This tests the setId() method
     */
    @Test
    @Transactional
    public void testSetId () {
        final Inventory ivt = inventoryService.getInventory();
        ivt.setId( (long) 2 );
        Assertions.assertEquals( 2, ivt.getId() );
    }

}
