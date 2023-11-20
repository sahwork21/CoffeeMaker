package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import edu.ncsu.csc.CoffeeMaker.models.enums.OrderState;

/**
 * The CustomerRequest object is how we will manage the user orders. The name
 * had to change to fix sql query errors. This class will have an associated
 * Recipe and Customer while it lives in the system. It will move from state to
 * state of unfulfilled->ready to pickup->history until completion of the order
 */
@Entity
public class CustomerRequest extends DomainObject {

    /**
     * Id for the order. Will also be used as an numbering scheme in the
     * frontend.
     */
    @Id
    @GeneratedValue
    private Long       id;
    /**
     * The associated Customer that ordered this recipe
     */
    @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Customer   customer;
    /**
     * The associated Recipe that this order contains. Will be used to update
     * the inventory and check prices.
     */
    @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private Recipe     recipe;
    // private Date date;
    /**
     * The state of the order as an FSM. The order will be made by a Customer
     * and start in unfulfilled. Then it will be fulfilled by a staff and go to
     * ready to pickup. Finally the customer will pickup the order and it will
     * become history.
     */
    private OrderState status;
    // private Long orderNumber;

    /**
     * Parametrized constructor for the orders that a customer will make. The
     * orders will start in unfulfilled then move from state to state using the
     * setter method.
     *
     * @param customer
     *            the associated customer for this object. The person who
     *            ordered
     * @param recipe
     *            the associated recipe for this object. The recipe that will be
     *            made on fulfills
     *
     */
    public CustomerRequest ( final Customer customer, final Recipe recipe ) {
        // this.id = id;
        this.customer = customer;
        this.recipe = recipe;
        // this.date = date;

        this.status = OrderState.UNFULFILLED;
    }

    /**
     * Empty constructor to appease Spring Remove the body of this method if
     * something goes wrong
     */
    public CustomerRequest () {
        this.status = OrderState.UNFULFILLED;
    }

    // Getters and Setters
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Spring method to set id values
     *
     * @param id
     *            the value to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    // public Date getDate () {
    // return date;
    // }
    //
    // public void setDate ( final Date date ) {
    // this.date = date;
    // }

    /**
     * Get the state of the order
     *
     * @return the status of the order
     */
    public OrderState getStatus () {
        return status;
    }

    /**
     * Set the status of the order. Should move like an FSM.
     * UNFULFILLED->READY_TO_PICKUP->HISTORY
     *
     * @param status
     *            the state we want to be in for this order
     */
    public void setStatus ( final OrderState status ) {
        // Add some checks for moving from state to state.
        if ( getStatus() == OrderState.UNFULFILLED ) {
            if ( status == OrderState.READY_TO_PICKUP ) {
                this.status = status;
            }
            else {
                throw new IllegalStateException(
                        "Status must move to \"Ready to Pickup\" if currently in \"Unfulfilled\"." );
            }
        }
        else if ( getStatus() == OrderState.READY_TO_PICKUP ) {
            if ( status == OrderState.HISTORY ) {
                this.status = status;
            }
            else {
                throw new IllegalStateException(
                        "Status must move to \"History\" if currently in \"Ready to Pickup\"." );
            }
        }
        else {
            throw new IllegalStateException( "Status can not be changed if currently in \"History\"." );
        }
    }

    /**
     * Get the customer that owns this order
     *
     * @return the customer object that owns this object
     */
    public Customer getCustomer () {
        return customer;
    }

    /**
     * Set the customer who owns this object
     *
     * @param customer
     *            the customer who owns this object
     */
    public void setCustomer ( final Customer customer ) {
        this.customer = customer;
    }

    /**
     * Get the recipe that this order will make
     *
     * @return recipe that will be made during this order's fulfillment
     */
    public Recipe getRecipe () {
        return recipe;
    }

    /**
     * Set the recipe that this order will use
     *
     * @param recipe
     *            the recipe that will be made and linked to this order
     */
    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
    }

    /*
     * Check that this object has all the needed fields
     */
}
