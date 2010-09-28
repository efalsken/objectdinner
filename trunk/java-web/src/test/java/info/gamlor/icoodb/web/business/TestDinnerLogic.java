package info.gamlor.icoodb.web.business;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import info.gamlor.icoodb.web.model.Dinner;
import info.gamlor.icoodb.web.testutils.InMemoryDB;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

/**
 * @author roman.stoffel@gamlor.info
 * @since 16.08.2010
 */
public class TestDinnerLogic {
    private DinnerLogic toTest;
    private ObjectContainer db;

    @BeforeMethod
    public void setup() {
        this.db = InMemoryDB.newDB();
        this.toTest = new DinnerLogic(db);
    }

    @Test
    public void createsNewDinner() {
        final Dinner dinner = toTest.createNewDinner();
        assertNotNull(dinner);
    }

    @Test
    public void getById() {
        Dinner d1 = storeDinners();

        final Dinner dinner = toTest.byIdOrNew(d1.getId());
        assertNotNull(dinner);
        assertEquals(dinner, d1);
    }

    @Test
    public void returnsNullForUnknownId() {
        Dinner d1 = storeDinners();

        final Dinner dinner = toTest.byIdOrNull(42);
        assertNull(dinner);
    }

    @Test
    public void newDinnerForInvalidId() {
        storeDinners();

        final Dinner dinner = toTest.byIdOrNew(1);
        assertNotNull(dinner);
    }

    @Test
    public void storeDinner() {
        final Dinner dinner = toTest.createNewDinner();
        toTest.store(dinner);

        final ObjectSet<Dinner> dinners = db.query(Dinner.class);
        assertEquals(dinners.size(), 1);
    }

    @Test
    public void listOnlyCurrentDinners() {
        storeDinnerWithOffset(1);
        storeDinnerWithOffset(2);
        storeDinnerWithOffset(-1);

        int count = toTest.listDinners().size();
        assertEquals(count, 2);
    }

    private void storeDinnerWithOffset(int dayOffset) {
        final Dinner dinner = toTest.createNewDinner();
        dinner.setEventDate(new DateTime().plusDays(dayOffset).toDate());
        toTest.store(dinner);
    }


    private Dinner storeDinners() {
        Dinner d1 = new Dinner();
        Dinner d2 = new Dinner();
        db.store(d1);
        db.store(d2);
        return d1;
    }
}
