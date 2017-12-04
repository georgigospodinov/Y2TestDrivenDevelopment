package uk.ac.standrews.cs.cs2001.w03.test;

import org.junit.Test;
import uk.ac.standrews.cs.cs2001.w03.common.AbstractFactoryClient;
import uk.ac.standrews.cs.cs2001.w03.impl.Product;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This is a JUnit test class for the {@link Product} class.
 *
 * @author 150009974
 * @version 1.1
 */
public class ProductTest extends AbstractFactoryClient {

    /**
     * The {@link IProduct} object used in the test methods.
     */
    private IProduct product = null;

    /**
     * Test to see if the {@link Product#getBarCode()} method correctly returns the {@link Product#barCode}.
     */
    @Test
    public void getBarCodeTest() {

        String barCode = "123-456";
        product = getFactory().makeProduct(barCode, "");
        assertEquals(barCode, product.getBarCode());

    }

    /**
     * Test to see if the {@link Product#getDescription()} method correctly returns the {@link Product#description}.
     */
    @Test
    public void getDescriptionTest() {

        String description = "des";
        product = getFactory().makeProduct("", description);
        assertEquals(description, product.getDescription());

    }

    /**
     * Test to see that if when a product is created with null values it's fields are not null.
     */
    @Test
    public void fieldsNeverNullTest() {

        product = getFactory().makeProduct(null, null);
        assertNotNull(product.getBarCode());
        assertNotNull(product.getDescription());

    }

    /**
     * Test to see if the {@link Product#equals(Object)} method correctly determines if two Products are the same.
     */
    @Test
    public void equalsTest() {

        String barCode = "1-2";
        product = getFactory().makeProduct(barCode, null);
        IProduct sameProduct = getFactory().makeProduct(barCode, null);

        assertEquals(product, sameProduct);
    }

}
