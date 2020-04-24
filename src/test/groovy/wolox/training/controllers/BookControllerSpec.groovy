package wolox.training.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import wolox.training.models.Book
import wolox.training.repositories.BookRepository
import wolox.training.services.OpenLibraryService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController.class)
@ContextConfiguration(classes = [BookController.class, OpenLibraryService.class])
@AutoConfigureMockMvc(addFilters = false)
class BookControllerSpec extends Specification {
    @Autowired
    MockMvc mvc

    def book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8")
    def fakeBook = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Fake Books",
            "1979", 180, "0-330-25864-8")

    def allBooks = new PageImpl<Book>(Arrays.asList(book, fakeBook))

    @SpringBean
    BookRepository bookRepository = Stub() {
        findByAllFields(null, "", null, null, null,
                null, null, null, null, null,
                PageRequest.of(0, 2)) >> allBooks
    }

    def "when get is performed"() {
        expect: "Status is 200"
        mvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
    }

    def "when editing a book with wrong is performed"() {
        def ow = new ObjectMapper().writer().withDefaultPrettyPrinter()
        def json = ow.writeValueAsString(book)

        expect: "Status is 400"
        mvc.perform(put("/api/books/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
    }
}
