package wolox.training.dto;

public class OpenLibraryCoverDTO {

    private String small;
    private String medium;
    private String large;

    public OpenLibraryCoverDTO() {
    }

    public OpenLibraryCoverDTO(String small, String medium, String large) {
        setSmall(small);
        setMedium(medium);
        setLarge(large);
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
