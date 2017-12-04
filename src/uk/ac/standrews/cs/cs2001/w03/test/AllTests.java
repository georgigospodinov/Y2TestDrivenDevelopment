package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This is a suite test class, used to run all test classes at once.
 *
 * @author 150009974
 * @version 1.0
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CustomerTest.class,
        OrderTest.class,
        FactoryTest.class,
        ProductTest.class,
        StockRecordTest.class,
        ShopTest.class
})
public class AllTests {
}
