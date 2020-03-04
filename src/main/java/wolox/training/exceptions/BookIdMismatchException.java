package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.models.Book;

/**
 * Exception when the Id does not match the id in the {@link Book} object.
 *
 * @author M. G.
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Book Id Mismatch")
public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super();
    }
}
