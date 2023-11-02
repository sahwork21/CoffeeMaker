package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
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
public class APIInventoryController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService  service;

    /**
     * Autowired in by spring to allow us to delete old ingredients
     *
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the Inventory to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity getInventory () {
        final Inventory inventory = service.getInventory();
        return new ResponseEntity( inventory, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity updateInventory ( @RequestBody final Inventory inventory ) {
        final Inventory inventoryCurrent = service.getInventory();

        // Delete the old ingredients before adding so we do not save unlinked
        // ingredients
        final List<Ingredient> deletes = inventoryCurrent.getIngredients();

        // Add in the ingredients and then set the list on inventoryCurrent
        inventory.addIngredients( inventoryCurrent.getIngredients() );

        // Now delete all those old ingredients
        for ( final Ingredient i : deletes ) {
            ingredientService.delete( i );
        }

        /* IMPORTANT **/

        // Uncomment this and save the inventory current if things go wrong with
        // ingredients
        // inventoryCurrent.setIngredients( inventory.getIngredients() );
        // Delete the old inventory and save the new one

        service.delete( inventoryCurrent );
        service.save( inventory );

        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding a
     * new Ingredient to the database with 1 inventory flag
     *
     * @param ing
     *            New ingredient object we wish to save
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory/ingredients" )
    public ResponseEntity updateInventory ( @RequestBody final Ingredient ing ) {
        final Inventory inventoryCurrent = service.getInventory();

        // Add in the ingredients and then set the list on inventoryCurrent
        // Make sure the ingredient is a new ingredient first
        if ( inventoryCurrent.getIngredient( ing.getName() ) != null ) {
            // This ingredient already exists so don't make it twice
            return new ResponseEntity( errorResponse( "Ingredient already exists in the inventory" ),
                    HttpStatus.CONFLICT );
        }

        inventoryCurrent.addIngredient( ing );

        // The change deletes the old inventory and replaces it
        // This should resolve an issue with unlinked ingredients persisting
        // .delete( inventoryCurrent );

        service.save( inventoryCurrent );
        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }
}
