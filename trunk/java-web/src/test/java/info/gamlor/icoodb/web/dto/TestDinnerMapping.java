package info.gamlor.icoodb.web.dto;

import info.gamlor.icoodb.web.model.Dinner;
import info.gamlor.icoodb.web.model.UserIdentity;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class TestDinnerMapping {


    @Test
    public void mapEmptyDinner() {
        Dinner dinner = new Dinner();
        DinnerViewModel viewModel = DinnerMapper.toViewModel(dinner);

        assertBasicProperties(dinner, viewModel);
    }

    @Test
    public void fillDinner() {
        Dinner dinner = new Dinner();
        fillDinner(dinner);
        DinnerViewModel viewModel = DinnerMapper.toViewModel(dinner);

        assertBasicProperties(dinner, viewModel);
    }

    @Test
    public void coverAll() {
        final Dinner dinnerWithOneGuy = new Dinner();
        dinnerWithOneGuy.createRSVP(new UserIdentity("localhost"));
        List<InListDinnerViewModel> viewModels = DinnerMapper.convertAll(Arrays.asList(dinnerWithOneGuy, dinnerWithOneGuy));
        assertEquals(viewModels.size(), 2);
        for (InListDinnerViewModel viewModel : viewModels) {
            assertBasicProperties(dinnerWithOneGuy, viewModel);
        }
    }

    @Test
    public void mapDinner() {
        DinnerViewModel data = new DinnerViewModel();
        Dinner dinner = new Dinner();
        fillDinnerDTO(data);
        DinnerMapper.fromViewModel(data, dinner);

        assertBasicProperties(dinner, data);
    }

    @Test
    public void doesNotMapBackId() {
        DinnerViewModel data = new DinnerViewModel();
        Dinner dinner = new Dinner();
        fillDinnerDTO(data);

        data.setId(42);
        DinnerMapper.fromViewModel(data, dinner);

        assertEquals(dinner.getId(), 0);
    }

    private void fillDinner(Dinner dinner) {
        dinner.setAddress("Address");
        dinner.setCountry("Country");
        dinner.setDescription("Description");
        dinner.setEventDate(new Date());
        dinner.setHostedBy("HostedBy");
        dinner.setTitle("Title");
        dinner.setContactPhone("ContactPhone");
        dinner.setLatitude(42.42);
        dinner.setLongitude(13.37);
    }

    private void fillDinnerDTO(DinnerViewModel dinner) {
        dinner.setAddress("Address");
        dinner.setCountry("Country");
        dinner.setDescription("Description");
        Date eventDate = new DateTime().plusYears(5)
                .plusMonths(1).plusDays(2).plusHours(2).plusMinutes(5).toDate();
        SimpleDateFormat formatterDate = new SimpleDateFormat(DinnerMapper.DATE_FORMAT);
        SimpleDateFormat formatterTime = new SimpleDateFormat(DinnerMapper.TIME_FORMAT);
        dinner.setEventDate(formatterDate.format(eventDate));
        dinner.setEventTime(formatterTime.format(eventDate));
        dinner.setHostedBy("HostedBy");
        dinner.setTitle("Title");
        dinner.setContactPhone("ContactPhone");
        dinner.setLatitude(42.42);
        dinner.setLongitude(13.37);
    }

    private void assertBasicProperties(Dinner dinner, DinnerViewModelBase viewModel) {
        assertEquals(viewModel.getId(), dinner.getId());
        assertEquals(viewModel.getAddress(), dinner.getAddress());
        assertEqualsDateAndTime(dinner, viewModel);
        assertEquals(viewModel.getHostedBy(), dinner.getHostedBy());
        assertEquals(viewModel.getTitle(), dinner.getTitle());
        assertEquals(viewModel.getLatitude(), dinner.getLatitude());
        assertEquals(viewModel.getLongitude(), dinner.getLongitude());
    }

    private void assertBasicProperties(Dinner dinner, InListDinnerViewModel viewModel) {
        assertBasicProperties(dinner, (DinnerViewModelBase) viewModel);
        assertEquals(dinner.getAttendees().size(), viewModel.getAttendeeCount());
    }

    private void assertBasicProperties(Dinner dinner, DinnerViewModel viewModel) {
        assertBasicProperties(dinner, (DinnerViewModelBase) viewModel);
        assertEquals(viewModel.getCountry(), dinner.getCountry());
        assertEquals(viewModel.getDescription(), dinner.getDescription());
        assertEquals(viewModel.getContactPhone(), dinner.getContactPhone());
    }

    private void assertEqualsDateAndTime(Dinner dinner, DinnerViewModelBase viewModel) {
        String dateAsString = viewModel.getEventDate();
        String timeAsString = viewModel.getEventTime();
        DateTime date = new DateTime(dinner.getEventDate());

        assertTrue(dateAsString.contains(String.valueOf(date.getYear())));
        assertTrue(dateAsString.contains(String.valueOf(date.getDayOfMonth())));
        assertTrue(dateAsString.contains(String.valueOf(date.getMonthOfYear())));
        assertTrue(timeAsString.contains(String.valueOf(date.getHourOfDay())));
        assertTrue(timeAsString.contains(String.valueOf(date.getMinuteOfHour())));

    }

}
