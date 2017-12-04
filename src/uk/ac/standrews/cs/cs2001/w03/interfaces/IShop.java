package uk.ac.standrews.cs.cs2001.w03.interfaces;

import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;

/**
 * Interface for a simple shop ADT.
 */
public interface IShop {

    /**
     * Registers the specified product for sale in the shop.
     *
     * @param product the product to register
     * @throws BarCodeAlreadyInUseException if a product is already registered in the shop with matching bar code
     */
    void registerProduct(IProduct product) throws BarCodeAlreadyInUseException;

    /**
     * Unregisters the specified product from the shop.
     *
     * @param product the product to remove
     * @throws ProductNotRegisteredException if the product has not been registered in the shop
     */
    void unregisterProduct(IProduct product) throws ProductNotRegisteredException;

    /**
     * Adds stock for the product with given bar code.
     *
     * @param barCode the bar code of the product
     * @throws ProductNotRegisteredException if the product does not exist in the shop
     */
    void addStock(String barCode) throws ProductNotRegisteredException;

    /**
     * Buys the product with given bar code from the shop.
     *
     * @param barCode the bar code of the product to be bought
     * @throws StockUnavailableException     if the product is not currently in stock
     * @throws ProductNotRegisteredException if the product does not exist in the shop
     */
    void buyProduct(String barCode) throws StockUnavailableException, ProductNotRegisteredException;

    /**
     * Gets the number of different products currently available in the shop. Multiple copies of the same product
     * only count once.
     *
     * @return the number of different products available in the shop
     */
    int getNumberOfProducts();

    /**
     * Gets the total count of stock over all products in the shop. For example, returns 3 if
     * the shop has 2 cans of baked beans and 1 loaf of bread in stock.
     *
     * @return the total stock count over all products
     */
    int getTotalStockCount();

    /**
     * Gets the stock count for a particular product.
     *
     * @param barCode the bar code of the product
     * @return the stock count for a particular product
     * @throws ProductNotRegisteredException if the product does not exist in the shop
     */
    int getStockCount(String barCode) throws ProductNotRegisteredException;

    /**
     * Gets the total number of times that a given product was bought.
     *
     * @param barCode the bar code of the product
     * @return the total number of times that the product has been bought
     * @throws ProductNotRegisteredException if the product does not exist in the shop
     */
    int getNumberOfSales(String barCode) throws ProductNotRegisteredException;

    /**
     * Gets the product that has been bought the greatest number of times. The behaviour
     * is undefined if there is not a single most popular product.
     *
     * @return the product that has been bought the greatest number of times
     * @throws ProductNotRegisteredException if no products are registered in the shop
     */
    IProduct getMostPopular() throws ProductNotRegisteredException;

    /**
     * Retrieves the {@link IProduct} with the specified bar code.
     * This method is added for the extension.
     *
     * @param barCode the bar code of the product to retrieve
     * @return the product with that bar code
     * @throws ProductNotRegisteredException if no such product is found
     */
    IProduct getProduct(String barCode) throws ProductNotRegisteredException;

    /**
     * Sets the price of the {@link IProduct} with the specified bar code.
     *
     * @param barCode the bar code of the product to have its price set
     * @param price   the price to be set
     * @throws ProductNotRegisteredException when the no product with that bar code is found
     */
    void setPriceOf(String barCode, int price) throws ProductNotRegisteredException;

    /**
     * Gets the price of the {@link IProduct} with the specified bar code.
     *
     * @param barCode the bar code of the product
     * @return the price of the product
     * @throws ProductNotRegisteredException when the no product with that bar code is found
     */
    int getPriceOf(String barCode) throws ProductNotRegisteredException;

    /**
     * Calculates and returns the total shop revenue from all sales.
     *
     * @return the shop revenue
     */
    int getRevenue();

}
