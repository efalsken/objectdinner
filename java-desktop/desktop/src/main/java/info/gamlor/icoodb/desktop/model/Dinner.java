package info.gamlor.icoodb.desktop.model;

import info.gamlor.icoodb.desktop.utils.ChangeEventSender;
import info.gamlor.icoodb.desktop.utils.PropertyEventsSupport;
import org.joda.time.DateTime;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 14.07.2010
 */
public class Dinner implements ChangeEventSender {
    private String title = "";
    private Date eventDate = new DateTime().plusHours(1).toDate();
    private String description = "";
    private String hostedBy = "";
    private String contactPhone = "";
    private String address = "";
    private String country = "";
    private final List<RSVP> attendees = new ArrayList<RSVP>();

    private final transient PropertyEventsSupport changeSupport = new PropertyEventsSupport(this);


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        changeSupport.firePropertyChange("title", title);
    }

    public Date getEventDate() {
        return new Date(eventDate.getTime());
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
        changeSupport.firePropertyChange("eventDate", eventDate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        changeSupport.firePropertyChange("description", description);
    }

    public String getHostedBy() {
        return hostedBy;
    }

    public void setHostedBy(String hostedBy) {
        this.hostedBy = hostedBy;
        changeSupport.firePropertyChange("hostedBy", hostedBy);
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        changeSupport.firePropertyChange("contactPhone", contactPhone);
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        changeSupport.firePropertyChange("address", address);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        changeSupport.firePropertyChange("country", country);
    }

    public List<RSVP> getAttendees() {
        return Collections.unmodifiableList(attendees);
    }

    public void addAttendee(RSVP rsvp) {
        attendees.add(rsvp);
        changeSupport.firePropertyChange("attendees", getAttendees());
    }

    public RSVP createRSVP() {
        RSVP rsvp = new RSVP(this);
        addAttendee(rsvp);
        return rsvp;
    }

    public void removeRSVP(RSVP currentRSVP) {
        this.attendees.remove(currentRSVP);
        changeSupport.firePropertyChange("attendees", getAttendees());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
