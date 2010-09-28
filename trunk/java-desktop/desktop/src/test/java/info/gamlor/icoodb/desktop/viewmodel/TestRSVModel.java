package info.gamlor.icoodb.desktop.viewmodel;

import com.db4o.ObjectContainer;
import info.gamlor.icoodb.desktop.logic.RSVPLogic;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.model.RSVP;
import info.gamlor.icoodb.desktop.testutils.InMemoryDB;
import info.gamlor.icoodb.desktop.testutils.WasInvoked;
import info.gamlor.icoodb.desktop.ui.viewmodel.RSVPModel;
import info.gamlor.icoodb.desktop.ui.viewmodel.WorkFlowController;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class TestRSVModel {
    private Dinner dinner;
    private RSVPModel toTest;
    private WorkFlowController workflow;
    private RSVPLogic logic;
    private ObjectContainer db;

    @BeforeMethod
    public void setup() {
        dinner = new Dinner();
        db = InMemoryDB.newDB();
        logic = new RSVPLogic(db);
        workflow = mock(WorkFlowController.class);
        this.toTest = new RSVPModel(logic, workflow);
        toTest.createForDinner(dinner);
    }

    @Test
    public void titleFromDinner() {
        dinner.setTitle("Title");
        assertEquals(toTest.getTitle(), dinner.getTitle());
    }

    @Test
    public void addressFromDinner() {
        dinner.setAddress("Address");
        assertEquals(toTest.getAddress(), dinner.getAddress());
    }

    @Test
    public void descriptionFromDinner() {
        dinner.setDescription("Description");
        assertEquals(toTest.getDescription(), dinner.getDescription());
    }

    @Test
    public void hostFromDinner() {
        dinner.setHostedBy("Host");
        assertEquals(toTest.getHostedBy(), dinner.getHostedBy());
    }


    @Test
    public void createRSVP() {
        toTest.createForDinner(new Dinner());
        assertEquals(toTest.getAttendees().size(), 1);
    }

    @Test
    public void currentRSVPsSet() {
        toTest.createForDinner(new Dinner());
        assertNotNull(toTest.getCurrentRSVP());
    }

    @Test
    public void storesRSVP() {
        toTest.createForDinner(new Dinner());
        toTest.executeDone();

        final int rsvpCount = db.query(RSVP.class).size();
        assertEquals(rsvpCount, 1);
        verify(workflow).finished();
    }

    @Test
    public void afterStoringNoRollback() {
        final Dinner dinner = new Dinner();
        toTest.createForDinner(dinner);
        toTest.executeDone();

        toTest.dispose();
        assertEquals(dinner.getAttendees().size(), 1);
    }


    @Test
    public void firesEvent() {
        WasInvoked eventFireCheck = new WasInvoked();
        toTest.addPropertyChangeListener(eventFireCheck);
        toTest.createForDinner(new Dinner());
        eventFireCheck.assertWasInvoked(6);
    }

    @Test
    public void removesRVPOnDispose() {
        toTest.dispose();
        assertEquals(dinner.getAttendees().size(), 0);

    }

    @Test
    public void settingDinnerFiresEvents() {
        WasInvoked eventFireCheck = new WasInvoked();
        toTest.addPropertyChangeListener(eventFireCheck);
        toTest.createForDinner(new Dinner());
        eventFireCheck.assertWasInvoked(6);
    }


    @Test
    public void isInvalid() {
        assertFalse(toTest.isReadyToFinish());
    }

    @Test
    public void validateWithEntry() {
        toTest.getCurrentRSVP().setName("Roman");
        assertTrue(toTest.isReadyToFinish());
    }

    @Test
    public void firesEventOnValidationChange() {
        WasInvoked event = new WasInvoked();
        toTest.addPropertyChangeListener(event);
        toTest.getCurrentRSVP().setName("Roman");
        event.assertWasInvoked();
        assertTrue(toTest.isReadyToFinish());
    }
}
