package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerRequest;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;
import edu.ncsu.csc.CoffeeMaker.services.CustomerRequestService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the API controller for orders that will be used to move orders from
 * state to state. It is also responsible for listing orders to the customers
 * and baristas
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APICustomerRequestController extends APIController {

    /**
     * Object to check that the recipe we are looking for in database exists.
     * Will be used to find recipes by name.
     */
    @Autowired
    private RecipeService          recipeService;

    /**
     * Object that manages CRUD operations for orders. Will display, create, and
     * update the orders
     */
    @Autowired
    private CustomerRequestService orderService;

    /**
     * Object tht manages CRUD operations for the users in the system. This will
     * make sure that the person ordering is a customer in the system.
     */
    @Autowired
    private CustomerService        customerService;

    /**
     * API operation to get the list of all orders. Returns a list object to
     * frontend caller that should always be successful
     *
     * @return returns a list of all the orders in the system
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<CustomerRequest> listOrders () {
        return orderService.findAll();
    }

    /**
     * API operation to get orders that are unfulfilled so the baristas can see
     * what needs to be done.
     *
     * @return a list of the orders in state unfulfilled
     */
    @GetMapping ( BASE_PATH + "/orders/unfulfilled" )
    public List<CustomerRequest> listUnfulfilledOrders () {
        return orderService.findByStatus( OrderState.UNFULFILLED );
    }

    /**
     * API operation to create an order. The frontend will pass in the username
     * of the customer who ordered, recipe name, and their payment. If they gave
     * us a valid recipe, valid username, and enough payment then an okay
     * response is given. The next API call should make the coffee after it gets
     * fulfilled.
     *
     * @param orderRequest
     *            object that contains the recipe, username, and payment for an
     *            order. Used to make JSON inputs easier
     * @return a response entity of 200 if the parameters are valid, 404 if the
     *         recipe or user are not found, and 400 if the payment is
     *         insufficient.
     */

    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final OrderRequest orderRequest ) {
        // Look up the customer in the database
        // This username will be saved and not require any relogin
        final Customer customer = customerService.findByUsername( orderRequest.getUsername() );
        if ( customer == null ) {
            // This username does not link to any customers
            return new ResponseEntity( errorResponse( "User not found" ), HttpStatus.NOT_FOUND );
        }

        final Recipe recipe = recipeService.findByName( orderRequest.getRecipeName() );
        if ( recipe == null ) {
            // The requested recipe doesn't exist so return a 404 error
            return new ResponseEntity( errorResponse( "Recipe not found" ), HttpStatus.NOT_FOUND );
        }

        if ( recipe.getPrice() > orderRequest.getPayment() ) {
            // They did not give enough money so return a 400 bad request error
            return new ResponseEntity( errorResponse( "Insufficient funds" ), HttpStatus.BAD_REQUEST );
        }

        // We can now create the order and add it to the customer's list of
        // orders
        final CustomerRequest req = new CustomerRequest();
        req.setCustomer( customer );
        req.setRecipe( recipe );
        req.setStatus( OrderState.UNFULFILLED );

        customer.addOrder( req );

        // Now save it
        orderService.save( req );
        // Calculate the change.
        final String change = "" + ( orderRequest.payment - recipe.getPrice() );
        return new ResponseEntity<String>( successResponse( change ), HttpStatus.OK );
    }

    // This request can also be by id if that is easier
    /**
     * API operation to fulfill an order. This will just move an order from
     * unfulfilled to ready for the customer to pickup. We will get an order in
     * the request body.
     *
     * @param request
     *            the order object the user has input
     * @return a confirmation that the order moved states
     */
    @PutMapping ( BASE_PATH + "/orders/fulfill" )
    public ResponseEntity fulfillOrder ( @RequestBody final CustomerRequest request ) {
        // Modify
        // the
        // request
        // body
        // to
        // just
        // the
        // id
        // if
        // needed

        // Go find this request in the database to make sure it exists
        final CustomerRequest req = orderService.findById( request.getId() );

        // Move the request from state to state if it is in unfulfilled
        // currently
        if ( req.getStatus() != OrderState.UNFULFILLED ) {
            return new ResponseEntity(
                    errorResponse( "Order not in right state to be fulfilled. Must be in unfulfilled" ),
                    HttpStatus.BAD_REQUEST );
        }

        // Move from state to state now and save
        req.setStatus( OrderState.READY_TO_PICKUP );
        orderService.save( req );

        return new ResponseEntity( successResponse( "Order fulfilled" ), HttpStatus.OK );
    }

    // This request can also be by id if that is easier. This operation is just
    // like the one above
    /**
     * API operation to fulfill an order. This will just move an order from
     * state to state. We will get an order in the request body. This will also
     * remove the order from the customer's list of orders
     *
     * @param request
     *            the order object the user has input
     * @return a confirmation that the order moved states
     */
    @PutMapping ( BASE_PATH + "/orders/pickup" )
    public ResponseEntity pickupOrder ( @RequestBody final CustomerRequest request ) {
        // Modify
        // the
        // request
        // body
        // to
        // just
        // the
        // id
        // if
        // needed

        // Go find this request in the database to make sure it exists
        final CustomerRequest req = orderService.findById( request.getId() );

        // Move the request from state to state if it is in unfulfilled
        // currently
        if ( req.getStatus() != OrderState.READY_TO_PICKUP ) {
            return new ResponseEntity(
                    errorResponse( "Order not in right state to be pickup. Must be in ready to pickup" ),
                    HttpStatus.BAD_REQUEST );
        }

        // Move from state to state now and save
        // Also remove this order from the customer's list
        final Customer customer = customerService.findByUsername( req.getCustomer().getUsername() );

        // Remove this get operation of cusotmer if it screws things up
        customer.removeOrder( req );
        customerService.save( customer );

        // Go to this if we saving customer twice causes issues
        // req.getCustomer().removeOrder( req );
        req.setStatus( OrderState.HISTORY );
        orderService.save( req );

        return new ResponseEntity( successResponse( "Order picked up" ), HttpStatus.OK );
    }

    /**
     * API operation to get the orders that have been completed. This will be
     * used for the order history to view all the orders in the history state.
     *
     * @return a list of the orders in the state HISTORY
     */
    @GetMapping ( BASE_PATH + "/orders/history" )
    public List<CustomerRequest> getOrdersHistory () {
        return orderService.findByStatus( OrderState.HISTORY );
    }

    /**
     * API operation to get the orders that the customer will have. Returns all
     * the orders for a customer if it exists
     *
     * @param username
     *            the username of the customer we are looking for
     * @return a list of the orders contained by a user
     */
    @GetMapping ( BASE_PATH + "/orders/{username}" )
    public ResponseEntity getCustomersOrders ( @PathVariable ( "username" ) final String username ) {
        final Customer customer = customerService.findByUsername( username );
        if ( customer == null ) {
            // We couldn't find the customer so return a 404 error
            return new ResponseEntity( errorResponse( "Customer not found. No orders can be displayed." ),
                    HttpStatus.NOT_FOUND );
        }

        // This customer exists, now return the list of orders.
        return new ResponseEntity<List<CustomerRequest>>( customer.getOrders(), HttpStatus.OK );

    }

    /**
     * operation to clear out our history of orders. Clear out all the orders
     * that are in the history status.
     *
     * @return response entity of 200 if the orders in the history state are
     *         cleared out.
     */
    @DeleteMapping ( BASE_PATH + "/orders/history" )
    public ResponseEntity deleteOrderHistory () {
        // Get the orders list and delete the ones with status of HISTORy
        orderService.deleteByStatus( OrderState.HISTORY );
        return new ResponseEntity( successResponse( "Successfully cleared order history" ), HttpStatus.OK );
    }

    /**
     * The OrderRequest class serves as a single object to store the data from
     * the frontend containing the order information.
     *
     * @author Brian Marks
     */
    private static class OrderRequest {
        /**
         * Represents the data for creating a new order.
         */
        private String username;

        /**
         * Represents the name of the recipe for the new order.
         */
        private String recipeName;

        /**
         * Represents the payment amount for the new order.
         */
        private int    payment;

        /**
         * Gets the username for the order.
         *
         * @return The username associated with the order.
         */
        public String getUsername () {
            return username;
        }

        /**
         * Gets the name of the recipe for the order.
         *
         * @return The name of the recipe associated with the order.
         */
        public String getRecipeName () {
            return recipeName;
        }

        /**
         * Gets the payment amount for the order.
         *
         * @return The payment amount associated with the order.
         */
        public int getPayment () {
            return payment;
        }

    }
}
