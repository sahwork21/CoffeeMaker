package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient class is a representation of an Ingredient in the CoffeeMaker
 * inventory and is used in Inventory and in Recipes. Each ingredient contains
 * an identifier, name, and amount. The only mutable field is amount, which
 * represents the current inventory quantity of the ingredient.
 */
@Entity
public class Ingredient extends DomainObject {
    /** Ingredient id */
    @Id
    @GeneratedValue
    private Long    id;
    /** Ingredient name */
    private String  name;

    /** Boolean value to check if the ingredient is in the inventory */
    private boolean isInventory;
    /**
     * Ingredient amount must be greater than or equal to 0
     */
    @Min ( 0 )
    private int     amount;

    /**
     * Empty constructor for creation
     */
    public Ingredient () {
        this.name = "";
        setAmount( 0 );
        setInventory( false );
    }

    /**
     * Constructor given a name and amount
     *
     * @param name
     *            Name of ingredient
     * @param amount
     *            Amount of ingredient
     * @param isInventory
     *            flag to signal this Ingredient belongs to an Inventory
     *            association
     */
    public Ingredient ( final String name, @Min ( 0 ) final int amount, final boolean isInventory ) {
        super();

        setName( name );
        setAmount( amount );
        setInventory( isInventory );

    }

    /**
     * Checks the isInventory flag to tell us if this is an object linked to
     * Inventory or Recipe
     *
     * @return the boolean value given
     */
    public boolean isInventory () {
        return isInventory;
    }

    /**
     * getIsInventory is required for support with Hibernate and Spring.
     *
     * @return true if ingredient is inventory, false otherwise.
     */
    public boolean getIsInventory () {
        return this.isInventory;
    }

    /**
     * Set the Inventory flag to the input value
     *
     * @param isInventory
     *            the value that denotes if this is true in the inventory or
     *            false an instance linked to a recipe
     */
    public void setInventory ( final boolean isInventory ) {
        this.isInventory = isInventory;
    }

    /**
     * Returns the unique generated Id of the ingredient
     *
     * @return Id of ingredient
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the name of the ingredient
     *
     * @return name of ingredient
     */
    public String getName () {
        return this.name;
    }

    /**
     * Returns amount of the ingredient
     *
     * @return Amount of ingredient
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Sets the name of the string
     *
     * @param name
     *            name to be
     * @throws IllegalArgumentException
     *             throws if name is null
     */
    public void setName ( final String name ) {
        if ( name == null ) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * Returns the amount of the ingredient
     *
     * @param amount
     *            Amount of ingredient
     */
    public void setAmount ( final int amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException();
        }
        this.amount = amount;
    }

    /**
     * Returns a string representation of the ingredient, containing its name
     * and amount.
     */
    @Override
    public String toString () {
        return String.format( "Ingredient [name=%s, amount=%d]", this.getName(), this.amount );
    }

    /**
     * hashCode method that considers name and isInventory flags
     */
    @Override
    public int hashCode () {
        return Objects.hash( isInventory, name );
    }

    /**
     * Comparison of Ingredient. Ingredients are equivalent if they share the
     * same name. Checks equality using the isInventory flag and name
     *
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Ingredient other = (Ingredient) obj;

        if ( this.isInventory != other.isInventory ) {
            return false;
        }

        // if ( this.getAmount() != other.getAmount() ) {
        // return false;
        // }

        if ( this.getName().equals( other.getName() ) ) {
            return true;
        }

        return false;
    }

}
