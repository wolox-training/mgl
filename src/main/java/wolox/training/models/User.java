package wolox.training.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import wolox.training.exceptions.BookAlreadyOwnedException;

/**
 * Model for users
 *
 * @author M. G.
 */

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate birthDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_user",
        joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<Book> books = new ArrayList<Book>();

    public User() {
    }

    public User(long id, String username, String name, LocalDate birthDate,
        List<Book> books) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Add a {@link Book} to the list of books of this user
     *
     * @param book the book to be added
     */

    public void addBook(Book book) {
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }

        this.books.add(book);
    }

    /**
     * Remove a {@link Book} of the list of books of this user
     *
     * @param book the book to be removed
     */

    public void deleteBook(Book book) {
        books.remove(book);
    }
}
