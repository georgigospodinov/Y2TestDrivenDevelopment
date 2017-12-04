package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.Before;
import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w03.common.*;
import uk.ac.standrews.cs.cs2001.w03.impl.Customer;
import uk.ac.standrews.cs.cs2001.w03.impl.StockRecord;
import uk.ac.standrews.cs.cs2001.w03.interfaces.ICustomer;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IOrder;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IShop;

import static org.junit.Assert.*;

/**
 * This is a JUnit test class for the {@link Customer} class.
 *
 * @author 150009974
 * @version 1.0
 */
public class CustomerTest extends AbstractFactoryClient {

    /**
     * The {@link ICustomer} instance used in the test methods.
     */
    private ICustomer customer;

    /**
     * Resets the {@link CustomerTest#customer} before each test.
     */
    @Before
    public void setUp() {
        customer = getFactory().makeCustomer();
    }

    @Test
    public void setAndGetMoneyTest() {

        assertEquals(Customer.DEFAULT_MONEY, customer.getMoney());

        int money = 50;
        customer.setMoney(money);
        assertEquals(money, customer.getMoney());

        money = -30;
        customer.setMoney(money);
        assertEquals(Customer.DEFAULT_MONEY, customer.getMoney());

    }

    @Test
    public void createOrderSuccessfully() {

        assertEquals(0, customer.getTotalNumberOfOrders());
        customer.createOrder(getFactory().makeShop());
        assertEquals(1, customer.getTotalNumberOfOrders());

    }

    @Test(expected = NullPointerException.class)
    public void createOrderNull() {
        customer.createOrder(null);
    }

    @Test
    public void getOrderSuccessfully() {

        customer.createOrder(getFactory().makeShop());
        try {

            IOrder order = customer.getOrder(0);
            assertNotNull(order);
            assertFalse(order.isComplete());

        }
        catch (IndexOutOfBoundsException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getOrderNotFound() throws IndexOutOfBoundsException {
        customer.getOrder(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getOrderNegativeIndex() throws IndexOutOfBoundsException {
        customer.getOrder(-2);
    }

    @Test
    public void getTotalNumberOfOrdersTest() {

        IShop shop = getFactory().makeShop();

        assertEquals(0, customer.getTotalNumberOfOrders());

        customer.createOrder(shop);
        customer.createOrder(shop);
        assertEquals(2, customer.getTotalNumberOfOrders());

        customer.createOrder(shop);
        assertEquals(3, customer.getTotalNumberOfOrders());

        try {

            customer.completeOrder(1);
            customer.completeOrder(2);

        }
        catch (OrderAlreadyCompleteException | NotEnoughMoneyException | StockUnavailableException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

        assertEquals(3, customer.getTotalNumberOfOrders());

    }

    @Test
    public void getNumberOfCompletedOrdersTest() {

        IShop shop = getFactory().makeShop();

        customer.createOrder(shop);
        customer.createOrder(shop);

        try {

            assertEquals(0, customer.getNumberOfCompletedOrders());

            customer.completeOrder(0);
            assertEquals(1, customer.getNumberOfCompletedOrders());

            customer.completeOrder(1);
            assertEquals(2, customer.getNumberOfCompletedOrders());

        }
        catch (OrderAlreadyCompleteException | NotEnoughMoneyException | StockUnavailableException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void getNumberOfIncompleteOrdersTest() {

        IShop shop = getFactory().makeShop();

        customer.createOrder(shop);
        customer.createOrder(shop);

        try {

            assertEquals(2, customer.getNumberOfIncompleteOrders());

            customer.completeOrder(0);
            assertEquals(1, customer.getNumberOfIncompleteOrders());

            customer.completeOrder(1);
            assertEquals(0, customer.getNumberOfIncompleteOrders());

            customer.createOrder(shop);
            assertEquals(1, customer.getNumberOfIncompleteOrders());

        }
        catch (OrderAlreadyCompleteException | NotEnoughMoneyException | ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test
    public void completeOrderSuccessfully() {

        IShop shop = getFactory().makeShop();
        String barCode1 = "1111", barCode2 = "2222", barCode3 = "3333";
        IProduct product1 = getFactory().makeProduct(barCode1, null);
        IProduct product2 = getFactory().makeProduct(barCode2, null);
        IProduct product3 = getFactory().makeProduct(barCode3, null);

        try {

            shop.registerProduct(product1);
            shop.addStock(barCode1);
            shop.addStock(barCode1);

            shop.registerProduct(product2);
            shop.addStock(barCode2);
            shop.addStock(barCode2);

            shop.registerProduct(product3);
            shop.addStock(barCode3);
            shop.addStock(barCode3);

        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

        IOrder order = customer.createOrder(shop);

        try {

            order.addItem(barCode1);
            order.increaseQuantityOf(barCode1);

            order.addItem(barCode2);
            order.increaseQuantityOf(barCode2);
            order.increaseQuantityOf(barCode2);

            order.addItem(barCode3);
            order.increaseQuantityOf(barCode3);

            customer.completeOrder(0);
            assertEquals(Customer.DEFAULT_MONEY - order.getTotalQuantity()* StockRecord.DEFAULT_PRICE, customer.getMoney());

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException | StockUnavailableException | NotEnoughMoneyException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void completeOrderNegativeIndex() {

        customer.createOrder(getFactory().makeShop());
        try {
            customer.completeOrder(-1);
        }
        catch (OrderAlreadyCompleteException | NotEnoughMoneyException | StockUnavailableException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void completeOrderNoOrders() {

        try {
            customer.completeOrder(0);
        }
        catch (OrderAlreadyCompleteException | NotEnoughMoneyException | StockUnavailableException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void completeOrderAboveBound() {

        customer.createOrder(getFactory().makeShop());

        try {
            customer.completeOrder(1);
        }
        catch (OrderAlreadyCompleteException | NotEnoughMoneyException | StockUnavailableException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = OrderAlreadyCompleteException.class)
    public void completeOrderTwice() throws OrderAlreadyCompleteException {

        IShop shop = getFactory().makeShop();
        String barCode = "1111";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            shop.addStock(barCode);
            shop.addStock(barCode);

        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

        IOrder order = customer.createOrder(shop);

        try {

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

            customer.completeOrder(0);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException | StockUnavailableException | NotEnoughMoneyException e) {
            fail(NOT_EXPECTED);
        }

        try {
            customer.completeOrder(0);
        }
        catch (NotEnoughMoneyException | StockUnavailableException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

    }

    @Test(expected = NotEnoughMoneyException.class)
    public void completeOrderNotEnoughMoney() throws NotEnoughMoneyException {

        IShop shop = getFactory().makeShop();
        String barCode = "1111";
        IProduct product = getFactory().makeProduct(barCode, null);

        try {

            shop.registerProduct(product);
            shop.setPriceOf(barCode, 200);
            shop.addStock(barCode);
            shop.addStock(barCode);

        }
        catch (BarCodeAlreadyInUseException | ProductNotRegisteredException e) {
            fail(NOT_EXPECTED);
        }

        IOrder order = customer.createOrder(shop);

        try {

            order.addItem(barCode);
            order.increaseQuantityOf(barCode);

        }
        catch (ProductNotRegisteredException | BarCodeAlreadyInUseException | OrderAlreadyCompleteException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

        try {
            customer.completeOrder(0);
        }
        catch (OrderAlreadyCompleteException | ProductNotRegisteredException | StockUnavailableException e) {
            fail(NOT_EXPECTED);
        }

    }

}
