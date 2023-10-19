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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints for the Ingredients
 *
 * Spring converts Ingredients to a JSON file so they can get saved
 *
 *
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * Object responsible for saving ingredients to the database
     */
    @Autowired
    private IngredientService service;

    /**
     * Helper function. Finds the ingredient that is the one represented in
     * inventory.
     *
     * @param ingredients
     *            The list of ingredients with the same name.
     * @return The ingredient that isInventory is set to true
     */
    private Ingredient getInventoryIngredient ( final List<Ingredient> ingredients ) {
        for ( final Ingredient anIngredient : ingredients ) {
            if ( anIngredient.isInventory() ) {
                return anIngredient;
            }
        }
        return null;
    }

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * Returns the inventory instance of the given ingredient in the system
     *
     * @param name
     *            Name of ingredient
     * @return JSON representation of ingredient
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable final String name ) {
        final List<Ingredient> ingredients = service.findByName( name );
        final Ingredient ingredient = getInventoryIngredient( ingredients );
        if ( ingredient == null ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( ingredient, HttpStatus.OK );
    }

    /**
     * Creates a new ingredient given that it is not a inventory ingredient, or
     * inventory ingredient does not exist.
     *
     * @param ingredient
     *            The ingredient object to save
     * @return ResponseEntity, status 200 if created. CONFLICT otherwise.
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        final List<Ingredient> ingredients = service.findByName( ingredient.getName() );
        System.out.println(
                "THE SIZE OF INGREDIENTS IS" + ingredients.size() + ingredient.isInventory() + ingredient.getName() );
        if ( ingredient.isInventory() && ingredients.size() > 0 ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with name " + ingredient.getName() + "already exists" ),
                    HttpStatus.CONFLICT );
        }

        service.save( ingredient );
        return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ), HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's Ingredients
     * This will update the ingredient in CoffeeMaker by setting amount from the
     * ingredient provided to the CoffeeMaker's stored amount
     *
     * @param ingredient
     *            amount to set to ingredient
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity updateIngredient ( @RequestBody final Ingredient ingredient ) {
        final Ingredient ingredientCurrent = getInventoryIngredient( service.findByName( ingredient.getName() ) );
        if ( ingredientCurrent == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + ingredient.getName() ),
                    HttpStatus.NOT_FOUND );
        }
        ingredientCurrent.setAmount( ingredient.getAmount() );
        service.save( ingredientCurrent );
        return new ResponseEntity( ingredientCurrent, HttpStatus.OK );
    }

    /**
     * REST API method to allow deleting a Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final List<Ingredient> ingredients = service.findByName( name );
        final Ingredient ingredient = getInventoryIngredient( ingredients );
        if ( ingredient == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( ingredient );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

}
