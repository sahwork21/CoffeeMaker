package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIRecipeController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService     service;

    /**
     * IngredientService object to allow us to delete Ingredients. Called when
     * we edit a recipe and the previous ingredient objects get deleted and
     * replaced by new ones.
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/recipes" )
    public List<Recipe> getRecipes () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity getRecipe ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        return null == recipe
                ? new ResponseEntity( errorResponse( "No recipe found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( recipe, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail. Service should save
     * Ingredient for us
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/recipes" )
    public ResponseEntity createRecipe ( @RequestBody final Recipe recipe ) {
        if ( null != service.findByName( recipe.getName() ) ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + recipe.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( service.findAll().size() < 3 ) {
            // We might need to vet the Recipe for at least one ingredient that
            // isn't 0
            boolean noIngredient = false;
            for ( final Ingredient i : recipe.getIngredients() ) {
                if ( i.getAmount() > 0 ) {
                    noIngredient = true;
                }
            }
            if ( !noIngredient ) {
                return new ResponseEntity(
                        successResponse(
                                recipe.getName() + " needs at least one ingredient with an amount more than 0" ),
                        HttpStatus.BAD_REQUEST );
            }
            service.save( recipe );
            return new ResponseEntity( successResponse( recipe.getName() + "successfully created" ), HttpStatus.OK );
        }
        else {

            return new ResponseEntity(
                    errorResponse( "Insufficient space in recipe book for recipe " + recipe.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( recipe );

        // And we have to delete the Ingredients that correspond to the Recipes

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow editing of a Recipe. Recipe is edited by
     * matching an input recipe's fields to our searched for recipe. PUT
     * operation makes sure to replace the recipe object's fields and deletes
     * associated ingredients since new elements are saved to the database and
     * old ones are removed.
     *
     * Basically copies input recipe to the searched for one. Saves this
     * recipe's new ingredients and throws out the old ones
     *
     * @param name
     *            the name of the Recipe to edit
     * @param recipeSubmitted
     *            the recipeSubmitted is the recipe we want to copy over
     * @return a ResponseEntity that is a success and ok if we edited the
     *         recipe. Error if the recipe was not found or the user put in a
     *         bad recipe to edit
     */
    @PutMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity editRecipe ( @PathVariable final String name, @RequestBody final Recipe recipeSubmitted ) {

        // Find a recipe with a matching name
        final Recipe recipeToEdit = service.findByName( name );

        // Make sure we actually found a recipe to edit
        if ( recipeToEdit == null ) {
            return new ResponseEntity( errorResponse( name + " does not exist" ), HttpStatus.NOT_FOUND );
        }
        // Get the ingredients since we may have to throw them out
        final List<Ingredient> deletes = recipeToEdit.getIngredients();

        // If the user put in a bad recipe it will throw an exception on the
        // editRecipe method
        try {
            recipeToEdit.editRecipe( recipeSubmitted );

        }
        catch ( final IllegalArgumentException e ) {
            // The user made a bad request
            return new ResponseEntity( errorResponse( name
                    + " could not be edited since the input recipe is invalid in price, mismatched names, or ingredient amounts" ),
                    HttpStatus.BAD_REQUEST );

        }

        // They didn't mess up so delete old ingredients
        for ( final Ingredient i : deletes ) {
            ingredientService.delete( i );
        }

        // And save this one to database
        service.save( recipeToEdit );

        // All good and the data is exported
        return new ResponseEntity( successResponse( name + " was edited successfully" ), HttpStatus.OK );
    }
}
