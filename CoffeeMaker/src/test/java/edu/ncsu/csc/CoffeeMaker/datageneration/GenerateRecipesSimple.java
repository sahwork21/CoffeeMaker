
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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipesSimple {

    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testCreateRecipes () {

        recipeService.deleteAll();

        final Recipe r1 = new Recipe();
        r1.addIngredient( new Ingredient() );
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );

        final Recipe r2 = new Recipe();
        r2.addIngredient( new Ingredient() );
        r2.setName( "Mocha" );
        r2.setPrice( 3 );

        recipeService.save( r1 );
        recipeService.save( r2 );

        Assert.assertEquals( "Creating two recipes should results in two recipes in the database", 2,
                recipeService.count() );

    }

}
