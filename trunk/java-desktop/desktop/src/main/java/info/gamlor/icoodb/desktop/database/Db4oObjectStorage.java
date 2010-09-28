package info.gamlor.icoodb.desktop.database;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Predicate;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.07.2010
 */
class Db4oObjectStorage implements ObjectStorage{
    private final ObjectContainer container;

    Db4oObjectStorage(ObjectContainer container) {
        this.container = container;
    }

    public void store(Object o) throws DatabaseClosedException, DatabaseReadOnlyException {
        container.store(o);
    }

    public <TargetType> ObjectSet<TargetType> query(Class<TargetType> targetTypeClass) throws Db4oIOException, DatabaseClosedException {
        return container.query(targetTypeClass);
    }

    public <TargetType> ObjectSet<TargetType> query(Predicate<TargetType> targetTypePredicate) throws Db4oIOException, DatabaseClosedException {
        return container.query(targetTypePredicate);
    }
}
