package wolox.training.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void givenUsers_whenGetAllUsers_thenReturnJsonArray()
        throws Exception {

        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1));

        List<User> allUsers = Arrays.asList(user);

        given(userRepository.findAll()).willReturn(allUsers);

        mvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].username", is("mary")));
    }

    @Test
    public void givenNoUsers_whenGetAllUsers_thenReturnEmptyJsonArray()
        throws Exception {
        List<User> allUsers = new ArrayList<User>();

        given(userRepository.findAll()).willReturn(allUsers);

        mvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenUser_whenGetAUser_thenReturnJson()
        throws Exception {

        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(get("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("mary")));
    }

    @Test
    public void givenNoUser_whenGetAUser_thenReturnJsonError()
        throws Exception {
        mvc.perform(get("/api/users/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidUser_whenCreateAUser_thenReturnOk()
        throws Exception {

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(post("/api/users")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void givenUser_whenDeleteAUser_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNoUser_whenDeleteAUser_thenReturnNotFound()
        throws Exception {
        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidUser_whenEditAUser_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary L. Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(put("/api/users/1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenAValidUserAndInvalidId_whenEditAUser_thenReturnBadRequest()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary L. Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(put("/api/users/2")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNoUser_whenEditAUser_thenReturnNotFound()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary L. Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(put("/api/users/1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAUser_whenAddingABook_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNoUser_whenAddingABook_thenReturnError()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAUser_whenAddingAnInvalidBook_thenReturnError()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAUser_whenDeletingABook_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNoUser_whenDeletingABook_thenReturnNotFound()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAUser_whenDeletingAnInvalidBook_thenReturnNotFound()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1),
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
