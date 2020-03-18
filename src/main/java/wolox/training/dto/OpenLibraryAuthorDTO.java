package wolox.training.dto;

public class OpenLibraryAuthorDTO {

    private String name;

    public OpenLibraryAuthorDTO() {
    }

    public OpenLibraryAuthorDTO(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
