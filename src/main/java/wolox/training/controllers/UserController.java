package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

/**
 * Controller for {@link User}
 *
 * @author M. G.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private BookRepository bookRepository;

    /**
     * Find all {@link User}s.
     *
     * @return all the users that are persisted
     */

    @GetMapping
    public Iterable findAll() {
        return userRepository.findAll();
    }

    /**
     * Find a {@link User}.
     *
     * @param id the id of the user
     * @return the user found or an exception otherwise
     */

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Create a {@link User}.
     *
     * @param user the user to be created
     * @return the user persisted
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * Delete a {@link User}.
     *
     * @param id the id of the user to be deleted
     */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    /**
     * Update a {@link User}.
     *
     * @param user the details to be updated of the user
     * @param id   the id of the user to be updated
     * @return the user persisted
     */

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    /**
     * Add a {@link Book} to a collection of a {@link User}.
     *
     * @param user_id the id of the user
     * @param book_id the id of the book
     */

    @PostMapping("/{user_id}/books/{book_id}")
    public void addBookToUser(@PathVariable Long user_id, @PathVariable Long book_id) {
        User user = userRepository.findById(user_id)
            .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(book_id)
            .orElseThrow(BookNotFoundException::new);

        user.addBook(book);
    }

    /**
     * Remove a {@link Book} from the collection of a {@link User}.
     *
     * @param user_id the id of the user
     * @param book_id the id of the book
     */

    @DeleteMapping("/{user_id}/books/{book_id}")
    public void deleteBookToUser(@PathVariable Long user_id, @PathVariable Long book_id) {
        User user = userRepository.findById(user_id)
            .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(book_id)
            .orElseThrow(BookNotFoundException::new);

        user.deleteBook(book);
    }
}
