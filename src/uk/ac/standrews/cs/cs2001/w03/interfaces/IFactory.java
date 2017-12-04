package uk.ac.standrews.cs.cs2001.w03.interfaces;

/**
 * Interface for a factory allowing the other interfaces to be instantiated without knowing the implementation classes.
 * 
 */
public interface IFactory {

    /**
     * Creates an instance of {@link IProduct}.
     * @param barCode the bar code of the product
     * @param description the description of the product
     * @return the product
     */
    IProduct makeProduct(String barCode, String description);

    /**
     * This method creates an instance of {@link IStockRecord} for a new product.
     * @param product the product to use for this stock record
     * @return the stick record
     */
    IStockRecord makeStockRecord(IProduct product);

    /**
     * Creates an instance of {@link IShop}.
     * 
     * @return the shop
     */
    IShop makeShop();

    /**
     * Creates an instance of {@link ICustomer}.
     * This method is added for the extensions.
     *
     * @return the created customer
     */
    ICustomer makeCustomer();

}
