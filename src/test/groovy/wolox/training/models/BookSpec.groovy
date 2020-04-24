package wolox.training.models

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import wolox.training.repositories.BookRepository

@DataJpaTest
class BookSpec extends Specification {

    @Autowired
    TestEntityManager entityManager

    @Autowired
    BookRepository bookRepository

    def "persisting books"() {
        given: "seed the books table"
        entityManager.persist(new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8"))

        expect: "the number of books is 1"
        bookRepository.count() == 1L
        and: "the book is persisted"
        bookRepository.findByAuthor("Douglas Adams").get().getTitle() == "The Hitchhiker's Guide to the Galaxy"
    }

    def "finding by year"() {
        def book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8")

        def fakeBook = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Fake Books",
                "1979", 180, "0-330-25864-9")

        given: "seed the books table"
        entityManager.persist(book)
        entityManager.persist(fakeBook)

        expect: "it only returns the first book"
        bookRepository.findByPublisherAndGenreAndYear(null, null, book.getYear(),
                PageRequest.of(0, 1)).get().toArray() == Arrays.asList(book)
    }

    def "creating without a Genre"() {
        when: "creating the book"
        def book = new Book(null, "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8")

        then:
        thrown(IllegalArgumentException)
    }
}