package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
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

    /**
     * Find a {@link User} by its birthDay in a range and by its name (case insensitive).
     *
     * @param startDate the start date where the birthDate should be included
     * @param endDate   the end date where the birthDate should be included
     * @param name      the name of the user (case insensitive)
     * @return the user if it exists, and otherwise Optional.empty() object.
     */
    List<User> findByBirthDateBetweenAndNameIgnoreCase(LocalDate startDate, LocalDate endDate,
        String name);
}
