package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w03.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.impl.Factory;
import uk.ac.standrews.cs.cs2001.w03.impl.StockRecord;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IStockRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This is a JUnit test class for the {@link StockRecord} class.
 *
 * @author 150009974
 * @version 1.2
 */
public class StockRecordTest extends AbstractFactoryClient {

    /**
     * The {@link IStockRecord} instance used in the test methods.
     */
    private IStockRecord stockRecord;

    /**
     * The {@link IProduct} instance passed to the
     * {@link Factory#makeStockRecord(IProduct)} method, when instantiating
     * the {@link StockRecordTest#stockRecord} object.
     */
    private static IProduct product;

    /**
     * Instantiates the product before the test class is run.
     */
    @BeforeClass
    public static void setUpClass() {
        product = getFactory().makeProduct("123-456", "nothing");
    }

    /**
     * Resets the {@link StockRecordTest#stockRecord} before each test method.
     */
    @Before
    public void setUp() {
        stockRecord = getFactory().makeStockRecord(product);
    }

    /**
     * Test to see if the {@link StockRecord#getProduct()} method correctly returns the Product.
     */
    @Test
    public void getProductTest() {

        assertEquals(product, stockRecord.getProduct());

    }

    /**
     * Test to see if the {@link StockRecord#getStockCount()} method correctly returns the amount of stock.
     */
    @Test
    public void getStockCountTest() {

        assertEquals(0, stockRecord.getStockCount());

        stockRecord.addStock();
        assertEquals(1, stockRecord.getStockCount());

        stockRecord.addStock();
        assertEquals(2, stockRecord.getStockCount());

        try {
            stockRecord.buyProduct();
        }
        catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }
        assertEquals(1, stockRecord.getStockCount());

    }

    /**
     * Test to see if the {@link StockRecord#getNumberOfSales()} method
     * correctly returns the {@link StockRecord#numberOfSales}.
     */
    @Test
    public void getNumberOfSalesTest() {

        assertEquals(0, stockRecord.getNumberOfSales());

        stockRecord.addStock();
        stockRecord.addStock();

        try {

            assertEquals(0, stockRecord.getNumberOfSales());

            stockRecord.buyProduct();
            assertEquals(1, stockRecord.getNumberOfSales());

            stockRecord.buyProduct();
            assertEquals(2, stockRecord.getNumberOfSales());

        }
        catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see if the {@link StockRecord#addStock()} method successfully
     * increments the {@link StockRecord#stockCount} by one.
     */
    @Test
    public void addStockTest() {

        assertEquals(0, stockRecord.getStockCount());

        stockRecord.addStock();
        assertEquals(1, stockRecord.getStockCount());

        /*
         * Reach the maximum stock count.
         */
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            stockRecord.addStock();
        }
        assertEquals(Integer.MAX_VALUE, stockRecord.getStockCount());

        stockRecord.addStock();
        assertEquals(Integer.MAX_VALUE, stockRecord.getStockCount());

    }

    /**
     * Test to see if the {@link StockRecord#buyProduct()} method successfully
     * reduces the {@link StockRecord#stockCount} and increases the {@link StockRecord#numberOfSales}
     * when stock is available.
     */
    @Test
    public void buyProductAvailable() {

        assertEquals(0, stockRecord.getNumberOfSales());

        stockRecord.addStock();

        try {
            stockRecord.buyProduct();
        }
        catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }
        assertEquals(1, stockRecord.getNumberOfSales());

        /*
         * Reach the maximum number of sales.
         */
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            stockRecord.addStock();
            try {
                stockRecord.buyProduct();
            }
            catch (StockUnavailableException e) {
                fail(NOT_EXPECTED);
            }
        }
        assertEquals(Integer.MAX_VALUE, stockRecord.getNumberOfSales());

        stockRecord.addStock();
        try {
            stockRecord.buyProduct();
        }
        catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }
        assertEquals(Integer.MAX_VALUE, stockRecord.getNumberOfSales());

    }

    /**
     * Test to see if the {@link StockRecord#buyProduct()} method does not change the fields' values
     * when no stock is available for purchase.
     *
     * @throws StockUnavailableException the exception that is thrown when stock is unavailable for purchase
     */
    @Test(expected = StockUnavailableException.class)
    public void buyProductUnavailable() throws StockUnavailableException {
        stockRecord.buyProduct();
    }

    /**
     * Test to see if the setter and getter for the price are working correctly.
     */
    @Test
    public void setAndGetPriceTest() {

        int price = 5;
        stockRecord.setPrice(price);
        assertEquals(price, stockRecord.getPrice());

        price = -2;
        stockRecord.setPrice(price);
        assertEquals(StockRecord.DEFAULT_PRICE, stockRecord.getPrice());

        price = 10;
        stockRecord.setPrice(price);
        assertEquals(price, stockRecord.getPrice());

        price = 0;
        stockRecord.setPrice(price);
        assertEquals(StockRecord.DEFAULT_PRICE, stockRecord.getPrice());

    }

}
