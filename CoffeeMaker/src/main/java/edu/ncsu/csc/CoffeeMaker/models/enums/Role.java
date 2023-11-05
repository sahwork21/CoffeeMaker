package edu.ncsu.csc.CoffeeMaker.models.enums;

/**
 * Used to track the RoleType of a user. Currently a user can be a customer,
 * manager, or barista. Role is used to help the frontend determine which pages
 * the user can access.
 */
public enum Role {
    /**
     * The enumerated types we use to restrict the types of users available
     */
    CUSTOMER, MANAGER, BARISTA;
}
