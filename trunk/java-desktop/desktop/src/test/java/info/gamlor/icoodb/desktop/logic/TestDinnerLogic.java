package info.gamlor.icoodb.desktop.logic;

import com.db4o.ObjectContainer;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.testutils.InMemoryDB;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 15.07.2010
 */
public class TestDinnerLogic {
    private DinnerLogic toTest;
    private ObjectContainer db;

    @BeforeMethod
    public void setup() {
        db = InMemoryDB.newDB();
        this.toTest = new DinnerLogic(db);
    }

    @Test
    public void createNewDinner() {
        Dinner newDinner = toTest.createNewDinner();
        assertNotNull(newDinner);

    }

    @Test
    public void storesInDatabase() {
        Dinner newDinner = toTest.createNewDinner();
        toTest.store(newDinner);
        assertEquals(db.query(Dinner.class).size(), 1);
    }

    @Test
    public void listOneDinner() {
        final Dinner dinner = newDinnerInTheFuture();
        db.store(dinner);
        final List<Dinner> list = toTest.listDinners();
        assertEquals(list.size(), 1);
        assertTrue(list.contains(dinner));
    }

    @Test
    public void listOnlyCurrentDinners() {
        db.store(newDinnerInTheFuture());
        db.store(newDinnerInTheFuture());
        final Dinner dinner = newDinnerInTheFuture();
        dinner.setEventDate(new DateTime(dinner.getEventDate()).minusDays(2).toDate());
        db.store(dinner);

        final List<Dinner> list = toTest.listDinners();
        assertEquals(list.size(), 2);
    }

    private Dinner newDinnerInTheFuture() {
        Dinner dinner = new Dinner();
        dinner.setEventDate(new DateTime().plusDays(2).toDate());
        return dinner;
    }

    @Test
    public void deleteDinner() {
        db.store(newDinnerInTheFuture());
        db.store(newDinnerInTheFuture());
        Dinner toDelete = toTest.listDinners().get(0);
        toTest.delete(toDelete);
        assertEquals(db.query(Dinner.class).size(), 1);
    }
}
