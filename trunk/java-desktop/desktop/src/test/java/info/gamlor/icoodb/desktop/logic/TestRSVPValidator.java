package info.gamlor.icoodb.desktop.logic;

import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.model.RSVP;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class TestRSVPValidator {
    private RSVP rsvp;
    private final RSVPValidator toTest = new RSVPValidator();


    @BeforeMethod
    public void setup() {
        this.rsvp = new Dinner().createRSVP();
    }

    @Test
    public void emptyIsInvalid() {
        assertFalse(toTest.isValid(rsvp));
    }

    @Test
    public void withTextIsValid() {
        rsvp.setName("Roman");
        assertTrue(toTest.isValid(rsvp));
    }
}
