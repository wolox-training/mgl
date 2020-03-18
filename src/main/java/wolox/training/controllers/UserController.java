package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import wolox.training.services.UserService;

/**
 * Controller for {@link User}
 *
 * @author M. G.
 */

@RestController
@RequestMapping("/api/users")
@Api
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    /**
     * Show the current logged {@link User}s name.
     *
     * @return the name of the user
     */

    @GetMapping("/me")
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

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
    @ApiOperation(value = "Giving attributes of a user, creates a user", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "User successfully created"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
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
     * @param modifiedUser the details to be updated of the user
     * @param id           the id of the user to be updated
     * @return the user persisted
     */

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User modifiedUser, @PathVariable Long id) {
        if (modifiedUser.getId() != id) {
            throw new UserIdMismatchException();
        }

        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return userService.updateUser(user, modifiedUser);
    }

    /**
     * Update a {@link User}'s password.
     *
     * @param newPassword the new password for this user
     * @param id          the id of the user to be updated
     * @return the user persisted
     */

    @PutMapping("/{id}/password")
    public User updatePassword(@RequestHeader(value = "New-Password") String newPassword,
        @PathVariable Long id) {

        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return userService.updatePassword(user, newPassword);
    }

    /**
     * Add a {@link Book} to a collection of a {@link User}.
     *
     * @param user_id the id of the user
     * @param book_id the id of the book
     */

    @PostMapping("/{user_id}/books/{book_id}")
    public User addBookToUser(@PathVariable Long user_id, @PathVariable Long book_id) {
        User user = userRepository.findById(user_id)
            .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(book_id)
            .orElseThrow(BookNotFoundException::new);

        user.addBook(book);

        return userRepository.save(user);
    }

    /**
     * Remove a {@link Book} from the collection of a {@link User}.
     *
     * @param user_id the id of the user
     * @param book_id the id of the book
     */

    @DeleteMapping("/{user_id}/books/{book_id}")
    public User deleteBookToUser(@PathVariable Long user_id, @PathVariable Long book_id) {
        User user = userRepository.findById(user_id)
            .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(book_id)
            .orElseThrow(BookNotFoundException::new);

        user.deleteBook(book);

        return userRepository.save(user);
    }
}
