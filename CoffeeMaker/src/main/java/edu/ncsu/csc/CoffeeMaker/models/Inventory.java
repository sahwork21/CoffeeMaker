package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe contained ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        this.ingredients = new ArrayList<Ingredient>();
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Construct an Inventory from a provided list Use the addIngredients method
     * so they cannot sneak duplicates by us
     *
     * @param ingredients
     *            ingredients we want to add to the inventory
     */
    public Inventory ( final List<Ingredient> ingredients ) {
        this.ingredients = new ArrayList<Ingredient>();
        addIngredients( ingredients );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Private helper method that finds the corresponding Ingredient in the
     * inventory. It returns the Ingredient object to be checked and altered
     * when making recipes or adding to the inventory
     *
     * @param name
     *            the target Ingredient's name
     * @return the Ingredient with a name matching the input name String
     */
    public Ingredient getIngredient ( final String name ) {
        for ( final Ingredient ingredient : this.ingredients ) {
            if ( ingredient.getName().equals( name ) ) {
                return ingredient;
            }
        }

        return null;
    }

    /**
     * Returns the list of Ingredients in the Inventory
     *
     * @return ingredients list
     */
    public List<Ingredient> getIngredients () {
        return ingredients;

    }

    /**
     * Setter methods to add to the list
     *
     * @param ingredients
     *            the ingredients we want to change to
     */
    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients.clear();
        addIngredients( ingredients );
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        for ( final Ingredient ingredient : r.getIngredients() ) {
            final Ingredient inventoryIngredient = this.getIngredient( ingredient.getName() );
            if ( inventoryIngredient == null || ingredient.getAmount() > inventoryIngredient.getAmount() ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( !enoughIngredients( r ) ) {
            return false;
        }

        for ( final Ingredient ingredient : r.getIngredients() ) {
            final Ingredient inventoryIngredient = this.getIngredient( ingredient.getName() );
            final int newAmount = inventoryIngredient.getAmount() - ingredient.getAmount();
            inventoryIngredient.setAmount( newAmount );
        }

        return true;
    }

    /**
     * Uses the getIngredient helper method to find the corresponding Ingredient
     * and add to it. If the input ingredient does not exist then it is not
     * saved to the database so throw the input out
     *
     * @param ingredient
     *            the ingredient that we want to add to the
     * @return true if the Ingredient was successfully added to. False if not
     *
     * @throws IllegalArgumentException
     *             if the ingredient's IngredientType is a duplicate that is in
     *             the Inventory already
     */
    public boolean addIngredient ( final Ingredient ingredient ) throws IllegalArgumentException {
        // if ( ingredient.getAmount() < 0 ) {
        // throw new IllegalArgumentException( "Ingredient amounts must be 0 or
        // more. Ingredient "
        // + ingredient.getName().toString() + "'s amount was " +
        // ingredient.getAmount() );
        // }

        // If the ingredient exists in the inventory we just want to reset some
        // values
        if ( this.getIngredient( ingredient.getName() ) != null ) {
            for ( final Ingredient i : ingredients ) {
                if ( i.getName().equals( ingredient.getName() ) ) {
                    i.setAmount( i.getAmount() + ingredient.getAmount() );
                    return true;
                }
            }
        }

        ingredient.setInventory( true );
        ingredients.add( ingredient );

        return true;
    }

    /**
     * Sets the amount for the provided ingredient Only accepts positive values
     *
     * @param ingredient
     *            the ingredient object we want to change
     */
    public void setIngredient ( final Ingredient ingredient ) {
        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( ingredient.getName() ) && ingredient.getAmount() >= 0 ) {
                i.setAmount( ingredient.getAmount() );
            }
        }
    }

    /**
     * Add the number of units in the inventory to the current amount of units
     * for the provided IngredientType.
     *
     * @param name
     *            the IngredientType we are checking for
     * @param amount
     *            amount of an ingredient
     *
     * @return checked amount of an Ingredient
     * @throws IllegalArgumentException
     *             if the amount parameter isn't a positive integer (less than
     *             0) or the name is not a valid IngredientType
     */
    public Ingredient checkIngredient ( final String name, final String amount ) throws IllegalArgumentException {

        // Verify the amount provided is 0 or more
        Integer amt = 0;
        try {
            amt = Integer.parseInt( amount );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }
        if ( amt < 0 ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }

        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( name ) ) {

                return new Ingredient( i.getName(), amt, true );

            }
        }

        throw new IllegalArgumentException( "The provided IngredientType does not exist" );
    }

    /**
     * Add all the ingredients in the list of ingredients to the inventory.
     * Validation of the input is done first making sure no values are below 0.
     * After than the addIngredient method takes over and either adds to the
     * existing amount or adds a new Ingredient to the Inventory
     *
     * @param ingredients
     *            the list of ingredients we want to add to the inventory
     * @return returns true if the ingredients were all successfully added
     * @throws IllegalArgumentException
     *             if an ingredient has a negative values (less than 0)
     */
    public boolean addIngredients ( final List<Ingredient> ingredients ) throws IllegalArgumentException {
        // Verify the input has only positive amounts for ingredients
        // Make sure every ingredient is present in the inventory

        // No problems so we can add to the inventory
        for ( final Ingredient i : ingredients ) {
            addIngredient( i );
        }

        return true;

    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        return "Inventory with ingredients " + this.ingredients.toString();
    }

}
