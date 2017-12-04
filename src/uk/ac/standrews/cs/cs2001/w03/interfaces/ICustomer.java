package uk.ac.standrews.cs.cs2001.w03.interfaces;

import uk.ac.standrews.cs.cs2001.w03.common.NotEnoughMoneyException;
import uk.ac.standrews.cs.cs2001.w03.common.OrderAlreadyCompleteException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;

/**
 * Interface for a customer ADT.
 */
public interface ICustomer {

    /**
     * Sets the amount of money the customer has.
     *
     * @param money the amount of money to be set
     */
    void setMoney(int money);

    /**
     * Getter for the amount of money the customer has at disposal.
     *
     * @return the amount of money
     */
    int getMoney();

    /**
     * Returns the total number of orders the customer has ever made.
     *
     * @return total number of orders
     */
    int getTotalNumberOfOrders();

    /**
     * Returns the number of completed orders.
     *
     * @return number of completed orders
     */
    int getNumberOfCompletedOrders();

    /**
     * Returns the number of incomplete orders.
     *
     * @return number of incomplete orders
     */
    int getNumberOfIncompleteOrders();

    /**
     * Creates a new order associated with a shop.
     *
     * @param shop the shop with which the order is associated
     * @return the order created.
     */
    IOrder createOrder(IShop shop);

    /**
     * Accesses the inner {@link IOrder} container and retrieves object at the specified index.
     *
     * @param index index of the {@link IOrder} to be retrieved
     * @return the {@link IOrder} at that position
     * @throws IndexOutOfBoundsException when there is no order at the specified index
     */
    IOrder getOrder(int index) throws IndexOutOfBoundsException;

    /**
     * Completes the {@link IOrder} at the specified index.
     * This includes and is limited to:
     * 0. Checking that all the products are still available,
     *          i.e. no one else has bought the last jar of pickles that has been added to both our orders.
     * 1. Subtracting the total cost of the order from the customers amount of money.
     * 2. Reducing the stock count for each product appropriately.
     * 3. Increasing the corresponding shop's revenue.
     * 4. Marking the order as complete.
     *
     * @param index index of the {@link IOrder} to be completed
     * @throws StockUnavailableException when the stock at the shop has dropped below the amount for purchasing
     * @throws ProductNotRegisteredException when a product has been removed from the shop before completing the order
     * @throws IndexOutOfBoundsException when there is no order at the index parameter
     * @throws OrderAlreadyCompleteException when the order is already complete
     * @throws NotEnoughMoneyException when the customer does not have enough money to complete the order
     */
    void completeOrder(int index) throws StockUnavailableException, ProductNotRegisteredException,
            IndexOutOfBoundsException, OrderAlreadyCompleteException, NotEnoughMoneyException;

}
