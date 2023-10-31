package edu.ncsu.csc.CoffeeMaker.controllers.routing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker's Customer role. The controller returns
 * the approprate HTML page in the /src/main/resources/templates/(customer)? folder. For a
 *
 * @author Julian Madrigal
 */
@Controller
public class CustomerController {
    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/customer/customer.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer" } )
    public String index ( final Model model ) {
        return "customer/customer";
    }
    
    /**
     * On a GET request to /customer/customer.js, the CustomerController will return
     * /src/main/resources/templates/customer/customer.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer.js" } )
    public String customerJS( final Model model ) {
        return "customer/customer.js";
    }
    
    /**
     * On a GET request to /order with the Customer Role, the IndexController will return
     * /src/main/resources/templates/customer/order/order.html
     * @param model underlying UI model
     * @return contents of page
     */
    @GetMapping ( { "/customer/order" } )
    public String order( final Model model ) {
        return "customer/order/order";
    }
    
    
    /**
     * On a GET request to /customer/customer.js, the MakeCoffeeController will return
     * /src/main/resources/templates/customer/customer.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/order.js" } )
    public String orderJS( final Model model ) {
        return "customer/order/order.js";
    }
}