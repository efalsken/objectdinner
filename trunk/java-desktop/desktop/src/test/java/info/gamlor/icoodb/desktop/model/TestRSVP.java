package info.gamlor.icoodb.desktop.model;

import org.testng.annotations.Test;

import static info.gamlor.icoodb.desktop.testutils.PropertyChangeEventsTestUtils.assertCanAddListener;
import static info.gamlor.icoodb.desktop.testutils.PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author roman.stoffel@gamlor.info
 * @since 27.07.2010
 */
public class TestRSVP {

    @Test
    public void canAddListener() {
        assertCanAddListener(new Dinner().createRSVP());
    }

    @Test
    public void hasDinnerSet() {
        Dinner dinner = new Dinner();
        RSVP rsvp = dinner.createRSVP();
        assertEquals(rsvp.getDinner(), dinner);
    }

    @Test
    public void titleNotNnullOnNewInstance() {
        RSVP rsvp = new Dinner().createRSVP();
        assertNotNull(rsvp.getName());
    }

    @Test
    public void getSetTitle() {
        assertCanSetAndGetWithEvents(new Dinner().createRSVP(), "name", "Name");
    }


}
