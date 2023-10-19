package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * The handler of CRUD operations that involve Ingredients. It has the ability
 * to save and delete objects from Service class methods. We can also find an
 * Ingredient by name
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {
    /**
     * The repo object that we want to save the Ingredient to or get from
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public IngredientRepository getRepository () {
        return ingredientRepository;
    }

    /**
     * Find a ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public List<Ingredient> findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }
}
