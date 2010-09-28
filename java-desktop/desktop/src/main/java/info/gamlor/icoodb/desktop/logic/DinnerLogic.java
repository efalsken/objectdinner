package info.gamlor.icoodb.desktop.logic;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import info.gamlor.icoodb.desktop.database.Transactional;
import info.gamlor.icoodb.desktop.model.Dinner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 15.07.2010
 */
@Singleton
@Transactional
public class DinnerLogic {
    private ObjectContainer container;

    @Inject
    public DinnerLogic(ObjectContainer container) {
        this.container = container;
    }

    public Dinner createNewDinner() {
        return new Dinner();
    }

    public void store(Dinner dinner) {
        container.store(dinner);
    }

    public List<Dinner> listDinners() {
        final long currentTime = System.currentTimeMillis();
        final ObjectSet<Dinner> dinners = container.query(new Predicate<Dinner>() {
            @Override
            public boolean match(Dinner dinner) {
                return dinner.getEventDate().getTime() >= currentTime;
            }
        });

        return new ArrayList<Dinner>(dinners);
    }

    public void delete(Dinner toDelete) {
        container.delete(toDelete);
    }
}
