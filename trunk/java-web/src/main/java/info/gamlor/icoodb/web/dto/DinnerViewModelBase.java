package info.gamlor.icoodb.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author roman.stoffel@gamlor.info
 * @since 25.08.2010
 */
public class DinnerViewModelBase {
    private int id;
    @NotEmpty
    private String title = "";
    @NotEmpty
    private String eventDate = "";
    @NotEmpty
    private String eventTime = "";
    @NotEmpty
    private String hostedBy = "";
    @NotEmpty
    private String address = "";


    private double latitude = 0.0;
    private double longitude = 0.0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getHostedBy() {
        return hostedBy;
    }

    public void setHostedBy(String hostedBy) {
        this.hostedBy = hostedBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEventTime() {
        return eventTime;
    }
}
