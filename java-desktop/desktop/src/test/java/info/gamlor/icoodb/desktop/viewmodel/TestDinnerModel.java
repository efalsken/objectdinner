package info.gamlor.icoodb.desktop.viewmodel;

import com.db4o.ObjectContainer;
import info.gamlor.icoodb.desktop.logic.DinnerLogic;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.testutils.InMemoryDB;
import info.gamlor.icoodb.desktop.testutils.WasInvoked;
import info.gamlor.icoodb.desktop.ui.viewmodel.DinnerListItem;
import info.gamlor.icoodb.desktop.ui.viewmodel.DinnerListModel;
import info.gamlor.icoodb.desktop.ui.viewmodel.WorkFlowController;
import info.gamlor.icoodb.desktop.ui.views.CreateDinner;
import info.gamlor.icoodb.desktop.ui.views.RSVPPanel;
import info.gamlor.icoodb.desktop.utils.OneArgAction;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.07.2010
 */
public class TestDinnerModel {
    private DinnerLogic logic;
    private DinnerListModel toTest;
    private WorkFlowController workFlowController;
    private ObjectContainer database;

    @BeforeMethod
    public void setup() {
        database = InMemoryDB.newDB();
        this.logic = new DinnerLogic(database);
        workFlowController = mock(WorkFlowController.class);
        this.toTest = new DinnerListModel(logic, workFlowController);
    }

    @Test
    public void noEntriesList() {
        List<DinnerListItem> dinners = toTest.getDinners();
        assertEquals(dinners.size(), 0);
    }

    @Test
    public void hasZeroDefault() {
        prepareEntries(logic);
        int count = firstDinner().getCount();
        Assert.assertEquals(count, 0);
    }

    @Test
    public void hasAttendeeCount() {
        prepareEntries(logic);
        DinnerListItem dinner = firstDinner();
        dinner.getDinner().createRSVP();
        dinner.getDinner().createRSVP();
        Assert.assertEquals(dinner.getCount(), 2);
    }

    @Test
    public void listDinners() {
        prepareEntries(logic);
        List<DinnerListItem> dinners = toTest.getDinners();
        assertEquals(dinners.size(), 3);
    }

    @Test
    public void editDinner() {
        prepareEntries(logic);
        toTest.edit(firstDinner().getDinner());
        verify(workFlowController).finishAndSwitchTo(eq(CreateDinner.class), (OneArgAction<CreateDinner>) any());
    }

    @Test
    public void deleteDinner() {
        prepareEntries(logic);
        toTest.delete(firstDinner().getDinner());
        List<DinnerListItem> dinners = toTest.getDinners();
        assertEquals(dinners.size(), 2);
    }

    @Test
    public void addRSVP() {
        prepareEntries(logic);
        toTest.addRSVP(firstDinner().getDinner());
        verify(workFlowController).finishAndSwitchTo(eq(RSVPPanel.class), (OneArgAction<RSVPPanel>) any());
    }

    @Test
    public void propertyChangeListenerWasFired() {
        prepareEntries(logic);
        WasInvoked invokeCheck = new WasInvoked();
        toTest.addPropertyChangeListener(invokeCheck);
        toTest.delete(firstDinner().getDinner());
        invokeCheck.assertWasInvoked();

    }

    @Test
    public void disposingStoresAll() {
        prepareEntries(logic);
        final DinnerListItem dinner = toTest.getDinners().get(0);
        dinner.setTitle("New Title");
        toTest.dispose();


        final Dinner compareDinner = database.ext().peekPersisted(dinner.getDinner(), 1, false);
        assertEquals(compareDinner.getTitle(), dinner.getTitle());


    }

    private void prepareEntries(DinnerLogic logic) {
        logic.store(logic.createNewDinner());
        logic.store(logic.createNewDinner());
        logic.store(logic.createNewDinner());
    }

    private DinnerListItem firstDinner() {
        return toTest.getDinners().get(0);
    }


}
