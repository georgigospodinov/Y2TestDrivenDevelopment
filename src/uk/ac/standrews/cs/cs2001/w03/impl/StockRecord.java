package uk.ac.standrews.cs.cs2001.w03.impl;

import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IStockRecord;

/**
 * This class represents a record held by a {@link Shop} for a particular product.
 */
public class StockRecord implements IStockRecord {

    /**
     * A default price assigned when a new {@link StockRecord} instance is created and
     * when an invalid price is attempted to be set.
     */
    public static final int DEFAULT_PRICE = 1;

    /**
     * The {@link IProduct} instance for which stock information is stored.
     */
    private IProduct product;

    /**
     * The price of the {@link StockRecord#product}.
     */
    private int price;

    /**
     * The amount of stock of this object's {@link StockRecord#product}.
     */
    private int stockCount;

    /**
     * The number of sales of this object's {@link StockRecord#product}.
     */
    private int numberOfSales;

    @Override
    public IProduct getProduct() {
        return product;
    }

    @Override
    public int getStockCount() {
        return stockCount;
    }

    @Override
    public int getNumberOfSales() {
        return numberOfSales;
    }

    @Override
    public void addStock() {

        if (stockCount < Integer.MAX_VALUE) {
            stockCount++;
        }

    }

    @Override
    public void buyProduct() throws StockUnavailableException {

        if (stockCount < 1) {
            throw new StockUnavailableException();
        } else {

            stockCount--;

            if (numberOfSales < Integer.MAX_VALUE) {
                numberOfSales++;
            }

        }

    }

    @Override
    public void setPrice(int price) {

        if (price <= 0) {
            this.price = DEFAULT_PRICE;
        } else {
            this.price = price;
        }

    }

    @Override
    public int getPrice() {
        return price;
    }

    /**
     * Creates a new {@link StockRecord} instance to store information about the passed {@link IProduct}.
     * If null is passed, the {@link Factory#makeProduct(String, String)} method is called with null values.
     * This causes a product with random field values to be created.
     *
     * @param product the product about which information will be kept
     * @see Product#Product(String, String)
     */
    StockRecord(IProduct product) {

        if (product != null) {
            this.product = product;
        } else {
            this.product = Factory.getInstance().makeProduct(null, null);
        }

        this.price = DEFAULT_PRICE;

    }

}
