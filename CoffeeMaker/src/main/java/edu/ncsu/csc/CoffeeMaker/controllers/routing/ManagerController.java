package edu.ncsu.csc.CoffeeMaker.controllers.routing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker's Manager role. The
 * controller returns the approprate HTML page in the
 * /src/main/resources/templates/manager/* folder. For a
 *
 * @author Julian Madrigal
 */
@Controller
public class ManagerController {
    /**
     * On a GET request to /, the ManagerController will return
     * /src/main/resources/templates/manager/manager.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manager", "/manager.html" } )
    public String index ( final Model model ) {
        return "manager/manager";
    }

    /**
     * On a GET request to /manager/manager.js, the ManagerController will
     * return /src/main/resources/templates/manager/manager.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manager.js" } )
    public String customerJS ( final Model model ) {
        return "manager/manager.js";
    }

    /**
     * On a GET request to /manageBaristas, the ManagerController will return
     * /src/main/resources/templates/manager/manageBaristas/manageBaristas.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manageBaristas", "/manageBaristas" } )
    public String manageBaristasHTML ( final Model model ) {
        return "manager/manageBaristas/manageBaristas.html";
    }

    /**
     * On a GET request to /manageBaristas.js, the ManagerController will return
     * /src/main/resources/templates/manager/manageBaristas/manageBaristas.js.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manageBaristas.js" } )
    public String manageBaristasJS ( final Model model ) {
        return "manager/manageBaristas/manageBaristas.js";
    }
    
    /**
     * On a GET request to /editRecipe, the ManagerController will return
     * /src/main/resources/templates/manager/editRecipe/editRecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeHTML ( final Model model ) {
        return "manager/editRecipe/editRecipe.html";
    }

    /**
     * On a GET request to /editRecipe.js, the ManagerController will return
     * /src/main/resources/templates/manager/editRecipe/editRecipe.js.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editRecipe.js" } )
    public String editRecipeJS ( final Model model ) {
        return "manager/editRecipe/editRecipe.js";
    }
    
    /**
     * On a GET request to /deleteRecipe, the ManagerController will return
     * /src/main/resources/templates/manager/deleteRecipe/deleteRecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleteRecipe", "/deleteRecipe.html" } )
    public String deleteRecipeHTML ( final Model model ) {
        return "manager/deleteRecipe/deleteRecipe.html";
    }

    /**
     * On a GET request to /deleteRecipe.js, the ManagerController will return
     * /src/main/resources/templates/manager/deleteRecipe/deleteRecipe.js.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleteRecipe.js" } )
    public String deleteRecipeJS ( final Model model ) {
        return "manager/deleteRecipe/deleteRecipe.js";
    }
    
    /**
     * On a GET request to /addRecipe, the ManagerController will return
     * /src/main/resources/templates/manager/addRecipe/addRecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addrecipe", "/addRecipe", "/addRecipe.html" } )
    public String addRecipeHTML ( final Model model ) {
        return "manager/addRecipe/addRecipe.html";
    }

    /**
     * On a GET request to /addRecipe.js, the ManagerController will return
     * /src/main/resources/templates/manager/addRecipe/addRecipe.js.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addRecipe.js" } )
    public String addRecipeJS ( final Model model ) {
        return "manager/addRecipe/addRecipe.js";
    }
    
    /**
     * On a GET request to /addIngredient, the ManagerController will return
     * /src/main/resources/templates/manager/addIngredient/addIngredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addingredient", "/addIngredient", "/addIngredient.html" } )
    public String addIngredientHTML ( final Model model ) {
        return "manager/addIngredient/addIngredient.html";
    }

    /**
     * On a GET request to /addIngredient.js, the ManagerController will return
     * /src/main/resources/templates/manager/addIngredient/addIngredient.js.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addIngredient.js" } )
    public String addIngredientJS ( final Model model ) {
        return "manager/addIngredient/addIngredient.js";
    }
    
    
    /**
     * On a GET request to /addInventory, the ManagerController will return
     * /src/main/resources/templates/manager/addInventory/addInventory.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addinventory", "/addInventory", "/addInventory.html" } )
    public String addInventoryHTML ( final Model model ) {
        return "manager/addInventory/addInventory.html";
    }

    /**
     * On a GET request to /addIngredient.js, the ManagerController will return
     * /src/main/resources/templates/manager/addIngredient/addIngredient.js.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addInventory.js" } )
    public String addInventoryJS ( final Model model ) {
        return "manager/addInventory/addInventory.js";
    }

}
