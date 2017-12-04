package uk.ac.standrews.cs.cs2001.w03.impl;

import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.OrderAlreadyCompleteException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IOrder;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IStockRecord;
import uk.ac.standrews.cs.cs2001.w03.interfaces.ICustomer;

import java.util.LinkedList;

/**
 * This class represents an {@link IOrder} made by a {@link ICustomer} to a specific {@link IShop}.
 *
 * @author 150009974
 * @version 1.0
 */
public class Order implements IOrder {

    /**
     * The {@link IShop} with which this order is associated.
     */
    private IShop shop;

    /**
     * Whether or not the order is complete.
     */
    private boolean complete;

    /**
     * The container for the items in the order.
     */
    private LinkedList<IStockRecord> items;

    @Override
    public void addItem(String barCode) throws ProductNotRegisteredException, BarCodeAlreadyInUseException, OrderAlreadyCompleteException {

        if (complete) {
            throw new OrderAlreadyCompleteException();
        }

        for (IStockRecord item : items) {
            if (item.getProduct().getBarCode().equals(barCode)) {
                throw new BarCodeAlreadyInUseException();
            }
        }

        IStockRecord record = Factory.getInstance().makeStockRecord(shop.getProduct(barCode));
        record.setPrice(shop.getPriceOf(barCode)); //this is the line that can throw ProductNotRegisteredException
        items.add(record);

    }

    @Override
    public IProduct removeItem(String barCode) throws ProductNotRegisteredException, OrderAlreadyCompleteException {

        if (complete) {
            throw new OrderAlreadyCompleteException();
        }

        for (IStockRecord item : items) {
            IProduct product = item.getProduct();
            if (product.getBarCode().equals(barCode)) {
                items.remove(item);
                return product;
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public IProduct getItem(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord item : items) {
            IProduct product = item.getProduct();
            if (product.getBarCode().equals(barCode)) {
                return product;
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getNumberOfItems() {
        return items.size();
    }

    @Override
    public void increaseQuantityOf(String barCode) throws ProductNotRegisteredException, StockUnavailableException, OrderAlreadyCompleteException {

        if (complete) {
            throw new OrderAlreadyCompleteException();
        }

        for (IStockRecord item : items) {
            if (item.getProduct().getBarCode().equals(barCode)) {

                if (item.getStockCount() < shop.getStockCount(barCode)) {
                    item.addStock();
                    return;
                }
                else {
                    throw new StockUnavailableException();
                }

            }
        }

        throw new ProductNotRegisteredException();
    }

    @Override
    public void decreaseQuantityOf(String barCode) throws ProductNotRegisteredException, StockUnavailableException, OrderAlreadyCompleteException {

        if (complete) {
            throw new OrderAlreadyCompleteException();
        }

        for (IStockRecord item : items) {
            if (item.getProduct().getBarCode().equals(barCode)) {

                if (item.getStockCount() > 0) {
                    item.buyProduct();
                    return;
                }
                else {
                    throw new StockUnavailableException();
                }

            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getQuantityOf(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord item : items) {
            if (item.getProduct().getBarCode().equals(barCode)) {
                return item.getStockCount();
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getTotalQuantity() {

        int quantity = 0;

        for (IStockRecord item : items) {
            quantity += item.getStockCount();
        }

        return quantity;

    }

    @Override
    public int getCostOf(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord item : items) {
            if (item.getProduct().getBarCode().equals(barCode)) {
                return (item.getPrice() * item.getStockCount());
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getTotalOrderCost() {

        int cost = 0;

        for (IStockRecord item : items) {
            cost += (item.getPrice() * item.getStockCount());
        }

        return cost;

    }

    @Override
    public void complete() throws StockUnavailableException, ProductNotRegisteredException, OrderAlreadyCompleteException {

        if (complete) {
            throw new OrderAlreadyCompleteException();
        }

        //Check that there is enough stock for all items in the order.
        for (IStockRecord item : items) {
            //If a product is missing, the getStockCount() method will throw the exception
            if (item.getStockCount() > shop.getStockCount(item.getProduct().getBarCode())) {
                throw new StockUnavailableException();
            }
        }

        for (IStockRecord item : items) {
            String barCode = item.getProduct().getBarCode();
            for (int i = 0; i < item.getStockCount(); i++) {
                shop.buyProduct(barCode);
            }
        }

        complete = true;

    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public IShop getShop() {
        return shop;
    }

    Order(IShop shop) {

        if (shop == null) {
            throw new NullPointerException("Constructor argument shop of type IShop should not be null!");
        }

        this.shop = shop;

        items = new LinkedList<>();
        complete = false;
    }
}
