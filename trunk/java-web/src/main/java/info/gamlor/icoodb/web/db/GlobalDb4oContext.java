package info.gamlor.icoodb.web.db;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.ExtObjectContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
@Component("db4o-root-container")
@Scope(value = "singleton")
public class GlobalDb4oContext implements DisposableBean, ServletContextAware {
    private static final String DEFAULT_PATH = "default-database.db4o";
    private String databaseFile = DEFAULT_PATH;


    private ObjectContainer container;

    public synchronized ObjectContainer newContainer() {
        final ExtObjectContainer objectContainer = rootContainer().ext();
        if (objectContainer.isClosed()) {
            throw new DatabaseClosedException();
        }
        return objectContainer.openSession();
    }

    private ObjectContainer rootContainer() {
        if (null == container) {
            this.container = createContainer();
            AutoIncrementSupport.install(container);
        }
        return container;
    }


    private ObjectContainer createContainer() {
        return Db4oEmbedded.openFile(DbConfiguration.newConfig(), getDatabaseFile());
    }

    public void setDatabaseFile(String databaseFile) {
        this.databaseFile = databaseFile;
    }

    public String getDatabaseFile() {
        if (null == databaseFile) {
            return DEFAULT_PATH;
        }
        return databaseFile;
    }

    public void close() {
        rootContainer().close();
    }

    @Override
    public void destroy() {
        this.rootContainer().close();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.databaseFile = servletContext.getRealPath(databaseFile);
    }
}
