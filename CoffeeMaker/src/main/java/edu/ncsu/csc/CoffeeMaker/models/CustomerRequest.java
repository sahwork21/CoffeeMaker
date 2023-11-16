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
    // @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // private Recipe recipe;
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
    // public CustomerRequest ( final Customer customer, final Recipe recipe ) {
    // // this.id = id;
    // this.customer = customer;
    // this.recipe = recipe;
    // // this.date = date;
    //
    // this.status = OrderState.UNFULFILLED;
    // }

    /**
     * Empty constructor to appease Spring
     */
    public CustomerRequest () {

    }

    // Getters and Setters
    @Override
    public Long getId () {
        return id;
    }

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

    public OrderState getStatus () {
        return status;
    }

    public void setStatus ( final OrderState status ) {
        this.status = status;
    }
}
