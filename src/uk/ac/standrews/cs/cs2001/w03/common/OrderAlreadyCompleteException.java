package uk.ac.standrews.cs.cs2001.w03.common;

import uk.ac.standrews.cs.cs2001.w03.interfaces.IOrder;

/**
 * This exception should be used to tell that an {@link IOrder} is marked as complete and cannot be amended any more.
 *
 * @author 150009974
 * @version 1.0
 */
public class OrderAlreadyCompleteException extends Exception {
}
