package wolox.training.dto;

public class OpenLibraryPublisherDTO {

    private String name;

    public OpenLibraryPublisherDTO() {
    }

    public OpenLibraryPublisherDTO(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
