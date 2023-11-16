package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Order {

    /**
     * Id for the order
     */
    @Id
    @GeneratedValue
    private Long     id;
    /**
     * customer
     */
    @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Customer customer;
    /**
     * recipe
     */
    @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Recipe   recipe;
    private Date     date;
    private String   status;
    // private Long orderNumber;

    // Constructor
    public Order ( final Long id, final Customer customer, final Recipe recipe, final Date date, final String status ) {
        this.id = id;
        this.customer = customer;
        this.recipe = recipe;
        this.date = date;
        this.status = status;
    }

    // Empty constructor.
    public Order () {

    }

    // Getters and Setters
    public Long getId () {
        return id;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public Customer getCustomer () {
        return customer;
    }

    public void setCustomer ( final Customer customer ) {
        this.customer = customer;
    }

    public Recipe getRecipe () {
        return recipe;
    }

    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
    }

    public Date getDate () {
        return date;
    }

    public void setDate ( final Date date ) {
        this.date = date;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus ( final String status ) {
        this.status = status;
    }
}
