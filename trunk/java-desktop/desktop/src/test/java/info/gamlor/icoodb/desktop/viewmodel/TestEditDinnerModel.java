package info.gamlor.icoodb.desktop.viewmodel;

import info.gamlor.icoodb.desktop.logic.DinnerLogic;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.testutils.InMemoryDB;
import info.gamlor.icoodb.desktop.testutils.PropertyChangeEventsTestUtils;
import info.gamlor.icoodb.desktop.testutils.WasInvoked;
import info.gamlor.icoodb.desktop.ui.viewmodel.EditDinnerModel;
import info.gamlor.icoodb.desktop.ui.viewmodel.WorkFlowController;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
public class TestEditDinnerModel {
    private EditDinnerModel toTest;
    private DinnerLogic logic;
    private WorkFlowController workflowMock;

    @BeforeMethod
    public void setup() {
        this.workflowMock = mock(WorkFlowController.class);
        this.logic = new DinnerLogic(InMemoryDB.newDB());
        this.toTest = new EditDinnerModel(logic, workflowMock);
    }

    @Test
    public void hasNewItem() {
        assertNotNull(toTest.getDinner());
    }


    @Test
    public void getAndSetDinner() {
        PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents(toTest, "dinner", new Dinner());
    }

    @Test
    public void storesOnDone() {
        final Dinner dinner = toTest.getDinner();
        dinner.setTitle("MyDinner");
        toTest.executeDone();

        final List<Dinner> dinners = logic.listDinners();
        assertEquals(dinners.size(), 1);
    }

    @Test
    public void closesWorkFlowOnDone() {
        final Dinner dinner = toTest.getDinner();
        dinner.setTitle("MyDinner");
        toTest.executeDone();

        verify(workflowMock).finished();
    }

    @Test
    public void isInvalid() {
        final Dinner dinner = toTest.getDinner();
        assertFalse(toTest.isReadyToFinish());
    }

    @Test
    public void validDinner() {
        final Dinner dinner = toTest.getDinner();
        fillDinner(dinner);
        assertTrue(toTest.isReadyToFinish());
    }

    @Test
    public void validPropertyChangeEvent() {
        final Dinner dinner = toTest.getDinner();
        final WasInvoked invoked = new WasInvoked("readyToFinish");
        toTest.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                invoked.invoke(evt.getPropertyName());
            }
        });
        fillDinner(dinner);
        invoked.assertWasInvoked();
    }

    private void fillDinner(Dinner dinner) {
        dinner.setTitle("Title");
        dinner.setHostedBy("Host");
        dinner.setAddress("Address");
    }


}
