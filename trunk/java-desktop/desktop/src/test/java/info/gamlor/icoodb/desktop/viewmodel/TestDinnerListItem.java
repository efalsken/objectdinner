package info.gamlor.icoodb.desktop.viewmodel;

import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.testutils.PropertyChangeEventsTestUtils;
import info.gamlor.icoodb.desktop.ui.viewmodel.DinnerListItem;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;

/**
 * @author roman.stoffel@gamlor.info
 * @since 27.07.2010
 */
public class TestDinnerListItem {


    @Test
    public void getDinner() {
        final Dinner dinner = new Dinner();
        DinnerListItem toTest = new DinnerListItem(dinner);
        assertEquals(toTest.getDinner(), dinner);
    }

    @Test
    public void getSetTitle() {
        DinnerListItem toTest = new DinnerListItem(new Dinner());
        PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents(toTest, "title", "Title");
    }

    @Test
    public void settingTitleAlsoOnUnderlyingObject() {
        DinnerListItem toTest = new DinnerListItem(new Dinner());
        toTest.setTitle("NewTitle");
        assertEquals(toTest.getDinner().getTitle(), toTest.getTitle());
    }

    @Test
    public void getEventDateFromDinner() {
        Dinner dinner = new Dinner();
        dinner.setEventDate(new Date());
        DinnerListItem toTest = new DinnerListItem(dinner);
        assertEquals(toTest.getEventDate(), dinner.getEventDate());
    }

    @Test
    public void getSetCountry() {
        DinnerListItem toTest = new DinnerListItem(new Dinner());
        PropertyChangeEventsTestUtils.assertCanSetAndGetWithEvents(toTest, "country", "Switzerland");
    }

    @Test
    public void settingCountryAlsoOnUnderlyingObject() {
        DinnerListItem toTest = new DinnerListItem(new Dinner());
        toTest.setCountry("NewTitle");
        assertEquals(toTest.getDinner().getCountry(), toTest.getCountry());
    }

    @Test
    public void getAddress() {
        Dinner dinner = new Dinner();
        dinner.setAddress("Address");
        DinnerListItem toTest = new DinnerListItem(dinner);
        assertEquals(toTest.getAddress(), dinner.getAddress());
    }
    @Test
    public void flattensAddress() {
        Dinner dinner = new Dinner();
        dinner.setAddress("Address\nCity");
        DinnerListItem toTest = new DinnerListItem(dinner);
        assertEquals(toTest.getAddress(),"Address, City");
    }

}
