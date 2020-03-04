package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.models.Book;

/**
 * Exception raised when a {@link Book} is added to an User that already had it.
 *
 * @author M. G.
 */

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Book Already Owned")
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super();
    }
}
