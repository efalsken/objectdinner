package info.gamlor.icoodb.desktop.testutils;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.io.MemoryStorage;
import info.gamlor.icoodb.desktop.database.DatabaseConfiguration;

/**
 * @author roman.stoffel@gamlor.info
 * @since 14.07.2010
 */
public class InMemoryDB {
    final MemoryStorage storage = new MemoryStorage();

    public ObjectContainer dbInstance() {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        DatabaseConfiguration.configure(configuration);
        configuration.file().storage(storage);
        return Db4oEmbedded.openFile(configuration, "!No:File");
    }

    public static ObjectContainer newDB() {
        return new InMemoryDB().dbInstance();
    }
}
