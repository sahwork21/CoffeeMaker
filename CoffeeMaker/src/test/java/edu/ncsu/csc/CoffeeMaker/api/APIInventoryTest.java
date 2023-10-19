package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Testing file for Inventory
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
class APIInventoryTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * Spring's testing framework to handle web servers
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Test class to get stuff easier
     */
    @Autowired
    private InventoryService      inventoryService;

    /**
     * Test class to get stuff easier
     */
    @Autowired
    private IngredientService     ingredientService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests the ability to add an Ingredient through the API call
     *
     * @throws Exception
     *             throws if you mess up and a response entity has differing
     *             status
     */
    @Test
    @Transactional
    public void testAddIngredient () throws Exception {
        assertEquals( 0, ingredientService.count() );

        final Ingredient i = new Ingredient( "Coffee", 15, false );

        mvc.perform( put( "/api/v1/inventory/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        assertEquals( 1, ingredientService.count() );
        assertEquals( 1, inventoryService.getInventory().getIngredients().size() );

        // Now add a duplicate named ingredient
        final Ingredient i2 = new Ingredient( "Coffee", 10, true );
        mvc.perform( put( "/api/v1/inventory/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().isConflict() );

        // Now make sure the inventory is still one and there is still only one
        // ingredient
        assertEquals( 1, ingredientService.count() );
        assertEquals( 1, inventoryService.getInventory().getIngredients().size() );

        // Now add a second ingredient to the inventory
        final Ingredient i3 = new Ingredient( "Milk", 17, false );
        mvc.perform( put( "/api/v1/inventory/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().isOk() );
        assertEquals( 2, ingredientService.count() );
        assertEquals( 2, inventoryService.getInventory().getIngredients().size() );

        final String invent = mvc.perform( get( "/api/v1/inventory" ) ).andReturn().getResponse().getContentAsString();
        // Make sure the print messages are right
        assertTrue( invent.contains( "\"name\":\"Coffee\",\"isInventory\":true,\"amount\":15,\"inventory\":true" ) );
        assertTrue( invent.contains( "\"name\":\"Milk\",\"isInventory\":true,\"amount\":17,\"inventory\":true" ) );

    }

}
