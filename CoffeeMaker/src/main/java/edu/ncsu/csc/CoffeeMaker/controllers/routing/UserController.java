package edu.ncsu.csc.CoffeeMaker.controllers.routing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker's Users (no specific
 * role). The controller returns the approprate HTML page in the
 * /src/main/resources/templates/* folder. This includes signin to authenticate,
 * and signup to create an account.
 *
 * @author Julian Madrigal
 */
@Controller
public class UserController {
    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/login.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/", "/signin", "/signin.html" } )
    public String signIn ( final Model model ) {
        return "./signin/signin";
    }

    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/signup/signin.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/signin.js" } )
    public String signInJS ( final Model model ) {
        return "./signin/signin.js";
    }

    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/signup/signup.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/signup", "/signup.html" } )
    public String signUp ( final Model model ) {
        return "./signup/signup";
    }

    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/signup/signup.js
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/signup.js" } )
    public String signUpJS ( final Model model ) {
        return "./signup/signup.js";
    }

    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/login.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/privacypolicy", "/privacypolicy.html" } )
    public String privacyPolicy ( final Model model ) {
        return "./privacypolicy";
    }
}
