package info.gamlor.icoodb.desktop.database;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import java.io.File;

/**
 * @author roman.stoffel@gamlor.info
 * @since 14.07.2010
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        configureTransactionHandling();
    }

    private void configureTransactionHandling() {
        TransactionHandler txHandler = new TransactionHandler(null);
        requestInjection(txHandler);
        bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(Transactional.class), txHandler);
        bindInterceptor(Matchers.annotatedWith(Transactional.class),
                Matchers.any(), txHandler);
    }

    @Provides
    @Singleton
    ObjectContainer buildContainer() {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        DatabaseConfiguration.configure(configuration);
        return Db4oEmbedded.openFile(configuration, buildPath());
    }

    private String buildPath() {
        final File path = new File(System.getProperty("user.home"));
        return new File(path,"nerddinner.db4o").getAbsolutePath();
    }

}
