package info.gamlor.icoodb.web.model;

import org.testng.annotations.Test;

import static info.gamlor.icoodb.web.testutils.PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class TestRSVP {

    @Test
    public void hasDinnerSet() {
        Dinner dinner = new Dinner();
        RSVP rsvp = dinner.createRSVP(new UserIdentity("localhost"));
        assertEquals(rsvp.getDinner(), dinner);
    }

    @Test
    public void titleNotNnullOnNewInstance() {
        RSVP rsvp = new Dinner().createRSVP(new UserIdentity("localhost"));
        assertNotNull(rsvp.getName());
    }

    @Test
    public void getSetTitle() {
        assertCanSetAndGetWithEvents(new Dinner().createRSVP(new UserIdentity("localhost")), "name", "Name");
    }
}
