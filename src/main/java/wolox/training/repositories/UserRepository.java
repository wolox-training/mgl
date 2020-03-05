package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

/**
 * Interface that serves as a Repository for {@link User}.
 *
 * @author M. G.
 */

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find a {@link User} by its username.
     *
     * @param username the name of the user
     * @return the user if it exists, and otherwise Optional.empty() object.
     */
    Optional<User> findByUsername(String username);
}
