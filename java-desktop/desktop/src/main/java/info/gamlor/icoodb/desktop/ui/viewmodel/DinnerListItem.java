package info.gamlor.icoodb.desktop.ui.viewmodel;

import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.model.RSVP;
import info.gamlor.icoodb.desktop.utils.ChangeEventSender;

import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * @author roman.stoffel@gamlor.info
 * @since 27.07.2010
 */
public class DinnerListItem implements ChangeEventSender {
    private final Dinner dinner;

    public DinnerListItem(Dinner dinner) {
        this.dinner = dinner;
    }

    public Dinner getDinner() {
        return dinner;
    }

    /**
     * Here to satisfy the beans-binding library. No use otherwise
     */
    public void setDinner(Dinner dinner) {

    }

    public String getTitle() {
        return dinner.getTitle();
    }

    public void setTitle(String title) {
        dinner.setTitle(title);
    }

    public Date getEventDate() {
        return dinner.getEventDate();
    }

    public String getCountry() {
        return dinner.getCountry();
    }

    public void setCountry(String country) {
        dinner.setCountry(country);
    }

    public String getAddress() {
        return flatten(getDinner().getAddress());
    }

    private String flatten(String address) {
        return address.replace("\n",", ");
    }

    public int getCount() {
        return getDinner().getAttendees().size();
    }

    public void addAttendee(RSVP rsvp) {
        dinner.addAttendee(rsvp);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        dinner.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        dinner.removePropertyChangeListener(listener);
    }
}
