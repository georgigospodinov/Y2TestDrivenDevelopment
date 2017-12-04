package uk.ac.standrews.cs.cs2001.w03.common;

import uk.ac.standrews.cs.cs2001.w03.impl.Factory;
import uk.ac.standrews.cs.cs2001.w03.interfaces.IFactory;

/**
 * Abstract base class for classes which need to use the Factory class.
 */
public abstract class AbstractFactoryClient {

    /**
     * A string that is printed when a test class receives an exception when
     * such is not expected.
     */
    protected static final String NOT_EXPECTED = "Exception was thrown when not expected!";

    /**
     * Holds the {@link IFactory} instance.
     * Only this instance is used in all project files.
     */
    private static IFactory factory = Factory.getInstance();

    /**
     * Method which returns an instance of IFactory.
     *
     * @return an instance of Factory
     */
    public static IFactory getFactory() {
        return factory;
    }
}
