package wolox.training.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
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
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookController.class, OpenLibraryService.class})
@AutoConfigureMockMvc(addFilters = false)

public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookRepository repository;

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

        Page<Book> allBooks = new PageImpl<Book>(Arrays.asList(book, fakeBook));

        given(
            repository.findByAllFields(null, null, null, null, null,
                null, null, null, null, null, PageRequest.of(0, 2)))
            .willReturn(allBooks);

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "0")
            .param("size", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$.content[0].publisher", is("Pan Books")))
            .andExpect(jsonPath("$.content[1].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$.content[1].publisher", is("Fake Books")));
    }

    @WithMockUser("test")
    @Test
    public void givenBooks_whenGetAllBooksPaged_thenReturnJsonArray()
        throws Exception {

        Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");
        Book fakeBook = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Fake Books",
            "1979", 180, "0-330-25864-8");

        Page<Book> allBooks = new PageImpl<Book>(Arrays.asList(book, fakeBook));

        given(
            repository.findByAllFields(null, null, null, null, null,
                null, null, null, null, null, Pageable.unpaged()))
            .willReturn(allBooks);

        given(
            repository.findByAllFields(null, null, null, null, null,
                null, null, null, null, null, PageRequest.of(0, 1, Sort.by("publisher"))))
            .willReturn(new PageImpl<Book>(Arrays.asList(fakeBook)));

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "0")
            .param("size", "1")
            .param("sort", "publisher,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$.content[0].publisher", is("Fake Books")));
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

        Page<Book> allBooks = new PageImpl<Book>(Arrays.asList(book, fakeBook));

        given(repository.findByAllFields(null, null, null, null, null,
            null, null, null, null, null, PageRequest.of(0, 2))).willReturn(allBooks);

        given(repository.findByAllFields(null, null, null, null, null,
            null, "Pan Books", null, null, null, PageRequest.of(0, 2)))
            .willReturn(new PageImpl<Book>(Arrays.asList(book)));

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .param("publisher", "Pan Books")
            .param("page", "0")
            .param("size", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].title", is("The Hitchhiker's Guide to the Galaxy")))
            .andExpect(jsonPath("$.content[0].publisher", is("Pan Books")));
    }

    @WithMockUser("test")
    @Test
    public void givenNoBooks_whenGetAllBooks_thenReturnEmptyJsonArray()
        throws Exception {
        Page<Book> allBooks = new PageImpl<Book>(new ArrayList<Book>());

        given(repository.findByAllFields(null, null, null, null, null,
            null, null, null, null, null, PageRequest.of(0, 1)))
            .willReturn(allBooks);

        mvc.perform(get("/api/books")
            .param("page", "0")
            .param("size", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)));
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


    @WithMockUser("test")
    @Test
    public void givenIsbn_whenSearchForExistingBook_thenReturnOk()
        throws Exception {

        Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0330258648");

        given(repository.findByIsbn(book.getIsbn())).willReturn(Optional.of(book));

        mvc.perform(get("/api/books/search/" + book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("The Hitchhiker's Guide to the Galaxy")));
    }


    @WithMockUser("test")
    @Test
    public void givenIsbn_whenSearchForNonExistingBook_thenReturnCreated()
        throws Exception {

        Book book = new Book("no genre", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0330258648");

        given(repository.save(book)).willReturn(book);

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.givenThat(
            WireMock.get(urlEqualTo("/api/books?bibkeys=ISBN:0330258648&format=json&jscmd=data"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("response_ok_book.json")));

        wireMockServer.start();

        mvc.perform(get("/api/books/search/" + book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title", is(book.getTitle())));

        wireMockServer.stop();
    }

    @WithMockUser("test")
    @Test
    public void givenIsbn_whenSearchForNonExistingBookInvalid_thenReturnNotFound()
        throws Exception {

        Book book = new Book("no genre", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0330258648");

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.givenThat(
            WireMock.get(urlEqualTo("/api/books?bibkeys=ISBN:0330258648&format=json&jscmd=data"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("response_book_not_found.json")));

        wireMockServer.start();

        mvc.perform(get("/api/books/search/" + book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        wireMockServer.stop();
    }
}
