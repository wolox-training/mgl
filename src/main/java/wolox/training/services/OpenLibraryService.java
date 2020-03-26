package wolox.training.services;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.dto.OpenLibraryBookDTO;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Service
public class OpenLibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Value("${openLibrary.baseUrl}")
    private String baseUrl;

    public OpenLibraryService() {
    }

    public ResponseEntity<Book> search(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);

        if (book.isPresent()) {
            return new ResponseEntity<Book>(book.get(), HttpStatus.OK);
        }

        OpenLibraryBookDTO bookDTO = bookInfo(isbn);

        if (bookDTO == null) {
            throw new BookNotFoundException();
        }

        Book newBook = new Book("no genre", bookDTO.getJoinedAuthors(),
            bookDTO.getCover().getSmall(),
            bookDTO.getTitle(), bookDTO.getSubtitle(), bookDTO.getJoinedPublishers(),
            bookDTO.getPublishDate(), bookDTO.getNumberOfPages(), isbn);

        newBook = bookRepository.save(newBook);

        return new ResponseEntity<Book>(newBook, HttpStatus.CREATED);
    }

    public OpenLibraryBookDTO bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/books?bibkeys=ISBN:{isbn}&format=json&jscmd=data";

        Map<String, OpenLibraryBookDTO> res = restTemplate.exchange(url, HttpMethod.GET, null,
            new ParameterizedTypeReference<Map<String, OpenLibraryBookDTO>>() {
            }, isbn).getBody();

        return res.get("ISBN:" + isbn);
    }
}

