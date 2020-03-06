package wolox.training.models;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import wolox.training.exceptions.BookAlreadyOwnedException;

/**
 * Model for users
 *
 * @author M. G.
 */

@Entity
@Table(name = "users")
@ApiModel(description = "Users for the Book Management System")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ApiModelProperty(notes = "The nickname that will use a user to identify itself")
    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthDate;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private List<Book> books = new ArrayList<Book>();

    public User() {
    }

    public User(long id, String username, String name, LocalDate birthDate,
        List<Book> books) {
        this.id = id;
        setUsername(username);
        setName(name);
        setBirthDate(birthDate);
        setBooks(books);
    }

    public User(String username, String name, LocalDate birthDate) {
        setUsername(username);
        setName(name);
        setBirthDate(birthDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkArgument(username != null && !username.isEmpty());

        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkArgument(name != null && !name.isEmpty());

        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        Preconditions.checkArgument(birthDate != null && birthDate.isBefore(LocalDate.now()));

        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = Preconditions.checkNotNull(books);
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
