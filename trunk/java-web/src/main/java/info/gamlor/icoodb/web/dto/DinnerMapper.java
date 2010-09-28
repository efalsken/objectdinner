package info.gamlor.icoodb.web.dto;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.Converter;
import info.gamlor.icoodb.web.model.Dinner;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static info.gamlor.icoodb.web.utils.ExceptionUtils.reThrow;


/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class DinnerMapper {
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String TIME_FORMAT = "HH:mm";

    private DinnerMapper(){
        
    }

    public static DinnerViewModel toViewModel(Dinner dinner) {
        DinnerViewModel viewModel = new DinnerViewModel();
        BeanUtils.copyProperties(dinner, viewModel, new String[]{"eventDate"});
        Date eventDate = dinner.getEventDate();
        SimpleDateFormat formatterDate = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat formatterTime = new SimpleDateFormat(TIME_FORMAT);
        viewModel.setEventDate(formatterDate.format(eventDate));
        viewModel.setEventTime(formatterTime.format(eventDate));
        return viewModel;
    }


    public static Dinner fromViewModel(DinnerViewModel data, Dinner dinner) {
        // Because the source is a new instance of a view model, we need to copy all the properties.
        BeanUtils.copyProperties(data, dinner, new String[]{"eventDate"});
        // Type converter from input string to a date.
        dinner.setEventDate(parseDate(data.getEventDate(), data.getEventTime()));
        return dinner;
    }

    public static List<InListDinnerViewModel> convertAll(List<Dinner> dinners) {
        return Lambda.convert(dinners, new Converter<Dinner, InListDinnerViewModel>() {
            @Override
            public InListDinnerViewModel convert(Dinner dinner) {
                return convertSingleListItem(dinner);
            }
        });
    }

    private static InListDinnerViewModel convertSingleListItem(Dinner dinner) {
        InListDinnerViewModel viewModel = new InListDinnerViewModel();
        BeanUtils.copyProperties(dinner, viewModel, new String[]{"eventDate"});
        Date eventDate = dinner.getEventDate();
        SimpleDateFormat formatterDate = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat formatterTime = new SimpleDateFormat(TIME_FORMAT);
        viewModel.setEventDate(formatterDate.format(eventDate));
        viewModel.setEventTime(formatterTime.format(eventDate));
        viewModel.setAttendeeCount(dinner.getAttendees().size());
        return viewModel;
    }

    private static Date parseDate(String date, String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);
        try {
            return formatter.parse(date + " " + time);
        } catch (ParseException e) {
            return reThrow(e);
        }
    }
}
