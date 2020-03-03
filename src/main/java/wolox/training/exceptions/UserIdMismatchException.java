package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.models.User;

/**
 * Exception when the Id does not match the id in the {@link User} object.
 *
 * @author M. G.
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User Id Mismatch")
public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
        super();
    }
}
