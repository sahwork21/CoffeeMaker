package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @Autowired
    private IngredientService ingredientService;

    @BeforeEach
    public void setup () {
        ingredientService.deleteAll();
    }

    @Test
    @Transactional
    public void testConstructors () {
        final Ingredient ingredient = new Ingredient();
        assertNotNull( ingredient );
        assertEquals( "", ingredient.getName() );
        assertFalse( ingredient.getIsInventory() );
        System.out.println( "THE ID OF THIS INGREDIENT IS" + ingredient.getId() );

        final Ingredient ingredient2 = new Ingredient( "Coffee", 1, true );
        assertNotNull( ingredient2 );
        assertEquals( "Coffee", ingredient2.getName() );
        assertEquals( 1, ingredient2.getAmount() );
        assertTrue( ingredient2.getIsInventory() );

    }

    @Test
    @Transactional
    public void testSaveIngredient () {
        final Ingredient ingredient = new Ingredient();
        ingredient.setAmount( 50 );
        ingredient.setName( "Pumpkin Spice" );
        ingredient.setInventory( false );
        ingredientService.save( ingredient );

        assertEquals( 1, ingredientService.count() );
    }

    @Test
    @Transactional
    public void testGetSetName () {
        final Ingredient ingredient = new Ingredient();
        ingredient.setName( "Pumpkin Spice" );
        assertTrue( "Pumpkin Spice".equals( ingredient.getName() ) );

        assertThrows( IllegalArgumentException.class, () -> {
            ingredient.setName( null );
        } );
    }

    @Test
    @Transactional
    public void testGetSetAmount () {
        final Ingredient ingredient = new Ingredient();
        ingredient.setAmount( 500 );
        assertEquals( 500, ingredient.getAmount() );

        assertThrows( IllegalArgumentException.class, () -> {
            ingredient.setAmount( -50 );
        } );
    }

    @Test
    public void testIsInventory () {
        final Ingredient ingredient = new Ingredient();
        assertFalse( ingredient.getIsInventory() );
        ingredient.setInventory( true );
        assertTrue( ingredient.getIsInventory() );
    }

    @Test
    @Transactional
    public void testToString () {
        final Ingredient ingredient = new Ingredient();
        ingredient.setName( "Pumpkin Spice" );
        ingredient.setAmount( 50 );

        ingredient.toString();
        assertEquals( "Ingredient [name=Pumpkin Spice, amount=50]", ingredient.toString() );
        assertFalse( ingredient.getIsInventory() );
    }

    @Test
    @Transactional
    public void testEquals () {
        final Ingredient ingredient = new Ingredient();

        assertEquals( ingredient, ingredient );
        assertNotEquals( ingredient, null );
        assertNotEquals( ingredient, new Object() );

        final Ingredient ingredient2 = new Ingredient();
        ingredient.setName( "Pumpkin Spice" );

        assertFalse( ingredient.equals( ingredient2 ) );
        assertFalse( ingredient2.equals( ingredient ) );

        ingredient2.setName( "Pumpkin Spice" );

        assertEquals( ingredient, ingredient2 );
    }

}
