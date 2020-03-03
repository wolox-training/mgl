package wolox.training.exceptions;

import wolox.training.models.Book;

/**
 * Exception raised when a {@link Book} is added to an User that already had it.
 *
 * @author M. G.
 */

public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super();
    }
}
