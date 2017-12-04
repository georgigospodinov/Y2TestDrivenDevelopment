package uk.ac.standrews.cs.cs2001.w03.interfaces;

/**
 * Interface for a shop product ADT.
 * 
 */
public interface IProduct {

    /**
     * This method returns the product's bar code.
     * @return the bar code for this product
     */
    String getBarCode();

    /**
     * This method returns the product description.
     * @return the description of the product
     */
    String getDescription();

}
