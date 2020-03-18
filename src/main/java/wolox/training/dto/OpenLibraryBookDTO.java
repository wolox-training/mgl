package wolox.training.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OpenLibraryBookDTO implements Serializable {

    private String isbn;
    private String title;
    private String subtitle;
    private List<OpenLibraryPublisherDTO> publishers;
    private String publishDate;
    private Integer numberOfPages;
    private List<OpenLibraryAuthorDTO> authors;
    private OpenLibraryCoverDTO cover;

    public OpenLibraryBookDTO() {
    }

    public OpenLibraryBookDTO(String isbn, String title, String subtitle,
        List<OpenLibraryPublisherDTO> publishers,
        String publishDate, Integer numberOfPages, List<OpenLibraryAuthorDTO> authors,
        OpenLibraryCoverDTO cover) {
        setIsbn(isbn);
        setTitle(title);
        setSubtitle(subtitle);
        setPublishers(publishers);
        setPublishDate(publishDate);
        setNumberOfPages(numberOfPages);
        setAuthors(authors);
        setCover(cover);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<OpenLibraryPublisherDTO> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<OpenLibraryPublisherDTO> publishers) {
        this.publishers = publishers;
    }

    public String getJoinedPublishers() {
        ArrayList<String> names = new ArrayList<String>();

        for (OpenLibraryPublisherDTO elem : getPublishers()) {
            names.add(elem.getName());
        }

        return String.join(",", names);
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<OpenLibraryAuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<OpenLibraryAuthorDTO> authors) {
        this.authors = authors;
    }

    public String getJoinedAuthors() {
        ArrayList<String> names = new ArrayList<String>();

        for (OpenLibraryAuthorDTO elem : getAuthors()) {
            names.add(elem.getName());
        }

        return String.join(",", names);
    }

    public OpenLibraryCoverDTO getCover() {
        return cover;
    }

    public void setCover(OpenLibraryCoverDTO cover) {
        this.cover = cover;
    }
}
