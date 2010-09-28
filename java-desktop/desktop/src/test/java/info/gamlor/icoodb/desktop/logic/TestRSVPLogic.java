package info.gamlor.icoodb.desktop.logic;

import com.db4o.ObjectContainer;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.model.RSVP;
import info.gamlor.icoodb.desktop.testutils.InMemoryDB;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class TestRSVPLogic {
    private ObjectContainer db;
    private RSVPLogic toTest;

    @BeforeMethod
    public void setup() {
        this.db = InMemoryDB.newDB();
        this.toTest = new RSVPLogic(db);
    }

    @Test
    public void delete() {
        final Dinner dinner = new Dinner();
        db.store(dinner.createRSVP());
        final RSVP rsvp = dinner.createRSVP();
        db.store(rsvp);

        toTest.delete(rsvp);

        final int countRVPs = db.query(RSVP.class).size();
        assertEquals(countRVPs, 1);
        assertEquals(dinner.getAttendees().size(), 1);
    }

    @Test
    public void store() {
        final Dinner dinner = new Dinner();
        toTest.store(dinner.createRSVP());


        final int countRVPs = db.query(RSVP.class).size();
        assertEquals(countRVPs, 1);
    }


}
