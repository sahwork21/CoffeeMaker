package edu.ncsu.csc.CoffeeMaker.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonManagedReference
    @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Customer   customer;
    /**
     * The associated Recipe that this order contains. Will be used to update
     * the inventory and check prices. This is just the recipe name that is
     * looked up.
     */
    private String     recipe;
    // private Date date;
    /**
     * The state of the order as an FSM. The order will be made by a Customer
     * and start in unfulfilled. Then it will be fulfilled by a staff and go to
     * ready to pickup. Finally the customer will pickup the order and it will
     * become history.
     */
    private OrderState status;

    /**
     * This is the amount of payment the user input into the object
     */
    private int        payment;

    /**
     * The time the order was placed
     */
    private String     placedAt;
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
     * @param payment
     *            the amount the user input for paying
     *
     */
    public CustomerRequest ( final Customer customer, final String recipe, final int payment ) {
        // this.id = id;
        this.customer = customer;
        this.recipe = recipe;
        // this.date = date;
        this.payment = payment;

        setPlacedAt();

        this.status = OrderState.UNFULFILLED;
    }

    /**
     * Get the string of the time of order placement
     *
     * @return placedAt the time this order was placed
     */
    public String getPlacedAt () {
        return placedAt;
    }

    /**
     * Set the time string of this order on creation. Does this automatically
     * with the Date time.
     *
     *
     */
    public void setPlacedAt () {
        // Create a timestamp in HH:mm a format
        final LocalDateTime myDateObj = LocalDateTime.now();

        final DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern( "HH:mm a" );

        final String formattedDate = myDateObj.format( myFormatObj );

        placedAt = formattedDate;
    }

    /**
     * Empty constructor to appease Spring Remove the body of this method if
     * something goes wrong
     */
    public CustomerRequest () {

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
        this.status = status;
    }

    /**
     * Return the payment amount of this order
     *
     * @return the amount paid
     */
    public int getPayment () {
        return payment;
    }

    /**
     * Set the amount of money that was paid
     *
     * @param payment
     *            the amount paid
     * @throws IllegalArgumentException
     *             throws when payment is negative
     */
    public void setPayment ( final int payment ) {
        if ( payment < 0 ) {
            throw new IllegalArgumentException( "Payment must be positive" );
        }
        this.payment = payment;
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
    public String getRecipe () {
        return recipe;
    }

    /**
     * Set the recipe that this order will use
     *
     * @param recipe
     *            the recipe that will be made and linked to this order
     */
    public void setRecipe ( final String recipe ) {
        this.recipe = recipe;
    }

    /*
     * Check that this object has all the needed fields
     */
}
