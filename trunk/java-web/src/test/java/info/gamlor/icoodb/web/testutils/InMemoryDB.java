package info.gamlor.icoodb.web.testutils;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.io.MemoryStorage;
import info.gamlor.icoodb.web.db.AutoIncrementSupport;
import info.gamlor.icoodb.web.db.DbConfiguration;


/**
 * @author roman.stoffel@gamlor.info
 * @since 14.07.2010
 */
public class InMemoryDB {
    final MemoryStorage storage = new MemoryStorage();

    public ObjectContainer dbInstance() {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.file().storage(storage);
        DbConfiguration.configure(configuration);
        final EmbeddedObjectContainer instance = Db4oEmbedded.openFile(configuration, "!No:File");
        AutoIncrementSupport.install(instance);
        return instance;
    }

    public static ObjectContainer newDB() {
        return new InMemoryDB().dbInstance();
    }
}
