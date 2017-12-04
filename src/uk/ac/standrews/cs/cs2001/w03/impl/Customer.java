package uk.ac.standrews.cs.cs2001.w03.impl;

import uk.ac.standrews.cs.cs2001.w03.common.NotEnoughMoneyException;
import uk.ac.standrews.cs.cs2001.w03.common.OrderAlreadyCompleteException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.interfaces.ICustomer;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IOrder;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;

import java.util.ArrayList;

/**
 * This class represents a customer who can place orders to different shops.
 *
 * @author 150009974
 * @version 1.0
 */
public class Customer implements ICustomer {

    /**
     * The default amount of money each customer starts with.
     * This amount is also assigned when an invalid amount is passed to
     * {@link Customer#setMoney(int)}.
     */
    public static final int DEFAULT_MONEY = 100;

    /**
     * Container for the orders made by a customer.
     */
    private ArrayList<IOrder> orders;

    /**
     * The current amount of money the customer has.
     */
    private int money;

    @Override
    public void setMoney(int money) {

        if (money < 0) {
            this.money = DEFAULT_MONEY;
        }
        else {
            this.money = money;
        }

    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public int getTotalNumberOfOrders() {
        return orders.size();
    }

    @Override
    public int getNumberOfCompletedOrders() {

        int completedOrders = 0;

        for (IOrder order : orders) {
            if (order.isComplete()) {
                completedOrders++;
            }
        }

        return completedOrders;

    }

    @Override
    public int getNumberOfIncompleteOrders() {

        int incompleteOrders = 0;

        for (IOrder order : orders) {
            if (!order.isComplete()) {
                incompleteOrders++;
            }
        }

        return incompleteOrders;

    }

    @Override
    public IOrder createOrder(IShop shop) {

        IOrder order = new Order(shop);
        orders.add(order);

        return order;

    }

    @Override
    public IOrder getOrder(int index) throws IndexOutOfBoundsException {
        return orders.get(index);
    }

    @Override
    public void completeOrder(int index) throws StockUnavailableException, ProductNotRegisteredException,
            IndexOutOfBoundsException, OrderAlreadyCompleteException, NotEnoughMoneyException {

        IOrder order = orders.get(index);

        if (money < order.getTotalOrderCost()) {
            throw new NotEnoughMoneyException();
        }

        money -= order.getTotalOrderCost();
        order.complete();

    }

    Customer() {

        orders = new ArrayList<>();
        money = DEFAULT_MONEY;

    }
}
