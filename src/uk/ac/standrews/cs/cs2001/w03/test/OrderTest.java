package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.Before;
import org.junit.Test;

import uk.ac.standrews.cs.cs2001.w03.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w03.common.BarCodeAlreadyInUseException;
import uk.ac.standrews.cs.cs2001.w03.common.OrderAlreadyCompleteException;
import uk.ac.standrews.cs.cs2001.w03.common.ProductNotRegisteredException;
import uk.ac.standrews.cs.cs2001.w03.common.StockUnavailableException;
import uk.ac.standrews.cs.cs2001.w03.impl.Order;
import uk.ac.standrews.cs.cs2001.w03.interfaces.ICustomer;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IOrder;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


/**
 * This is a JUnit test class for the {@link Order} class.
 *
 * @author 150009974
 * @version 1.5
 */
public class OrderTest extends AbstractFactoryClient {

    /**
     * The {@link IShop} to which the {@link OrderTest#order} is associated.
     */
    private IShop shop;

    /**
     * The {@link IOrder} instance used in the test methods.
     */
    private IOrder order;

    /**
     * Resets the {@link OrderTest#shop} and {@link OrderTest#order} before each test.
     */
    @Before
    public void setUp() {
        shop = getFactory().makeShop();
        ICustomer customer = getFactory().makeCustomer();
        order = customer.createOrder(shop);
    }

