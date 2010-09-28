package info.gamlor.icoodb.desktop.logic;

import com.db4o.ObjectContainer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import info.gamlor.icoodb.desktop.database.Transactional;
import info.gamlor.icoodb.desktop.model.RSVP;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
@Singleton
@Transactional
public class RSVPLogic {
    private ObjectContainer container;

    @Inject
    public RSVPLogic(ObjectContainer container) {
        this.container = container;
    }

    public void delete(RSVP toDelete) {
        container.delete(toDelete);
        toDelete.getDinner().removeRSVP(toDelete);
        container.store(toDelete.getDinner());
    }

    public void store(RSVP toDelete) {
        container.store(toDelete);
        container.store(toDelete.getDinner());
    }
}
