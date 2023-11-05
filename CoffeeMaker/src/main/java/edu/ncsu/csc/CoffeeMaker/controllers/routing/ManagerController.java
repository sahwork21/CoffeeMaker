package edu.ncsu.csc.CoffeeMaker.controllers.routing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker's Manager role. The controller returns
 * the approprate HTML page in the /src/main/resources/templates/manager/* folder. For a
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
    @GetMapping ( { "/manager" } )
    public String index ( final Model model ) {
        return "manager/manager";
    }
    
    /**
     * On a GET request to /manager/manager.js, the ManagerController will return
     * /src/main/resources/templates/manager/manager.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manager.js" } )
    public String customerJS( final Model model ) {
        return "manager/manager.js";
    }

}
