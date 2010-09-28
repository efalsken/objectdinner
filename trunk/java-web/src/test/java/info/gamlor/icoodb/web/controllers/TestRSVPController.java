package info.gamlor.icoodb.web.controllers;

import com.db4o.ObjectContainer;
import info.gamlor.icoodb.web.business.DinnerLogic;
import info.gamlor.icoodb.web.model.Dinner;
import info.gamlor.icoodb.web.model.RSVP;
import info.gamlor.icoodb.web.testutils.InMemoryDB;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author roman.stoffel@gamlor.info
 * @since 25.08.2010
 */
public class TestRSVPController {
    private DinnerLogic dinnerLogic;
    private ObjectContainer db;
    private LoggedInUserMock userLogic;
    private RSVPController toTest;
    private static final String RSVP_NAME = "RSVP-name";

    @BeforeMethod
    public void setup(){
        this.db = InMemoryDB.newDB();
        this.dinnerLogic = new DinnerLogic(db);
        this.userLogic = new LoggedInUserMock();
        userLogic.notLoggedIn();
        this.toTest = new RSVPController(dinnerLogic, userLogic);
    }

    @Test
    public void dontCreateRSVPForInvalidId(){
        toTest.storeRSVP(42, RSVP_NAME);
        assertNoRSVP();
    }
    @Test
    public void dontCreateRSVPIfNotLoggedIn(){
        int id = createDinner();
        toTest.storeRSVP(id, RSVP_NAME);
        assertNoRSVP();
    }
    @Test
    public void createRSVP(){
        userLogic.isLoggedIn();
        int id = createDinner();
        toTest.storeRSVP(id, RSVP_NAME);
        assertHasRSVP();
    }

    private int createDinner() {
        final Dinner dinner = dinnerLogic.createNewDinner();
        dinnerLogic.store(dinner);
        return dinner.getId();
    }

    private void assertNoRSVP() {
        final int count = db.query(RSVP.class).size();
        assertEquals(count,0);
    }
    private void assertHasRSVP() {
        final int count = db.query(RSVP.class).size();
        assertEquals(count,1);
    }
}
