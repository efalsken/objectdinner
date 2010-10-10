package info.gamlor.icoodb.web.controllers;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import info.gamlor.icoodb.web.business.DinnerLogic;
import info.gamlor.icoodb.web.dto.DataContainer;
import info.gamlor.icoodb.web.dto.DinnerMapper;
import info.gamlor.icoodb.web.dto.DinnerViewModel;
import info.gamlor.icoodb.web.dto.InListDinnerViewModel;
import info.gamlor.icoodb.web.model.Dinner;
import info.gamlor.icoodb.web.testutils.InMemoryDB;
import org.joda.time.DateTime;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 16.08.2010
 */
public class TestHomeController {
    private ObjectContainer db;
    private DinnerLogic dinnerLogic;
    private HomeController toTest;
    private LoggedInUserMock userLogic;


    @BeforeMethod
    public void setup() {
        this.db = InMemoryDB.newDB();
        this.dinnerLogic = new DinnerLogic(db);
        this.userLogic = userLoginLogic();
        userLogic.isLoggedIn();
        this.toTest = new HomeController(dinnerLogic, userLogic);
    }

    @Test
    public void returnsAsLoggedIn() {
        ModelAndView modelAndView = toTest.list();
        assertTrue((Boolean) modelAndView.getModel().get(HomeController.LOGGED_IN_FLAG));
        assertEquals("loginName", modelAndView.getModel().get(HomeController.IDENTITY_STRING));
    }

    @Test
    public void returnsAsNotLoggedIn() {
        userLogic.notLoggedIn();
        ModelAndView modelAndView = toTest.list();
        assertFalse((Boolean) modelAndView.getModel().get(HomeController.LOGGED_IN_FLAG));
    }

    @Test
    public void storeDinner() {
        userLogic.isLoggedIn();
        DinnerViewModel dinner = dinnerToStore();
        toTest.saveDinner(dinner, MockBindingResult.valid());

        final ObjectSet<Dinner> dinners = db.query(Dinner.class);
        assertEquals(dinners.size(), 1);
        assertNotNull(dinners.get(0).getHostedByIdentity());
    }

    @Test
    public void storeIfNotLoggedIn() {
        userLogic.notLoggedIn();
        DinnerViewModel dinner = dinnerToStore();
        toTest.saveDinner(dinner, MockBindingResult.valid());

        final ObjectSet<Dinner> dinners = db.query(Dinner.class);
        assertEquals(dinners.size(), 1);
    }

    @Test
    public void dontStoreIfInvalid() {
        userLogic.isLoggedIn();
        DinnerViewModel dinner = dinnerToStore();
        toTest.saveDinner(dinner, MockBindingResult.withErrors());

        final ObjectSet<Dinner> dinners = db.query(Dinner.class);
        assertEquals(dinners.size(), 0);
    }

    @Test
    public void listDinners() {
        db.store(newDinner());
        db.store(newDinner());
        db.store(newDinner());
        final List<InListDinnerViewModel> dinners = toTest.listDinners().getRows();

        assertEquals(dinners.size(), 3);
    }

    private Dinner newDinner() {
        Dinner dinner = new Dinner();
        dinner.setEventDate(new DateTime().plusDays(2).toDate());
        return dinner;
    }

    @Test
    public void editDinner() {
        db.store(newDinner());
        final DataContainer<DinnerViewModel> dinner = toTest.editDinner(1);

        assertEquals(dinner.getRows().size(), 1);
    }


    private DinnerViewModel dinnerToStore() {
        DinnerViewModel dinner = new DinnerViewModel();
        dinner.setTitle("Title");
        Date eventDate = new Date();
        SimpleDateFormat formatterDate = new SimpleDateFormat(DinnerMapper.DATE_FORMAT);
        SimpleDateFormat formatterTime = new SimpleDateFormat(DinnerMapper.TIME_FORMAT);
        dinner.setEventDate(formatterDate.format(eventDate));
        dinner.setEventTime(formatterTime.format(eventDate));
        dinner.setAddress("Address");
        dinner.setHostedBy("Me");
        return dinner;
    }

    private LoggedInUserMock userLoginLogic() {
        return new LoggedInUserMock();

    }

    private static class MockBindingResult extends AbstractBindingResult {
        private boolean errors;

        private MockBindingResult(boolean errors) {
            super("Mock");
            this.errors = errors;
        }

        public static MockBindingResult valid() {
            return new MockBindingResult(false);
        }

        public static MockBindingResult withErrors() {
            return new MockBindingResult(true);
        }

        @Override
        public boolean hasErrors() {
            return errors;
        }

        @Override
        public Object getTarget() {
            throw new Error("Mock");
        }

        @Override
        protected Object getActualFieldValue(String field) {
            throw new Error("Mock");
        }
    }

}
