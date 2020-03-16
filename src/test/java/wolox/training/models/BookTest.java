package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFindByAuthor_thenReturnBook() {
        Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
            "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
            "1979", 180, "0-330-25864-8");

        entityManager.persist(book);
        entityManager.flush();

        Optional<Book> found = bookRepository.findByAuthor(book.getAuthor());

        assertThat(found.get().getAuthor())
            .isEqualTo(book.getAuthor());
    }

    @Test
    public void whenInitializeBookWithoutGenre_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book(null, "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutAuthor_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", null, "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutImage_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", null,
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutTitle_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                null, "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "", "placeholder", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutSubtitle_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", null, "Pan Books",
                "1979", 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "", "Pan Books",
                "1979", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutPublisher_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", null,
                "1979", 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "",
                "1979", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutYear_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                null, 180, "0-330-25864-8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "", 180, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutPages_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", null, "0-330-25864-8");
        });
    }

    @Test
    public void whenInitializeBookWithoutIsbn_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 180, "");
        });
    }

    @Test
    public void whenInitializeBookWithWrongPages_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book book = new Book("Science Fiction", "Douglas Adams", "image.jpg",
                "The Hitchhiker's Guide to the Galaxy", "placeholder", "Pan Books",
                "1979", 0, "0-330-25864-8");
        });
    }
}
