package info.gamlor.icoodb.web.dto;

/**
 * The complete dinner, intended for editing a dinner
 *
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class DinnerViewModel extends DinnerViewModelBase {
    private String description = "";
    private String contactPhone = "";
    private String country = "";

    public DinnerViewModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
