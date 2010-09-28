package info.gamlor.icoodb.web.business;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import info.gamlor.icoodb.web.model.Dinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 * @since 16.08.2010
 */
@Component("dinner-logic")
@Scope(value = "request")
@Transactional()
public class DinnerLogic {
    @Autowired
    private ObjectContainer container;

    public DinnerLogic() {
    }

    public DinnerLogic(ObjectContainer container) {
        this.container = container;
    }

    public Dinner createNewDinner() {
        return new Dinner();
    }


    public Dinner byIdOrNew(final int id) {
        Dinner dinner = byIdOrNull(id);
        if (null == dinner) {
            return createNewDinner();
        }
        return dinner;
    }

    public Dinner byIdOrNull(final int id) {
        final ObjectSet<Dinner> result = container.query(new Predicate<Dinner>() {
            @Override
            public boolean match(Dinner dinner) {
                return dinner.getId() == id;
            }
        });
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public void store(Dinner dinner) {
        container.store(dinner);
    }


    public List<Dinner> listDinners() {
        final Date now = new Date();
        return container.query(new Predicate<Dinner>() {
            @Override
            public boolean match(Dinner dinner) {
                return dinner.getEventDate().compareTo(now) >= 0;
            }
        });
    }
}
