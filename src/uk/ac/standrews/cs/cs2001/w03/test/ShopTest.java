package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.Before;
import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w03.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.impl.Shop;
import uk.ac.standrews.cs.cs2001.w03.impl.StockRecord;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * This is a JUnit test class for the {@link Shop} class.
 *
 * @author 150009974
 * @version 2.2
 */
public class ShopTest extends AbstractFactoryClient {

    /**
     * The {@link IShop} instance used in the test methods.
     */
    private IShop shop;

    /**
     * Resets the {@link ShopTest#shop} before each test.
     */
    @Before
    public void setUp() {
        shop = getFactory().makeShop();
    }

    /**
     * Test to see that a new product is successfully registered when a valid
     * {@link IProduct} object is passed.
     */
    @Test
    public void registerProductSuccessfully() {

        IProduct product = getFactory().makeProduct("1-2", "desc");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }
        assertEquals(1, shop.getNumberOfProducts());

    }

    /**
     * Test to see that a new product is not registered when
     * a product with the same barcode is already registered.
     *
     * @throws BarCodeAlreadyInUseException when the product's barcode is already in use
     */
    @Test(expected = BarCodeAlreadyInUseException.class)
    public void registerProductDuplicateBarCode() throws BarCodeAlreadyInUseException {

        IProduct product1 = getFactory().makeProduct("1-2", "product 1");
        IProduct product2 = getFactory().makeProduct("1-2", "product 2");

        try {
            shop.registerProduct(product1);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.registerProduct(product2);

    }

    /**
     * Test to see that a new product is not registered when
     * the same product is attempted to be registered twice.
     *
     * @throws BarCodeAlreadyInUseException when the attempt is made
     */
    @Test(expected = BarCodeAlreadyInUseException.class)
    public void registerProductTwice() throws BarCodeAlreadyInUseException {

        IProduct product = getFactory().makeProduct("1-2", "desc");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.registerProduct(product);

    }

    /**
     * Test to see that a new, random, not-null product is registered when
     * null is passed to the {@link Shop#registerProduct(IProduct)} method.
     */
    @Test
    public void registerProductNull() {

        try {
            shop.registerProduct(null);
            assertEquals(1, shop.getNumberOfProducts());

            /*
             * There is only one product, so that is the most popular one.
             */
            assertNotNull(shop.getMostPopular());
        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that a product is successfully unregistered when a valid
     * {@link IProduct} object is passed to {@link Shop#unregisterProduct(IProduct)}.
     */
    @Test
    public void unregisterProductSuccessfully() {

        IProduct product = getFactory().makeProduct("1-2", "desc");
        try {
            shop.registerProduct(product);
            assertEquals(1, shop.getNumberOfProducts());
            shop.unregisterProduct(product);
        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

        assertEquals(0, shop.getNumberOfProducts());

    }

    /**
     * Test to see that no product is unregistered when a non-existing product
     * is passed to {@link Shop#unregisterProduct(IProduct)}.
     *
     * @throws ProductNotRegisteredException when the non-registered product is passed
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void unregisterProductNotExisting() throws ProductNotRegisteredException {

        IProduct existingProduct = getFactory().makeProduct("3-4", "desc");
        try {
            shop.registerProduct(existingProduct);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }
        assertEquals(1, shop.getNumberOfProducts());

        IProduct notExistingProduct = getFactory().makeProduct("1-2", "desc");
        shop.unregisterProduct(notExistingProduct);

    }

    /**
     * Test to see that unregistering the same, registered, product twice
     * produces the appropriate exception.
     *
     * @throws ProductNotRegisteredException when the same product is unregistered a second time
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void unregisterProductTwice() throws ProductNotRegisteredException {

        IProduct product = getFactory().makeProduct("1-2", "desc");

        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        /*
         * The first time the product is unregistered should be fine.
         */
        try {
            shop.unregisterProduct(product);
        }
        catch (ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

        /*
         * Unregistering it twice should produce the exception.
         */
        shop.unregisterProduct(product);

    }

    /**
     * Test to see that no product is unregistered when a null value is passed to the
     * {@link Shop#unregisterProduct(IProduct)} method.
     *
     * @throws ProductNotRegisteredException when the null value is passed
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void unregisterProductNull() throws ProductNotRegisteredException {
        IProduct product = getFactory().makeProduct("1-2", "desc");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }
        assertEquals(1, shop.getNumberOfProducts());

        shop.unregisterProduct(null);
    }

    /**
     * Test to see that the amount of stock of a particular product is increased by one when
     * the {@link Shop#addStock(String)} method is called with its barcode.
     */
    @Test
    public void addStockSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "des");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        try {

            shop.addStock(barCode);
            assertEquals(1, shop.getStockCount(barCode));

            shop.addStock(barCode);
            assertEquals(2, shop.getStockCount(barCode));

        }
        catch (ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the appropriate exception is thrown when an attempt is made to
     * add stock for a non-registered product.
     *
     * @throws ProductNotRegisteredException when the add stock attempt is made
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void addStockNotRegistered() throws ProductNotRegisteredException {

        String barCode = "1-2";
        shop.addStock(barCode);

    }

    /**
     * Test to see that the appropriate exception is thrown when the
     * {@link Shop#addStock(String)} method is called with a null value.
     *
     * @throws ProductNotRegisteredException when null is passed to {@link Shop#addStock(String)}
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void addStockNull() throws ProductNotRegisteredException {
        shop.addStock(null);
    }

    /**
     * Test to see that a product can successfully be bought when there is stock and that
     * the stock count and number of sales are changed accordingly.
     */
    @Test
    public void buyProductSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "desc");

        try {

            shop.registerProduct(product);
            shop.addStock(barCode);

            assertEquals(1, shop.getStockCount(barCode));

            shop.buyProduct(barCode);
            assertEquals(0, shop.getStockCount(barCode));
            assertEquals(1, shop.getNumberOfSales(barCode));
            assertEquals(shop.getPriceOf(barCode), shop.getRevenue());

        }
        catch (StockUnavailableException | ProductNotRegisteredException | BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the appropriate exception is thrown when
     * an attempt is made to buy a product that is not registered.
     *
     * @throws ProductNotRegisteredException when the attempt is made
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void buyProductNotRegistered() throws ProductNotRegisteredException {

        String barCode = "1-2";

        try {
            shop.buyProduct(barCode);
        }
        catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the appropriate exception is thrown when
     * an attempt is made to buy a product that currently has no stock.
     *
     * @throws StockUnavailableException when the attempt is made
     */
    @Test(expected = StockUnavailableException.class)
    public void buyProductNoStock() throws StockUnavailableException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "desc");
        try {
            shop.registerProduct(product);
            shop.buyProduct(barCode);
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }
    }

    /**
     * Test to see that the appropriate exception is thrown when the
     * {@link Shop#buyProduct(String)} method is called with a null value.
     *
     * @throws ProductNotRegisteredException when the value null is passed to {@link Shop#buyProduct(String)}
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void buyProductNull() throws ProductNotRegisteredException {
        try {
            shop.buyProduct(null);
        }
        catch (StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }
    }

    /**
     * Test to see that the number of products is always correctly returned
     * before and after products are registered and unregistered.
     */
    @Test
    public void getNumberOfProductsTest() {

        String barCode1 = "1-2", barCode2 = "3-4", barCode3 = "5-6";
        String desc = "desc";
        IProduct product1 = getFactory().makeProduct(barCode1, desc);
        IProduct product2 = getFactory().makeProduct(barCode2, desc);
        IProduct product3 = getFactory().makeProduct(barCode3, desc);

        int expectedNumberOfProducts = 0;

        try {

            assertEquals(expectedNumberOfProducts, shop.getNumberOfProducts());

            shop.registerProduct(product1);
            expectedNumberOfProducts++;
            assertEquals(expectedNumberOfProducts, shop.getNumberOfProducts());

            shop.registerProduct(product2);
            expectedNumberOfProducts++;
            assertEquals(expectedNumberOfProducts, shop.getNumberOfProducts());

            shop.registerProduct(product3);
            expectedNumberOfProducts++;
            assertEquals(expectedNumberOfProducts, shop.getNumberOfProducts());

            shop.unregisterProduct(product2);
            expectedNumberOfProducts--;
            assertEquals(expectedNumberOfProducts, shop.getNumberOfProducts());

        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the total stock count is always correctly returned
     * before and after stock is added and bought.
     */
    @Test
    public void getTotalStockCountTest() {

        String barCode1 = "1-2", barCode2 = "3-4";
        IProduct product1 = getFactory().makeProduct(barCode1, "des");
        IProduct product2 = getFactory().makeProduct(barCode2, "des");
        try {
            shop.registerProduct(product1);
            shop.registerProduct(product2);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        int expectedTotal = 0;

        try {

            assertEquals(expectedTotal, shop.getTotalStockCount());

            shop.addStock(barCode1);
            expectedTotal++;
            assertEquals(expectedTotal, shop.getTotalStockCount());

            shop.addStock(barCode1);
            expectedTotal++;
            assertEquals(expectedTotal, shop.getTotalStockCount());

            shop.addStock(barCode2);
            expectedTotal++;
            assertEquals(expectedTotal, shop.getTotalStockCount());

            shop.addStock(barCode2);
            expectedTotal++;
            assertEquals(expectedTotal, shop.getTotalStockCount());

            shop.buyProduct(barCode1);
            expectedTotal--;
            assertEquals(expectedTotal, shop.getTotalStockCount());

            shop.buyProduct(barCode2);
            expectedTotal--;
            assertEquals(expectedTotal, shop.getTotalStockCount());

        }
        catch (ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the stock count for a particular product is always correctly returned
     * before and after stock is added and bought.
     */
    @Test
    public void getStockCountSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "desc");

        try {

            shop.registerProduct(product);

            shop.addStock(barCode);
            assertEquals(1, shop.getStockCount(barCode));

            shop.addStock(barCode);
            assertEquals(2, shop.getStockCount(barCode));

            shop.buyProduct(barCode);
            assertEquals(1, shop.getStockCount(barCode));

        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the appropriate exception is thrown when an attempt is made to retrieve
     * the stock count for a non-existing product.
     *
     * @throws ProductNotRegisteredException when the attempt is made
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void getStockCountNotRegistered() throws ProductNotRegisteredException {

        IProduct product = getFactory().makeProduct("00", "desc");
        try {
            shop.registerProduct(product);
            shop.addStock("00");
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.getStockCount("1-2");
    }

    /**
     * Test to see that the number of sales is successfully returned.
     */
    @Test
    public void getNumberOfSalesSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "desc");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        try {

            shop.addStock(barCode);
            assertEquals(0, shop.getNumberOfSales(barCode));

            shop.buyProduct(barCode);
            assertEquals(1, shop.getNumberOfSales(barCode));

            shop.addStock(barCode);
            shop.buyProduct(barCode);
            assertEquals(2, shop.getNumberOfSales(barCode));

        }
        catch (ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the appropriate exception is thrown when an attempt is made to retrieve
     * the number of sales for a non-registered product.
     *
     * @throws ProductNotRegisteredException when the attempt is made
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void getNumberOfSalesNotRegistered() throws ProductNotRegisteredException {
        shop.getNumberOfSales("1-2");
    }

    /**
     * Test to see that the appropriate exception is thrown when the
     * {@link Shop#getNumberOfSales(String)} method is called with a null value.
     *
     * @throws ProductNotRegisteredException when the null value is passed to {@link Shop#getNumberOfSales(String)}
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void getNumberOfSalesNull() throws ProductNotRegisteredException {
        shop.getNumberOfSales(null);
    }

    /**
     * Test to see that the {@link Shop#getMostPopular()} method correctly returns the product
     * with the most sales.
     */
    @Test
    public void getMostPopularSuccessfully() {

        String barCode1 = "1-2", barCode2 = "3-4", barCode3 = "5-6";
        IProduct product1 = getFactory().makeProduct(barCode1, "p1");
        IProduct product2 = getFactory().makeProduct(barCode2, "p2");
        IProduct product3 = getFactory().makeProduct(barCode3, "p3");

        try {
            shop.registerProduct(product1);
            shop.registerProduct(product2);
            shop.registerProduct(product3);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        try {

            //Add some stock.
            shop.addStock(barCode1);
            shop.addStock(barCode1);
            shop.addStock(barCode1);
            shop.addStock(barCode1);

            shop.addStock(barCode2);
            shop.addStock(barCode2);
            shop.addStock(barCode2);

            shop.addStock(barCode3);
            shop.addStock(barCode3);
            shop.addStock(barCode3);
            shop.addStock(barCode3);

            //Buy some/all of it.
            shop.buyProduct(barCode1);
            shop.buyProduct(barCode1);
            shop.buyProduct(barCode1);

            shop.buyProduct(barCode2);
            shop.buyProduct(barCode2);

            shop.buyProduct(barCode3);
            shop.buyProduct(barCode3);
            shop.buyProduct(barCode3);
            shop.buyProduct(barCode3);
            assertEquals(product3, shop.getMostPopular());

            shop.buyProduct(barCode1);
            assertEquals(product1, shop.getMostPopular());

        }
        catch (ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    /**
     * Test to see that the appropriate exception is thrown when an attempt is made to retrieve
     * the most popular product, when there are no registered products.
     *
     * @throws ProductNotRegisteredException when the attempt is made
     */
    @Test(expected = ProductNotRegisteredException.class)
    public void getMostPopularNoProducts() throws ProductNotRegisteredException {
        shop.getMostPopular();
    }

    @Test
    public void getProductSuccessfully() {

        String barCode1 = "1-2", barCode2 = "3-4";
        IProduct product1 = getFactory().makeProduct(barCode1, "des");
        IProduct product2 = getFactory().makeProduct(barCode2, "des");

        try {
            shop.registerProduct(product1);
            shop.registerProduct(product2);

            assertEquals(product1, shop.getProduct(barCode1));
            assertEquals(product2, shop.getProduct(barCode2));
        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getProductNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "des");

        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.getProduct(null);

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getProductNotInShop() throws ProductNotRegisteredException {

        String barCode = "1-2", invalidBarCode = "0000";
        IProduct product = getFactory().makeProduct(barCode, "des");

        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.getProduct(invalidBarCode);

    }

    @Test
    public void setAndGetPriceOfSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "des");
        try {

            shop.registerProduct(product);

            shop.setPriceOf(barCode, 0);
            assertEquals(StockRecord.DEFAULT_PRICE, shop.getPriceOf(barCode));

            shop.setPriceOf(barCode, 10);
            assertEquals(10, shop.getPriceOf(barCode));

            shop.setPriceOf(barCode, -2);
            assertEquals(StockRecord.DEFAULT_PRICE, shop.getPriceOf(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void setPriceOfNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "des");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.setPriceOf(null, 2);

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getPriceOfNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, "des");
        try {
            shop.registerProduct(product);
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        shop.getPriceOf(null);

    }

    @Test
    public void getRevenueTest() {

        String barCode1 = "1-2", barCode2 = "3-4";
        IProduct product1 = getFactory().makeProduct(barCode1, "desc");
        IProduct product2 = getFactory().makeProduct(barCode2, "desc");
        int price1 = 1, price2 = 3;

        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);

            shop.setPriceOf(barCode1, price1);
            shop.setPriceOf(barCode2, price2);

            shop.addStock(barCode1);
            shop.addStock(barCode1);
            shop.addStock(barCode2);
            shop.addStock(barCode2);

            assertEquals(0, shop.getRevenue());

            shop.buyProduct(barCode1);
            assertEquals(price1, shop.getRevenue());

            shop.buyProduct(barCode2);
            assertEquals(price1 + price2, shop.getRevenue());

            shop.buyProduct(barCode1);
            assertEquals(price1 * 2 + price2, shop.getRevenue());

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }
    }

}
