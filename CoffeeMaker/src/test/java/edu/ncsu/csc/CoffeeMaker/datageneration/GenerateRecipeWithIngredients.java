
package edu.ncsu.csc.CoffeeMaker.datageneration;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {

    @Autowired
    private RecipeService recipeService;

    @Before
    public void setup () {
        recipeService.deleteAll();
    }

    @Test
    @Transactional
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient( new Ingredient( "Coffee", 10, false ) );
        r1.addIngredient( new Ingredient( "Pumpkin Spice", 3, true ) );
        r1.addIngredient( new Ingredient( "Milk", 2, false ) );

        recipeService.save( r1 );

        assertEquals( 1, recipeService.findAll().size() );

        final List<Ingredient> ings = recipeService.findAll().get( 0 ).getIngredients();
        assertEquals( "Coffee", ings.get( 0 ).getName() );
        assertEquals( "Pumpkin Spice", ings.get( 1 ).getName() );
        assertEquals( "Milk", ings.get( 2 ).getName() );
        assertEquals( 10, ings.get( 0 ).getAmount() );
        assertEquals( 3, ings.get( 1 ).getAmount() );
        assertEquals( 2, ings.get( 2 ).getAmount() );

        assertFalse( ings.get( 0 ).isInventory() );
        assertFalse( ings.get( 1 ).isInventory() );
        assertFalse( ings.get( 2 ).isInventory() );

        printRecipes();
    }

    private void printRecipes () {
        for ( final DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}
