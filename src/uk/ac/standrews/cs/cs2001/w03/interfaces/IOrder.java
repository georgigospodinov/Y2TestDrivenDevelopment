package uk.ac.standrews.cs.cs2001.w03.interfaces;

import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.OrderAlreadyCompleteException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;

/**
 * Interface for an order ADT.
 *
 * @author 150009974
 * @version 2.3
 */
public interface IOrder {

    /**
     * Adds an item to the order. The bar code parameter should match the bar code of a product in the shop,
     * with which the order is associated.
     *
     * @param barCode the bar code of the product to add
     * @throws ProductNotRegisteredException when there is no such product in the shop
     * @throws BarCodeAlreadyInUseException  when the product has already been added to the order
     * @throws OrderAlreadyCompleteException when the order is completed and cannot be amended
     */
    void addItem(String barCode)
            throws ProductNotRegisteredException, BarCodeAlreadyInUseException, OrderAlreadyCompleteException;

    /**
     * Removes an item from the order.
     *
     * @param barCode the bar code of the product to remove
     * @return the removed {@link IProduct}
     * @throws ProductNotRegisteredException when there is no such product in the order
     * @throws OrderAlreadyCompleteException when the order is completed and cannot be amended
     */
    IProduct removeItem(String barCode)
            throws ProductNotRegisteredException, OrderAlreadyCompleteException;

    /**
     * Returns an item from the order.
     *
     * @param barCode the bar code of the product to get
     * @return the {@link IProduct}
     * @throws ProductNotRegisteredException when there is no such product in the order
     */
    IProduct getItem(String barCode) throws ProductNotRegisteredException;

    /**
     * Getter for the number of different products in the order.
     *
     * @return the number of different products
     */
    int getNumberOfItems();

    /**
     * Increases the quantity of a given item to be bought.
     *
     * @param barCode the bar code of the item of which the quantity should be increased
     * @throws ProductNotRegisteredException when there is no such item in the order
     * @throws StockUnavailableException     when there is no more stock in the shop to take
     * @throws OrderAlreadyCompleteException when the order is completed and cannot be amended
     */
    void increaseQuantityOf(String barCode)
            throws ProductNotRegisteredException, StockUnavailableException, OrderAlreadyCompleteException;

    /**
     * Decreases the quantity of a given item to be bought.
     *
     * @param barCode the bar code of the item of which the quantity should be decreased
     * @throws ProductNotRegisteredException when there is no such item in the order
     * @throws StockUnavailableException     when there is no more stock to decrease
     * @throws OrderAlreadyCompleteException when the order is completed and cannot be amended
     */
    void decreaseQuantityOf(String barCode)
            throws ProductNotRegisteredException, StockUnavailableException, OrderAlreadyCompleteException;

    /**
     * Returns the quantity of a given item to be bought.
     *
     * @param barCode the bar code of the item of which the quantity should be retrieved
     * @return the quantity of the specified item
     * @throws ProductNotRegisteredException when there is no such item in the order
     */
    int getQuantityOf(String barCode) throws ProductNotRegisteredException;

    /**
     * Calculate and return the total amount of stock in the order.
     * Takes into account multiples of the same product.
     *
     * @return the total stock count
     */
    int getTotalQuantity();

    /**
     * Calculates and returns the cost of a particular item in the order.
     * Takes into account multiples of that product.
     *
     * @param barCode the bar code of the item
     * @return the calculated cost
     * @throws ProductNotRegisteredException when there is no product with the specified bar code
     */
    int getCostOf(String barCode) throws ProductNotRegisteredException;

    /**
     * Calculate and return the total cost of all the stock in the order.
     *
     * @return the total cost of the order
     */
    int getTotalOrderCost();

    /**
     * Completes the order, making it impossible to amend it.
     *
     * @throws StockUnavailableException     if the stock available in the shop
     *                                       has dropped below the amount of stock in the order
     * @throws ProductNotRegisteredException if a product in the order
     *                                       has been removed from the shop
     * @throws OrderAlreadyCompleteException if the order ha already been completed
     */
    void complete() throws StockUnavailableException, ProductNotRegisteredException, OrderAlreadyCompleteException;

    /**
     * Getter for whether the order is complete or not.
     * A complete order is paid for and no more products can be added to it.
     *
     * @return true if the order is paid for, false otherwise
     */
    boolean isComplete();

    /**
     * Getter for the shop to which this order is associated.
     *
     * @return the associated shop
     */
    IShop getShop();

}
