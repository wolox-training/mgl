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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.OpenLibraryService;
import wolox.training.services.PasswordEncoderService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookController.class})
@AutoConfigureMockMvc(addFilters = false)

public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository repository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoderService passwordEncoderService;

    @MockBean
    private OpenLibraryService openLibraryService;

    @WithMockUser("test")
    @Test
    public void givenBooks_whenGetAllBooks_thenReturnJsonArray()
        throws Exception {

        Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");
        Book fakeBook = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Fake Books",
            "1979", 180, "0-330-25864-8");

        List<Book> allBooks = Arrays.asList(book, fakeBook);

        given(
            repository.findByAllFields(null, null, null, null, null,
                null, null, null, null, null))
            .willReturn(allBooks);

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$[0].publisher", is("Pan Books")))
            .andExpect(jsonPath("$[1].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$[1].publisher", is("Fake Books")));
    }

    @WithMockUser("test")
    @Test
    public void givenBooks_whenGetSomeBooks_thenReturnJsonArray()
        throws Exception {

        Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        Book fakeBook = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Fake Books",
            "1979", 180, "0-330-25864-8");

        List<Book> allBooks = Arrays.asList(book, fakeBook);

        given(repository.findByAllFields(null, null, null, null, null,
            null, null, null, null, null)).willReturn(allBooks);

        given(repository.findByAllFields(null, null, null, null, null,
            null, "Pan Books", null, null, null)).willReturn(Arrays.asList(book));

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .param("publisher", "Pan Books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$[0].publisher", is("Pan Books")));
    }

    @WithMockUser("test")
    @Test
    public void givenNoBooks_whenGetAllBooks_thenReturnEmptyJsonArray()
        throws Exception {
        List<Book> allBooks = new ArrayList<Book>();

        given(repository.findByAllFields(null, null, null, null, null,
            null, null, null, null, null)).willReturn(allBooks);

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser("test")
    @Test
    public void givenBook_whenGetABook_thenReturnJson()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(repository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(get("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("The Hitchhiker's Guide to the Galaxy")));
    }

    @WithMockUser("test")
    @Test
    public void givenNoBook_whenGetABook_thenReturnJsonError()
        throws Exception {
        mvc.perform(get("/api/books/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidBook_whenCreateABook_thenReturnOk()
        throws Exception {

        Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(book);

        mvc.perform(post("/api/books")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @WithMockUser("test")
    @Test
    public void givenBook_whenDeleteABook_thenReturnOk()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(repository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(delete("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @WithMockUser("test")
    @Test
    public void givenNoBook_whenDeleteABook_thenReturnNotFound()
        throws Exception {
        mvc.perform(delete("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser("test")
    @Test
    public void givenAValidBook_whenEditABook_thenReturnOk()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(repository.findById(1L)).willReturn(Optional.of(book));

        book.setPages(181);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(book);

        mvc.perform(put("/api/books/1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @WithMockUser("test")
    @Test
    public void givenAValidBookAndInvalidId_whenEditABook_thenReturnBadRequest()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        given(repository.findById(1L)).willReturn(Optional.of(book));

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(book);

        mvc.perform(put("/api/books/2")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @WithMockUser("test")
    @Test
    public void givenNoBook_whenEditABook_thenReturnNotFound()
        throws Exception {

        Book book = new Book(1, "Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(book);

        mvc.perform(put("/api/books/1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
