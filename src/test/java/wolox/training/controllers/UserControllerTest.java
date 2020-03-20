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

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)

public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserService userService;

    @WithMockUser("test")
    @Test
    public void givenUsers_whenGetAllUsers_thenReturnJsonArray()
        throws Exception {

        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        User newUser = new User("Mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");

        Page<User> allUsers = new PageImpl<User>(Arrays.asList(user, newUser));

        given(userRepository.findAll(PageRequest.of(0, 2))).willReturn(allUsers);

        mvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "0")
            .param("size", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].username", is("mary")))
            .andExpect(jsonPath("$.content[1].username", is("Mary")));
    }

    @WithMockUser("test")
    @Test
    public void givenNoUsers_whenGetAllUsers_thenReturnEmptyJsonArray()
        throws Exception {
        Page<User> allUsers = new PageImpl<User>(new ArrayList<User>());

        given(userRepository.findAll(PageRequest.of(0, 1))).willReturn(allUsers);

        mvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "0")
            .param("size", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @WithMockUser("test")
    @Test
    public void givenUser_whenGetAUser_thenReturnJson()
        throws Exception {

        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(get("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("mary")));
    }

    @WithMockUser("test")
    @Test
    public void givenNoUser_whenGetAUser_thenReturnJsonError()
        throws Exception {
        mvc.perform(get("/api/users/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser("test")
    @Test
    public void givenAValidUser_whenCreateAUser_thenReturnOk()
        throws Exception {

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary Lewis\", \"birthDate\": \"1990-01-01\", \"password\": \"lewis\"}";

        mvc.perform(post("/api/users")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @WithMockUser("test")
    @Test
    public void givenUser_whenDeleteAUser_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @WithMockUser("test")
    @Test
    public void givenNoUser_whenDeleteAUser_thenReturnNotFound()
        throws Exception {
        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser("test")
    @Test
    public void givenAValidUser_whenEditAUser_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary L. Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(put("/api/users/1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @WithMockUser("test")
    @Test
    public void givenAValidUserAndInvalidId_whenEditAUser_thenReturnBadRequest()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary L. Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(put("/api/users/2")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @WithMockUser("test")
    @Test
    public void givenNoUser_whenEditAUser_thenReturnNotFound()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        String json = "{\"id\" :1, \"username\" :\"mary\", \"name\" :\"Mary L. Lewis\", \"birthDate\": \"1990-01-01\"}";

        mvc.perform(put("/api/users/1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser("test")
    @Test
    public void givenAUser_whenAddingABook_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
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

    @WithMockUser("test")
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

    @WithMockUser("test")
    @Test
    public void givenAUser_whenAddingAnInvalidBook_thenReturnError()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser("test")
    @Test
    public void givenAUser_whenDeletingABook_thenReturnOk()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
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

    @WithMockUser("test")
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

    @WithMockUser("test")
    @Test
    public void givenAUser_whenDeletingAnInvalidBook_thenReturnNotFound()
        throws Exception {

        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser("mary")
    @Test
    public void givenAUser_thenReturnOk() throws Exception {
        User user = new User(1, "mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis",
            new ArrayList<Book>());

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return user.getUsername();
            }
        };

        mvc.perform(get("/api/users/me")
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("mary")));
    }
}
