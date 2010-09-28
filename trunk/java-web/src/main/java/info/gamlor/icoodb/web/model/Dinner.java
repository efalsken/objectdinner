package info.gamlor.icoodb.web.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class Dinner extends IDHolder {
    private String title = "";
    private Date eventDate = new DateTime().plusHours(6).toDate();
    private String description = "";
    private String hostedBy = "";
    private UserIdentity hostedByIdentity;
    private String contactPhone = "";
    private String address = "";
    private String country = "";
    private double latitude = 0.0;
    private double longitude = 0.0;


    private final List<RSVP> attendees = new ArrayList<RSVP>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHostedBy() {
        return hostedBy;
    }

    public void setHostedBy(String hostedBy) {
        this.hostedBy = hostedBy;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public void setHostedByIdentity(UserIdentity hostedByIdentity) {
        this.hostedByIdentity = hostedByIdentity;
    }

    public UserIdentity getHostedByIdentity() {
        return hostedByIdentity;
    }

    public RSVP createRSVP(UserIdentity identity) {
        RSVP rsvp = new RSVP(this, identity);
        addAttendee(rsvp);
        return rsvp;
    }

    public List<RSVP> getAttendees() {
        return Collections.unmodifiableList(attendees);
    }

    public void removeRSVP(RSVP currentRSVP) {
        this.attendees.remove(currentRSVP);
    }

    private void addAttendee(RSVP rsvp) {
        attendees.add(rsvp);
    }
}
