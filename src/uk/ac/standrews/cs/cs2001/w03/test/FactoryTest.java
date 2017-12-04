package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w03.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.impl.Customer;
import uk.ac.standrews.cs.cs2001.w03.impl.Factory;
import uk.ac.standrews.cs.cs2001.w03.impl.Shop;
import uk.ac.standrews.cs.cs2001.w03.impl.StockRecord;
import uk.ac.standrews.cs.cs2001.w03.impl.Product;
import uk.ac.standrews.cs.cs2001.w03.interfaces.ICustomer;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IStockRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * This is a JUnit test class for the {@link Factory} class.
 *
 * @author 150009974
 * @version 1.1
 */
public class FactoryTest extends AbstractFactoryClient {

    /**
     * Test to see if the {@link Factory#makeShop()} method
     * correctly creates a {@link Shop} object.
     * The object returned should not be null and it should be possible to call methods on it.
     */
    @Test
    public void makeShopTest() {

        IShop shop = getFactory().makeShop();
        assertNotNull(shop);

        IProduct product = getFactory().makeProduct("1-2", "desc");
        try {
            shop.registerProduct(product);
            shop.unregisterProduct(product);
        } catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see if the {@link Factory#makeStockRecord(IProduct)} method
     * correctly creates a {@link StockRecord} object.
     * The object returned should not be null and it should be possible to call methods on it.
     */
    @Test
    public void makeStockRecordSuccessfully() {

        IProduct product = getFactory().makeProduct("1-2", "desc");
        IStockRecord record = getFactory().makeStockRecord(product);

        assertNotNull(record);
        assertEquals(product, record.getProduct());

        record.addStock();
        try {
            record.buyProduct();
        } catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see if the {@link Factory#makeStockRecord(IProduct)} method
     * correctly creates a {@link StockRecord} object when the passed {@link IProduct} is null.
     * The object returned and its fields should not be null.
     */
    @Test
    public void makeStockRecordNull() {

        IStockRecord record = getFactory().makeStockRecord(null);

        assertNotNull(record);
        assertNotNull(record.getProduct());
        assertNotNull(record.getProduct().getBarCode());
        assertNotNull(record.getProduct().getDescription());

    }

    /**
     * Test to see if the {@link Factory#makeProduct(String, String)} method
     * correctly creates a {@link Product}.
     * The object returned should not be null and it should be possible to call methods on it.
     */
    @Test
    public void makeProductSuccessfully() {

        String barCode = "1-2", description = "desc";
        IProduct product = getFactory().makeProduct(barCode, description);

        assertNotNull(product);
        assertEquals(barCode, product.getBarCode());
        assertEquals(description, product.getDescription());

    }

    /**
     * Test to see if the {@link Factory#makeProduct(String, String)} method
     * correctly creates a {@link Product} when null values are passed.
     * The object returned and its fields should not be null.
     */
    @Test
    public void makeProductNulls() {

        IProduct product = getFactory().makeProduct(null, null);

        assertNotNull(product);
        assertNotNull(product.getBarCode());
        assertNotNull(product.getDescription());

    }

    /**
     * Test to see if the {@link Factory#makeCustomer()} method
     * correctly creates {@link Customer} object.
     * The object returned should not be null and it should be possible to call methods on it.
     */
    @Test
    public void makeCustomerTest() {

        ICustomer customer = getFactory().makeCustomer();

        assertNotNull(customer);
        assertEquals(Customer.DEFAULT_MONEY, customer.getMoney());

    }

}
