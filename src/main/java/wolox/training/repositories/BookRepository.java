package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import wolox.training.models.Book;

/**
 * Interface that serves as a Repository for the Book model.
 *
 * @author M. G.
 */

public interface BookRepository extends Repository<Book, Long> {

    /**
     * Find a book by its author.
     *
     * @param author the name of the author
     * @return the book if it exists, and otherwise Optional.empty() object.
     */
    Optional<Book> findByAuthor(String author);
}
