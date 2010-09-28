package info.gamlor.icoodb.desktop.ui.viewmodel;

import info.gamlor.icoodb.desktop.utils.ChangeEventSender;
import info.gamlor.icoodb.desktop.utils.Disposable;
import info.gamlor.icoodb.desktop.utils.Disposer;
import info.gamlor.icoodb.desktop.utils.PropertyEventsSupport;

import java.beans.PropertyChangeListener;

/**
 * @author roman.stoffel@gamlor.info
 * @since 20.07.2010
 */
public abstract class AbstractViewModel implements ChangeEventSender, Disposable {
    private final PropertyEventsSupport changeSupport = new PropertyEventsSupport(this);

    private Disposer disposer = new Disposer();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object newValue) {
        changeSupport.firePropertyChange(propertyName, newValue);
    }

    protected Disposer getDisposer() {
        return disposer;
    }

    @Override
    public final void dispose() {
        this.disposer.dispose();
    }


}
