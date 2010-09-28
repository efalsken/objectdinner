package info.gamlor.icoodb.web.model;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.List;

import static info.gamlor.icoodb.web.testutils.PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents;
import static org.testng.Assert.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class TestDinner {

    @Test
    public void titleNotNullOnNewInstance() {
        Dinner dinner = new Dinner();
        assertNotNull(dinner.getTitle());
    }

    @Test
    public void getSetTitle() {
        assertCanSetAndGetWithEvents(new Dinner(), "title", "Title");
    }

    @Test
    public void newInstanceHasTodayAsDate() {
        DateTime now = new DateTime();
        Dinner dinner = new Dinner();
        DateTime dinnerTime = new DateTime(dinner.getEventDate());
        assertTrue(dinnerTime.isAfter(now)
                || dinnerTime.isEqual(now));
    }

    @Test
    public void getSetEventDate() {
        Dinner dinner = new Dinner();
        DateTime time = new DateTime(dinner.getEventDate()).minusHours(1);
        assertCanSetAndGetWithEvents(dinner, "eventDate", time.toDate());
    }

    @Test
    public void descriptionNotNotOnNewInstance() {
        Dinner dinner = new Dinner();
        assertNotNull(dinner.getDescription());
    }

    @Test
    public void getSetDescription() {
        assertCanSetAndGetWithEvents(new Dinner(), "description", "This is the description");
    }

    @Test
    public void hostedByNotNullOnNewInstance() {
        Dinner dinner = new Dinner();
        assertNotNull(dinner.getHostedBy());
    }

    @Test
    public void getSetHostedBy() {
        assertCanSetAndGetWithEvents(new Dinner(), "hostedBy", "Roman Stoffel");
    }

    @Test
    public void emptyContactPhone() {
        Dinner dinner = new Dinner();
        assertNotNull(dinner.getContactPhone());
    }

    @Test
    public void getSetPhoneNumber() {
        assertCanSetAndGetWithEvents(new Dinner(), "contactPhone", "01234");
    }

    @Test
    public void emptyAddress() {
        Dinner dinner = new Dinner();
        assertNotNull(dinner.getAddress());
    }

    @Test
    public void getSetAddress() {
        assertCanSetAndGetWithEvents(new Dinner(), "address", "Valleestrasse 155");
    }

    @Test
    public void emptyCountry() {
        Dinner dinner = new Dinner();
        assertNotNull(dinner.getCountry());
    }

    @Test
    public void getSetCountry() {
        assertCanSetAndGetWithEvents(new Dinner(), "country", "Switzerland");
    }

    @Test
    public void getSetLatitude() {
        assertCanSetAndGetWithEvents(new Dinner(), "latitude", 42.2);
    }

    @Test
    public void getSetLongitude() {
        assertCanSetAndGetWithEvents(new Dinner(), "longitude", 13.37);
    }

    @Test
    public void hasAttendees() {
        final List<RSVP> attendees = new Dinner().getAttendees();
        assertEquals(attendees.size(), 0);
    }

    @Test
    public void addingAttendee() {
        Dinner toTest = new Dinner();
        final RSVP rsvp = toTest.createRSVP(new UserIdentity("localhost"));
        final List<RSVP> attendees = toTest.getAttendees();
        assertEquals(attendees.size(), 1);
    }

    @Test
    public void removeAttendee() {
        Dinner toTest = new Dinner();
        final RSVP rsvp = toTest.createRSVP(new UserIdentity("localhost"));
        toTest.removeRSVP(rsvp);
        assertEquals(toTest.getAttendees().size(), 0);
    }
}
