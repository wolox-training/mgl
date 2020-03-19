package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

/**
 * Interface that serves as a Repository for the Book model.
 *
 * @author M. G.
 */

public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Find a book by its author.
     *
     * @param author the name of the author
     * @return the book if it exists, and otherwise Optional.empty() object.
     */
    Optional<Book> findByAuthor(String author);

    /**
     * Find a book by its isbn.
     *
     * @param isbn the isbn of the book
     * @return the book if it exists, and otherwise Optional.empty() object.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find all books that matches a specific publisher, genre and year.
     *
     * @param publisher the name of the publisher
     * @param genre     the genre of the book
     * @param year      the year of the book
     * @return the book if it exists, and otherwise Optional.empty() object.
     */
    List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);
}
