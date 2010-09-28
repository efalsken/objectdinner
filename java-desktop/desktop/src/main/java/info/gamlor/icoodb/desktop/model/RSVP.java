package info.gamlor.icoodb.desktop.model;

import info.gamlor.icoodb.desktop.utils.ChangeEventSender;
import info.gamlor.icoodb.desktop.utils.PropertyEventsSupport;

import java.beans.PropertyChangeListener;

/**
 * @author roman.stoffel@gamlor.info
 * @since 27.07.2010
 */
public class RSVP implements ChangeEventSender {
    private final Dinner dinner;
    private String name = "";

    private final transient PropertyEventsSupport eventHandling = new PropertyEventsSupport(this);

    public RSVP(Dinner dinner) {
        this.dinner = dinner;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        eventHandling.firePropertyChange("name", name);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        eventHandling.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        eventHandling.removePropertyChangeListener(listener);
    }
}
