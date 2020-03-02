package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * BookNotFoundException.java Exception when the book is not found.
 *
 * @author M. G.
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Book Not Found")
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super();
    }
}
