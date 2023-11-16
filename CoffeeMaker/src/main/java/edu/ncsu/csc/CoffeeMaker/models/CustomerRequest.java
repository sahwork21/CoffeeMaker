package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CustomerRequest extends DomainObject {

    /**
     * Id for the order
     */
    @Id
    @GeneratedValue
    private Long   id;
    /**
     * customer
     */
    // @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // private Customer customer;
    /**
     * recipe
     */
    // @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // private Recipe recipe;
    private Date   date;
    private String status;
    // private Long orderNumber;

    // Constructor
    public CustomerRequest ( final Long id, final Customer customer, final Recipe recipe, final Date date,
            final String status ) {
        this.id = id;
        // this.customer = customer;
        // this.recipe = recipe;
        this.date = date;
        this.status = status;
    }

    // Empty constructor.
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
