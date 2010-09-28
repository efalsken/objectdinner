package info.gamlor.icoodb.desktop.database;

import com.db4o.config.EmbeddedConfiguration;
import info.gamlor.icoodb.desktop.model.Dinner;
import info.gamlor.icoodb.desktop.model.RSVP;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class DatabaseConfiguration {
    public static void configure(EmbeddedConfiguration configuration) {
        configuration.common().objectClass(Dinner.class).callConstructor(true);
        configuration.common().objectClass(RSVP.class).callConstructor(true);
        configuration.common().updateDepth(2);
    }
}
