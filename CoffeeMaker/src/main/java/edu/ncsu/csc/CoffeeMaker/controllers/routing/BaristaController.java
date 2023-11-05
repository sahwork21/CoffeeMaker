package edu.ncsu.csc.CoffeeMaker.controllers.routing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Controller class for the URL mappings for CoffeeMaker's Barista role. The controller returns
 * the approprate HTML page in the /src/main/resources/templates/barista/* folder.
 *
 * @author Julian Madrigal
 */
@Controller
public class BaristaController {
    /**
     * On a GET request to /, the BaristaController will return
     * /src/main/resources/templates/barista/barista.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/barista" } )
    public String index ( final Model model ) {
        return "barista/barista";
    }
    
    /**
     * On a GET request to /customer/barista.js, the BaristaController will return
     * /src/main/resources/templates/barista/barista.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/barista.js" } )
    public String customerJS( final Model model ) {
        return "barista/barista.js";
    }
}
