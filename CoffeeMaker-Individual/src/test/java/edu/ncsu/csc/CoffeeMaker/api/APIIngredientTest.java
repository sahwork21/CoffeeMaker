package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {


    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }



    @Test
    @Transactional
    public void testCreateIngredient() throws Exception {
        final Ingredient ingredient = new Ingredient("Apple", 5, false);
        
        
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() );


        Assertions.assertEquals( 1, (int) service.count() );
    }
    
    @Test
    @Transactional
    public void testCreateIngredientIsInventory() throws Exception {
        final Ingredient ingredient = new Ingredient("Apple", 5, true);
        
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() );


        Assertions.assertEquals( 1, (int) service.count() );
        
        final Ingredient ingredientCopy = new Ingredient("Apple", 2, true);
        assertEquals(true, ingredientCopy.isInventory());
        
        // Shouldn't be able to add another ingredient that is an inventory ingredient
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredientCopy ) ) ).andExpect( status().isConflict() );


        Assertions.assertEquals( 1, (int) service.count() );
    }
    
    @Test
    @Transactional
    public void testGetIngredients() throws Exception {
        final Ingredient ingredient = new Ingredient("Apple", 5, true);
        
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() ).andDo( MockMvcResultHandlers.print() );



        String response = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
                    .andReturn().getResponse().getContentAsString();
        
        assertTrue(response.contains( "Apple" ));
    
        
        final Ingredient ingredientCopy = new Ingredient("Banana", 2, false);
        
        // Shouldn't be able to add another ingredient that is an inventory ingredient
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredientCopy ) ) ).andExpect( status().isOk() );

        response = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
    

        assertTrue(response.contains( "Banana" ));
        assertTrue(response.contains( "Apple" ));

    }
    
    
    @Test
    @Transactional
    public void testUpdateIngredient() throws Exception {
        
        final Ingredient ingredient = new Ingredient("Apple", 5, true);
        
        // Shouldn't be able to update  ingredient that doesn't exist
        mvc.perform( put( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isNotFound() );
        
        // Add it to database
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() ).andDo( MockMvcResultHandlers.print() );

        
        final Ingredient ingredientCopy = new Ingredient("Apple", 10, true);
        
        mvc.perform( put( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredientCopy ) ) ).andExpect( status().isOk() );

        String response = mvc.perform( get( "/api/v1/ingredients/apple" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
    

        assertTrue(response.contains( "10" ));

    }
    
    @Test
    @Transactional
    public void testGetIngredient() throws Exception {
        // Test that ingredient returns not found on ingredient that isn't saved
        mvc.perform( get( "/api/v1/ingredients/idontexist" ) ).andDo( print() ).andExpect( status().isNotFound() )
                .andReturn().getResponse().getContentAsString();
        
        final Ingredient ingredient = new Ingredient("Apple", 5, true);
        
        //
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() ).andDo( MockMvcResultHandlers.print() );



        String response = mvc.perform( get( "/api/v1/ingredients/apple" ) ).andDo( print() ).andExpect( status().isOk() )
                    .andReturn().getResponse().getContentAsString();
        
        assertTrue(response.contains( "Apple" ));
        assertTrue(response.contains( "5" ));
        assertTrue(response.contains( "true" ));
    

    }
    
    @Test
    @Transactional
    public void testDeleteIngredient() throws Exception {
        
        mvc.perform( delete( "/api/v1/ingredients/apple" ) ).andDo( print() ).andExpect( status().isNotFound() )
        .andReturn().getResponse().getContentAsString();
        
        final Ingredient ingredient = new Ingredient("Apple", 5, true);
        
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() ).andDo( MockMvcResultHandlers.print() );

        

        mvc.perform( delete( "/api/v1/ingredients/apple" ) ).andDo( print() ).andExpect( status().isOk() )
        .andReturn().getResponse().getContentAsString();
        
    

    }

}
