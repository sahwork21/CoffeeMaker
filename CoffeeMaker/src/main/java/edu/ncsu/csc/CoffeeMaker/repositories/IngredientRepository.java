package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * Ingredient CRUD operations are done here for an Ingredient and Spring will
 * generate more code to do
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    /**
     * Finds an Ingredient object with the provided name. Spring will generate
     * code to make this happen.
     *
     * @param name
     *            Name of the ingredient
     * @return Found ingredient, null if none.
     */
    List<Ingredient> findByName ( String name );
}
