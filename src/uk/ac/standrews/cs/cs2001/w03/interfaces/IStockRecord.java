package uk.ac.standrews.cs.cs2001.w03.interfaces;

import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;

/**
 * This is the interface for a StockRecord.
 */
public interface IStockRecord {

    /**
     * Returns the product associated with this IStockRecord.
     *
     * @return the product related to this stock record
     */
    IProduct getProduct();

    /**
     * Returns the stock count for this product.
     *
     * @return the stock level for the product
     */
    int getStockCount();

    /**
     * Returns the number of times this product has been bought.
     *
     * @return the number of sales for the product
     */
    int getNumberOfSales();

    /**
     * Adds stock for the product.
     */
    void addStock();

    /**
     * Processes the purchase of this product from the stock.
     *
     * @throws StockUnavailableException when the product is not currently in stock
     */
    void buyProduct() throws StockUnavailableException;

    /**
     * Sets the price of the {@link IProduct}.
     * This method is added for the extension.
     *
     * @param price the price to be set
     */
    void setPrice(int price);

    /**
     * Gets the price of the {@link IProduct}.
     * This method is added for the extension.
     *
     * @return the price of the {@link IProduct}
     */
    int getPrice();

}
