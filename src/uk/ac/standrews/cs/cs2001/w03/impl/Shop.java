package uk.ac.standrews.cs.cs2001.w03.impl;


import uk.ac.standrews.cs.cs2001.w03.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IStockRecord;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class represents a simple shop which can stock and sell products.
 */
public class Shop extends AbstractFactoryClient implements IShop {

    /**
     * A list of all {@link IStockRecord}-s in the shop.
     */
    private LinkedList<IStockRecord> records;

    /**
     * The total shop revenue from all sales.
     */
    private int revenue;

    @Override
    public void registerProduct(IProduct product) throws BarCodeAlreadyInUseException {

        IProduct existingProduct;
        for (IStockRecord record : records) {

            existingProduct = record.getProduct();
            if (existingProduct.equals(product)) {
                throw new BarCodeAlreadyInUseException();
            }

        }

        records.add(getFactory().makeStockRecord(product));
    }

    @Override
    public void unregisterProduct(IProduct product) throws ProductNotRegisteredException {

        if (product == null) {
            throw new ProductNotRegisteredException();
        }

        for (IStockRecord record : records) {
            if (record.getProduct().equals(product)) {
                records.remove(record);
                return;
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public void addStock(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord record : records) {
            if (record.getProduct().getBarCode().equals(barCode)) {
                record.addStock();
                return;
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public void buyProduct(String barCode) throws StockUnavailableException, ProductNotRegisteredException {

        for (IStockRecord record : records) {
            if (record.getProduct().getBarCode().equals(barCode)) {

                if (record.getStockCount() < 1) {
                    throw new StockUnavailableException();
                } else {
                    record.buyProduct();
                    revenue += record.getPrice();
                    return;
                }
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getNumberOfProducts() {
        return records.size();
    }

    @Override
    public int getTotalStockCount() {

        int totalStock = 0;

        for (IStockRecord record : records) {
            totalStock += record.getStockCount();
        }
        return totalStock;

    }

    @Override
    public int getStockCount(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord record : records) {
            if (record.getProduct().getBarCode().equals(barCode)) {
                return record.getStockCount();
            }
        }

        throw new ProductNotRegisteredException();
    }

    @Override
    public int getNumberOfSales(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord record : records) {
            if (record.getProduct().getBarCode().equals(barCode)) {
                return record.getNumberOfSales();
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public IProduct getMostPopular() throws ProductNotRegisteredException {

        IStockRecord popular;
        try {
            popular = records.getFirst();
        }
        catch (NoSuchElementException e) {
            throw new ProductNotRegisteredException();
        }

        for (IStockRecord record : records) {
            if (popular.getNumberOfSales() < record.getNumberOfSales()) {
                popular = record;
            }
        }

        return popular.getProduct();
    }

    @Override
    public IProduct getProduct(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord record : records) {
            IProduct product = record.getProduct();
            if (product.getBarCode().equals(barCode)) {
                return product;
            }
        }

        throw new ProductNotRegisteredException();
    }

    @Override
    public void setPriceOf(String barCode, int price) throws ProductNotRegisteredException {

        for (IStockRecord record : records) {
            if (record.getProduct().getBarCode().equals(barCode)) {
                record.setPrice(price);
                return;
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getPriceOf(String barCode) throws ProductNotRegisteredException {

        for (IStockRecord record : records) {
            if (record.getProduct().getBarCode().equals(barCode)) {
                return record.getPrice();
            }
        }

        throw new ProductNotRegisteredException();

    }

    @Override
    public int getRevenue() {
        return revenue;
    }

    /**
     * Creates a new {@link Shop} instance with an empty {@link Shop#records} field.
     */
    Shop() {

        records = new LinkedList<>();
        revenue = 0;

    }

}
