package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.AbstractUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Manager;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.ManagerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the API controller for the REST endpoints that we want the users to
 * access.
 *
 * We want to limit these operations to just log users in, signup users, and
 * display baristas for a manager caller.
 *
 * Returned JSONs should not contain passwords.
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIUserController extends APIController {

    /**
     * The service class for AbstractUser types. This will be used for logins
     * and user creation since the pool of usernames is shared by all users.
     */
    @Autowired
    private UserService<AbstractUser> userService;
    /**
     * The service class for getting and saving a new Customer
     */
    @Autowired
    private CustomerService           customerService;
    /**
     * The service class for getting and saving a new Staff member
     */
    @Autowired
    private StaffService              staffService;
    /**
     * The service class for getting and saving a new Manager.
     */
    @Autowired
    private ManagerService            managerService;

    /**
     * Endpoint that creates a new user with a unique username. We only want to
     * look at the information of usernames that the AbstractUser encapsulates
     * not orders or anything else.
     *
     * @param user
     *            the object we want to create
     * @return a response entity of the status. 200 if the user was successfully
     *         created, 400 for empty fields.
     */

    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final AbstractUser user ) {
        // Check that the user is valid before looking for persistence objects
        // Use this flag so people cannot time up if users do or do not exist
        boolean valid = true;
        if ( !user.checkUser() ) {
            valid = false;

        }

        final AbstractUser u = userService.findByUsername( user.getUsername() );

        // If no user is found with the associated username then we can make a
        // new user

        // This is not a new user give them a 400 error. We should probably be
        // obscuring these so people cannot snoop in on conversations
        // We also use this valid flag so people cannot time up if users do or
        // do not exist
        if ( !valid || u != null ) {
            return new ResponseEntity( errorResponse( "Invalid username or password" ), HttpStatus.BAD_REQUEST );
        }

        // The user is valid so save them with the correct information after
        // reconstructing them. This will affect the data type and the extra
        // fields and accesses for subclasses.

        // Constructors to create these new users

        if ( user.getRoleType() == Role.BARISTA ) {
            final Staff insertedUser = new Staff( user.getUsername(), user.getPassword(), user.getRoleType() );
            staffService.create( insertedUser );
            return new ResponseEntity(
                    successResponse( user.getUsername() + " staff account was successfully created" ), HttpStatus.OK );
        }
        else if ( user.getRoleType() == Role.MANAGER ) {
            final Manager insertedUser = new Manager( user.getUsername(), user.getPassword() );
            managerService.create( insertedUser );
            return new ResponseEntity(
                    successResponse( user.getUsername() + " manager account was successfully created" ),
                    HttpStatus.OK );
        }
        else if ( user.getRoleType() == Role.CUSTOMER ) {
            // Create a Customer as a last resort since this isn't a manager or
            // staff type
            final Customer insertedUser = new Customer( user.getUsername(), user.getPassword() );
            customerService.create( insertedUser );
            return new ResponseEntity(
                    successResponse( user.getUsername() + " customer account was successfully created" ),
                    HttpStatus.OK );
        }

        // This response better not be reached ever
        return new ResponseEntity( errorResponse( "Could not create user" ), HttpStatus.INTERNAL_SERVER_ERROR );

    }

    /**
     * Mapping to get a user for login. We have to use the the validLogin method
     * to check if the encrypted password matches the input raw password. Also
     * we have to verify the user is not cheating by entering in wrong
     * usernames.
     *
     * @param username
     *            the username of the user we are looking for.
     * @param password
     *            the unencrypted password of the user we are looking for.
     * @return a response entity of 4XX on incorrect usernames or passwords. 200
     *         and the JSON of user on successful logins
     */
    @PostMapping ( BASE_PATH + "/users/login/{username}" )
    public ResponseEntity loginUser ( @PathVariable ( "username" ) final String username,
            @RequestBody final String password ) {
        final AbstractUser user = userService.findByUsername( username );

        // If the user is null send back a 400 error since there is no
        // associated account

        // If the username input has incorrect credentials send back a bad
        // request
        if ( user == null || !user.checkPassword( password ) ) {
            return new ResponseEntity( errorResponse( "Invalid username or password" ), HttpStatus.BAD_REQUEST );
        }

        // Now that the passwords match we need to get the more specific object
        // returned. This is because customers may have lists of orders to
        // display.

        // The username and password match so send back the whole user as a JSON
        // Make sure to set the password to the empty string on the returned
        // object so secrets don't leak.
        // Also cast to the correct type so we can see encapsulated elements
        if ( user.getRoleType() == Role.BARISTA ) {
            final Staff ret = staffService.findByUsername( username );
            ret.setPassword( "" );
            return new ResponseEntity( ret, HttpStatus.OK );
        }
        else if ( user.getRoleType() == Role.MANAGER ) {
            final Manager ret = managerService.findByUsername( username );
            ret.setPassword( "" );
            return new ResponseEntity( ret, HttpStatus.OK );
        }
        else if ( user.getRoleType() == Role.CUSTOMER ) {
            final Customer ret = customerService.findByUsername( username );
            ret.setPassword( "" );
            return new ResponseEntity( ret, HttpStatus.OK );
        }

        // This response better not be reached ever
        return new ResponseEntity( errorResponse( "Could not return user class" ), HttpStatus.INTERNAL_SERVER_ERROR );

    }

    /**
     * Return a list of users with the barista role type This will be used by
     * managers to create new barista accounts. It should not matter that they
     * get casted to Staff since they only display usernames.
     *
     * @return a list of Staff with the role type of barista
     */
    @GetMapping ( BASE_PATH + "/users/barista" )
    public List<AbstractUser> getBaristas () {
        // Obscure passwords before returning

        // Find all the users that are baristas and return them
        final List<AbstractUser> users = userService.findByRoleType( Role.BARISTA );
        for ( final AbstractUser u : users ) {
            u.setPassword( "" );

        }
        return users;
    }

    /**
     * Create a manager, barista,and customer. This should only be used for the
     * demo to make generating users easier.
     *
     * @return an ok response since all the users were created or already exist
     */
    @PostMapping ( BASE_PATH + "/generateusers" )
    public ResponseEntity generateDemoUsers () {

        // Make sure there already isn't a user with matching name
        if ( customerService.findByUsername( "Customer" ) == null ) {
            customerService.create( new Customer( "Customer", "Customer" ) );
        }

        if ( staffService.findByUsername( "Barista" ) == null ) {
            staffService.create( new Staff( "Barista", "Barista", Role.BARISTA ) );
        }

        if ( managerService.findByUsername( "Manager" ) == null ) {
            managerService.create( new Manager( "Manager", "Manager" ) );
        }
        return new ResponseEntity( successResponse( "Demo users made" ), HttpStatus.OK );
    }

    // /**
    // * Login info containing the raw password and username
    // */
    // private class LoginInfo {
    // /**
    // * username of the login
    // */
    // private String username;
    //
    // /**
    // * password of the login
    // */
    // private String password;
    //
    // /**
    // * Get the username of the login form
    // *
    // * @return username of login info
    // */
    // public String getUsername () {
    // return username;
    // }
    //
    // /**
    // * Set the username
    // *
    // * @param username
    // * of the form
    // */
    // public void setUsername ( final String username ) {
    // this.username = username;
    // }
    //
    // /**
    // * Return the password of the form
    // *
    // * @return password of the form
    // */
    // public String getPassword () {
    // return password;
    // }
    //
    // /**
    // * Set the password
    // *
    // * @param password
    // */
    // public void setPassword ( final String password ) {
    // this.password = password;
    // }
    // }

}
