package edu.ncsu.csc.CoffeeMaker.models.enums;

/**
 * This class is used to track the type of order. An order can be unfulfilled,
 * ready_to_pickuo and History
 */
public enum OrderState {
    /**
     * The enumerated types we use to restrict the types of users available
     */
    UNFULFILLED, READY_TO_PICKUP, HISTORY;
}
