package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

/**
 * Interface that serves as a Repository for the Book model.
 *
 * @author M. G.
 */

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

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
     * @param pageable  the parameters about pagination and sorting of the books
     * @return the book if it exists, and otherwise Optional.empty() object.
     */

    @Query("SELECT u FROM Book u WHERE (:publisher IS NULL OR u.publisher = :publisher) AND"
        + " (:genre IS NULL OR u.genre = :genre) AND (:year IS NULL OR u.year = :year)")
    Page<Book> findByPublisherAndGenreAndYear(@Param("publisher") String publisher,
        @Param("genre") String genre, @Param("year") String year, Pageable pageable);

    /**
     * Find all {@link Book}s that matches a specific criteria.
     *
     * @param id        the id of the books
     * @param genre     the genre of the books
     * @param author    the author of the books
     * @param image     the image of the books
     * @param title     the title of the books
     * @param subtitle  the subtitle of the books
     * @param publisher the publisher of the books
     * @param year      the year of the books
     * @param pages     the pages of the books
     * @param isbn      the isbn of the books
     * @param pageable  the parameters about pagination and sorting of the books
     * @return a list of the books that are persisted and match the criteria
     */

    @Query(
        "SELECT u FROM Book u WHERE (:id IS NULL OR u.id = :id) AND "
            + "(:genre IS NULL OR u.genre = :genre) AND "
            + "(:author IS NULL OR u.author = :author) AND "
            + "(:image IS NULL OR u.image = :image) AND "
            + "(:title IS NULL OR u.title = :title) AND "
            + "(:subtitle IS NULL OR u.subtitle = :subtitle) AND "
            + "(:publisher IS NULL OR u.publisher = :publisher) AND "
            + "(:year IS NULL OR u.year = :year) AND "
            + "(:pages IS NULL OR u.pages = :pages) AND"
            + " (:isbn IS NULL OR u.isbn = :isbn)")
    Page<Book> findByAllFields(@Param("id") Long id,
        @Param("genre") String genre,
        @Param("author") String author, @Param("image") String image,
        @Param("title") String title, @Param("subtitle") String subtitle,
        @Param("publisher") String publisher, @Param("year") String year,
        @Param("pages") Integer pages, @Param("isbn") String isbn, Pageable pageable);
}
