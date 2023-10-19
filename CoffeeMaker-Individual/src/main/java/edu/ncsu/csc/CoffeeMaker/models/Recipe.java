package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /** Recipe contained ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        for ( final Ingredient i : ingredients ) {
            if ( i.getAmount() > 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
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
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Returns the list of ingredients contained by the Recipe
     *
     * @return ingredients the field containing ingredients corresponding with
     *         this recipe
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Set our ingredients field to point to an ingredient
     *
     * @param ingredients
     *            the list we want to point to
     */
    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the Ingredient object with the associated name in the ingredients
     * field
     *
     * @param name
     *            the name to look for
     * @return null if the Ingredient with matching name is not found or an
     *         Ingredient if a matching name is found
     */
    public Ingredient getIngredientByName ( final String name ) {
        // Find the ingredient and return it by name
        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( name ) ) {
                return i;
            }
        }
        return null;
    }

    /**
     * Sets the Ingredient amount to i Does nothing if an Ingredient with
     * matching names does not exist
     *
     * @param ing
     *            the ingredient that we want to change its amount
     *
     */
    public void setIngredient ( final Ingredient ing ) {
        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( ing.getName() ) ) {
                i.setAmount( ing.getAmount() );
            }
        }
    }

    /**
     * Adds an ingredient to the list of ingredients
     *
     * @param ingredient
     *            the ingredient to add
     * @throws IllegalArgumentException
     *             throws if the ingredient being added is a duplicate
     */
    public void addIngredient ( final Ingredient ingredient ) {
        ingredient.setInventory( false );
        // Find if this ingredient is already in the list
        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( ingredient.getName() ) ) {
                throw new IllegalArgumentException( "Duplicate ingredients not allowed" );
            }
        }
        ingredient.setInventory( false );
        this.ingredients.add( ingredient );
    }

    /**
     * edit recipe method to change the values of price to the parameter. Also
     * changes the Ingredients contained by this Recipe
     *
     * @param r
     *            the recipe we want to edit this recipe to mimic
     * @throws IllegalArgumentException
     *             throws if the recipe is invalid
     */
    public void editRecipe ( final Recipe r ) {
        // If the name does not match up this is an invalid edit
        if ( !r.getName().equals( this.name ) ) {
            throw new IllegalArgumentException( "Names do not match so we cannot edit the recipe" );
        }

        // We need to make sure this recipe is valid just like when posting a
        // recipe
        boolean noIngredient = false;
        for ( final Ingredient i : r.getIngredients() ) {
            if ( i.getAmount() > 0 ) {
                noIngredient = true;
            }
            i.setInventory( false );
        }

        if ( !noIngredient ) {
            throw new IllegalArgumentException(
                    "Input recipe does not have at least one recipe with an amount greater than zero" );
        }

        // We don't have to verify Ingredients since they verify their own
        // amounts
        // but check the price
        if ( r.getPrice() < 0 ) {
            // Throw an exception since this recipe is invalid
            throw new IllegalArgumentException( "Cannot edit recipe since input recipe has invalid price" );
        }

        setPrice( r.getPrice() );
        setIngredients( r.getIngredients() );

    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        if ( !"".equals( name ) ) {
            return name + " with ingredients " + this.ingredients.toString();
        }
        return name;

    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

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
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
