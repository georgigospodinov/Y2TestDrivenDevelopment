package uk.ac.standrews.cs.cs2001.w03.impl;

import uk.ac.standrews.cs.cs2001.w03.interfaces.IProduct;

import java.util.Random;

/**
 * This class represents products that can be stocked and sold in a shop.
 */
public class Product implements IProduct {

    /**
     * This is the maximum length of a string generated by the
     * {@link Product#generateRandomString()} method.
     */
    private static final int MAX_GENERATED_STRING_LENGTH = 15;

    /**
     * The bar code of the product.
     */
    private String barCode;
    /**
     * The description of the product.
     */
    private String description;

    /**
     * Generates a random string. The length of the generated string
     * is no more than {@link Product#MAX_GENERATED_STRING_LENGTH}.
     * This method is used in the constructor if null values are passed to it.
     *
     * @return the string generated
     */
    private static String generateRandomString() {

        Random random = new Random();
        int stringLength = random.nextInt(MAX_GENERATED_STRING_LENGTH);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            sb.append((char) random.nextInt(Character.MAX_VALUE));
        }

        return sb.toString();

    }

    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Creates a new {@link Product} instance with the given bar code and description.
     * Calls {@link Product#generateRandomString()}
     * to set any of the fields if null is passed.
     *
     * @param barCode     the product's bar code
     * @param description the product's description
     */
    Product(String barCode, String description) {

        if (barCode == null) {
            barCode = generateRandomString();
        }

        if (description == null) {
            description = generateRandomString();
        }

        this.barCode = barCode;
        this.description = description;
    }

    /**
     * Determines if a given object equals this product.
     *
     * @param obj Object to compare to this one.
     * @return True if obj is an instance of {@link IProduct}
     * and has the {@link Product#barCode}. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        return (obj != null) && (obj instanceof IProduct)
                && (this.getBarCode().equals(((IProduct) obj).getBarCode()));

    }

    /**
     * Returns the hash code of the {@link Product#barCode}.
     * That is the field used when comparing products.
     *
     * @return the bar code's hash code.
     */
    @Override
    public int hashCode() {
        return this.getBarCode().hashCode();
    }
}