    @Test
    public void addItemSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);

            assertEquals(0, order.getNumberOfItems());

            order.addItem(barCode);
            assertEquals(1, order.getNumberOfItems());

            assertEquals(product, order.getItem(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void addItemNull() throws ProductNotRegisteredException {

        try {
            shop.registerProduct(getFactory().makeProduct(null, null));
            order.addItem(null);
        }
        catch (BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void addItemNotInShop() throws ProductNotRegisteredException {

        try {
            order.addItem("1111");
        }
        catch (BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }
    }

    @Test(expected = BarCodeAlreadyInUseException.class)
    public void addItemTwice() throws BarCodeAlreadyInUseException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);
            order.addItem(barCode);
        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.addItem(barCode);
        }
        catch (ProductNotRegisteredException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = OrderAlreadyCompleteException.class)
    public void addItemToCompletedOrder() throws OrderAlreadyCompleteException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);
            order.addItem(barCode);
            order.increaseQuantityOf(barCode);
            order.complete();

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        IProduct product1 = getFactory().makeProduct(null, null);
        try {
            order.addItem(product1.getBarCode());
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void removeItemSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);

            assertEquals(0, order.getNumberOfItems());

            order.addItem(barCode);
            assertEquals(1, order.getNumberOfItems());

            assertEquals(product, order.removeItem(barCode));
            assertEquals(0, order.getNumberOfItems());

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void removeItemNull() throws ProductNotRegisteredException {

        try {
            shop.registerProduct(getFactory().makeProduct(null, null));
        }
        catch (BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.removeItem(null);
        }
        catch (OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void removeItemNotInOrder() throws ProductNotRegisteredException {

        String barCode1 = "1", barCode2 = "2";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);

        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);

            order.addItem(barCode1);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.removeItem(barCode2);
        }
        catch (OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }
    }

    @Test(expected = ProductNotRegisteredException.class)
    public void removeItemTwice() throws ProductNotRegisteredException {

        String barCode1 = "1";
        IProduct product1 = getFactory().makeProduct(barCode1, null);

        try {

            shop.registerProduct(product1);

            order.addItem(barCode1);
            order.removeItem(barCode1);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.removeItem(barCode1);
        }
        catch (OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = OrderAlreadyCompleteException.class)
    public void removeItemFromCompletedOrder() throws OrderAlreadyCompleteException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);

            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);

            order.complete();

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }


        try {
            order.removeItem(product.getBarCode());
        }
        catch (ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void getItemSuccessfully() {

        String barCode1 = "1", barCode2 = "2";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);

        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);

            order.addItem(barCode1);
            order.addItem(barCode2);

            assertEquals(product1, order.getItem(barCode1));
            assertEquals(product2, order.getItem(barCode2));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getItemNull() throws ProductNotRegisteredException {

        String barCode = "1";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            order.addItem(barCode);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        order.getItem(null);

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getItemNotInOrder() throws ProductNotRegisteredException {

        String barCode = "1", invalidCode = "2";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            order.addItem(barCode);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        order.getItem(invalidCode);

    }

    @Test
    public void getNumberOfItemsTest() {

        String barCode1 = "1", barCode2 = "2";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);

        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);
            assertEquals(0, order.getNumberOfItems());

            order.addItem(barCode1);
            assertEquals(1, order.getNumberOfItems());

            order.addItem(barCode2);
            assertEquals(2, order.getNumberOfItems());

            order.removeItem(barCode1);
            assertEquals(1, order.getNumberOfItems());

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void increaseQuantityOfSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);

            assertEquals(0, order.getQuantityOf(barCode));

            order.increaseQuantityOf(barCode);
            assertEquals(1, order.getQuantityOf(barCode));

            order.increaseQuantityOf(barCode);
            assertEquals(2, order.getQuantityOf(barCode));

            order.increaseQuantityOf(barCode);
            assertEquals(3, order.getQuantityOf(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void increaseQuantityOfNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.increaseQuantityOf(null);
        }
        catch (StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void increaseQuantityOfNotInOrder() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);
            shop.addStock(barCode);

            order.addItem(barCode);
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.increaseQuantityOf("000");
        }
        catch (StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }
    }

    @Test(expected = StockUnavailableException.class)
    public void increaseQuantityOfNotEnoughStock() throws StockUnavailableException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

            assertEquals(1, order.getQuantityOf(barCode));

            order.increaseQuantityOf(barCode);
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = OrderAlreadyCompleteException.class)
    public void increaseQuantityOfCompletedOrder() throws OrderAlreadyCompleteException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            order.addItem(barCode);
            order.complete();

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.increaseQuantityOf(barCode);
        }
        catch (ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void increaseQuantityOfNotInShop() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {
            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

            shop.unregisterProduct(product);
        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.increaseQuantityOf(barCode);
        }
        catch (StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void decreaseQuantityOfSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);

            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);
            assertEquals(3, order.getQuantityOf(barCode));

            order.decreaseQuantityOf(barCode);
            assertEquals(2, order.getQuantityOf(barCode));

            order.decreaseQuantityOf(barCode);
            assertEquals(1, order.getQuantityOf(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void decreaseQuantityOfNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {
            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);
        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.decreaseQuantityOf(null);
        }
        catch (OrderAlreadyCompleteException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void decreaseQuantityOfNotInOrder() throws ProductNotRegisteredException {

        String barCode = "1-2", notAddedBarCode = "000";
        IProduct product = getFactory().makeProduct(barCode, null);
        IProduct notAddedProduct = getFactory().makeProduct(notAddedBarCode, null);

        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.registerProduct(notAddedProduct);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.decreaseQuantityOf(notAddedBarCode);
        }
        catch (StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = StockUnavailableException.class)
    public void decreaseQuantityOfNotEnough() throws StockUnavailableException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);

            order.addItem(barCode);

            order.increaseQuantityOf(barCode);
            order.decreaseQuantityOf(barCode);
            assertEquals(0, order.getQuantityOf(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.decreaseQuantityOf(barCode);
        }
        catch (ProductNotRegisteredException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = OrderAlreadyCompleteException.class)
    public void decreaseQuantityOfCompletedOrder() throws OrderAlreadyCompleteException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);

            order.complete();

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.decreaseQuantityOf(barCode);
        }
        catch (ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void getQuantityOfSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);

            assertEquals(0, order.getQuantityOf(barCode));

            order.increaseQuantityOf(barCode);
            assertEquals(1, order.getQuantityOf(barCode));

            order.decreaseQuantityOf(barCode);
            assertEquals(0, order.getQuantityOf(barCode));

            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);
            assertEquals(2, order.getQuantityOf(barCode));

            order.decreaseQuantityOf(barCode);
            assertEquals(1, order.getQuantityOf(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getQuantityOfNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            order.addItem(barCode);

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException e) {
            fail(NOT_EXPECTED);
        }

        order.getQuantityOf(null);

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getQuantityOfNotInOrder() throws ProductNotRegisteredException {

        String barCode1 = "1-2", barCode2 = "000";
        IProduct product1 = getFactory().makeProduct(barCode1, null);

        try {

            shop.registerProduct(product1);
            order.addItem(barCode1);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        order.getQuantityOf(barCode2);

    }

    @Test
    public void getTotalQuantityTest() {

        String barCode1 = "1", barCode2 = "2";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);

        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);

            shop.addStock(barCode1);
            shop.addStock(barCode1);
            shop.addStock(barCode1);

            shop.addStock(barCode2);
            shop.addStock(barCode2);
            shop.addStock(barCode2);

            order.addItem(barCode1);
            order.addItem(barCode2);
            assertEquals(0, order.getTotalQuantity());

            order.increaseQuantityOf(barCode1);
            assertEquals(1, order.getTotalQuantity());

            order.increaseQuantityOf(barCode2);
            assertEquals(2, order.getTotalQuantity());

            order.increaseQuantityOf(barCode1);
            assertEquals(3, order.getTotalQuantity());

            order.decreaseQuantityOf(barCode1);
            assertEquals(2, order.getTotalQuantity());

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void getCostOfSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);

            shop.setPriceOf(barCode, 2);

            order.addItem(barCode);
            assertEquals(0, order.getCostOf(barCode));

            order.increaseQuantityOf(barCode);
            assertEquals(shop.getPriceOf(barCode), order.getCostOf(barCode));

            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);
            assertEquals(shop.getPriceOf(barCode) * order.getQuantityOf(barCode), order.getCostOf(barCode));

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getCostOfNull() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.setPriceOf(barCode, 2);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        order.getCostOf(null);

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void getCostOfNotAdded() throws ProductNotRegisteredException {

        String barCode1 = "1-2", barCode2 = "3-4";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);
        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);

            shop.addStock(barCode1);
            shop.addStock(barCode2);
            shop.setPriceOf(barCode1, 2);
            shop.setPriceOf(barCode2, 3);

            order.addItem(barCode1);
            order.increaseQuantityOf(barCode1);
        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        order.getCostOf(barCode2);

    }

    @Test
    public void getTotalOrderCost() {

        String barCode1 = "1-2", barCode2 = "3-4";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);
        int expectedCost = 0;
        try {

            shop.registerProduct(product1);
            shop.registerProduct(product2);

            shop.addStock(barCode1);
            shop.addStock(barCode1);
            shop.addStock(barCode1);
            shop.addStock(barCode2);
            shop.addStock(barCode2);
            shop.addStock(barCode2);
            shop.addStock(barCode2);
            shop.setPriceOf(barCode1, 2);
            shop.setPriceOf(barCode2, 3);

            assertEquals(expectedCost, order.getTotalOrderCost());
            order.addItem(barCode1);
            order.increaseQuantityOf(barCode1);
            expectedCost += shop.getPriceOf(barCode1);
            assertEquals(expectedCost, order.getTotalOrderCost());

            order.increaseQuantityOf(barCode1);
            expectedCost += shop.getPriceOf(barCode1);
            assertEquals(expectedCost, order.getTotalOrderCost());

            order.addItem(barCode2);
            order.increaseQuantityOf(barCode2);
            expectedCost += shop.getPriceOf(barCode2);
            assertEquals(expectedCost, order.getTotalOrderCost());

            order.increaseQuantityOf(barCode2);
            expectedCost += shop.getPriceOf(barCode2);
            assertEquals(expectedCost, order.getTotalOrderCost());

            order.increaseQuantityOf(barCode2);
            expectedCost += shop.getPriceOf(barCode2);
            assertEquals(expectedCost, order.getTotalOrderCost());

            expectedCost -= order.getQuantityOf(barCode2) * shop.getPriceOf(barCode2);
            order.removeItem(barCode2);
            assertEquals(expectedCost, order.getTotalOrderCost());

            order.decreaseQuantityOf(barCode1);
            expectedCost -= shop.getPriceOf(barCode1);
            assertEquals(expectedCost, order.getTotalOrderCost());

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void completeSuccessfully() {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);
        int price = 20;
        try {

            shop.registerProduct(product);
            shop.setPriceOf(barCode, price);

            shop.addStock(barCode);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);
            order.increaseQuantityOf(barCode);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        assertFalse(order.isComplete());
        assertEquals(0, shop.getRevenue());

        try {
            order.complete();
        }
        catch (StockUnavailableException | ProductNotRegisteredException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }
        assertTrue(order.isComplete());
        assertEquals(order.getTotalOrderCost(), shop.getRevenue());

    }

    @Test(expected = ProductNotRegisteredException.class)
    public void completeProductRemoved() throws ProductNotRegisteredException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

            shop.unregisterProduct(product);

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.complete();
        }
        catch (StockUnavailableException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = StockUnavailableException.class)
    public void completeReducedStock() throws StockUnavailableException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

            shop.buyProduct(barCode);

        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | BarCodeAlreadyInUseException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.complete();
        }
        catch (ProductNotRegisteredException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }
    }

    @Test(expected = OrderAlreadyCompleteException.class)
    public void completeOrderTwice() throws StockUnavailableException, OrderAlreadyCompleteException {

        String barCode = "1-2";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            shop.addStock(barCode);

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

            order.complete();
        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException | OrderAlreadyCompleteException e) {
            fail(NOT_EXPECTED);
        }

        try {
            order.complete();
        }
        catch (ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }
    }

    @Test
    public void getShopTest() {

        assertEquals(shop, order.getShop());

    }

    @Test(expected = NullPointerException.class)
    public void constructorNullShop() {
        order = getFactory().makeCustomer().createOrder(null);
    }

}
