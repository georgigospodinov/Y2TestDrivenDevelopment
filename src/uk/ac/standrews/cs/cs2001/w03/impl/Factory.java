package uk.ac.standrews.cs.cs2001.w03.impl;

import uk.ac.standrews.cs.cs2001.w03.interfaces.IFactory;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IStockRecord;
import uk.ac.standrews.cs.cs2001.w03.interfaces.ICustomer;

/**
 * This class implements a singleton factory.
 */
public final class Factory implements IFactory {

    /**
     * The {@link IFactory} instance used by other classes to make instances.
     * It is only accessed via {@link Factory#getInstance()}.
     */
    private static IFactory factoryInstance = null;

    private Factory() {
    }

    /**
     * Method which returns an instance of the singleton Factory class.
     *
     * @return the instance of the Factory
     */
    public static IFactory getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new Factory();
        }
        return factoryInstance;
    }

    @Override
    public IProduct makeProduct(String barCode, String description) {
        return new Product(barCode, description);
    }

    @Override
    public IStockRecord makeStockRecord(IProduct product) {
        return new StockRecord(product);
    }

    @Override
    public IShop makeShop() {
        return new Shop();
    }

    @Override
    public ICustomer makeCustomer() {
        return new Customer();
    }

}
