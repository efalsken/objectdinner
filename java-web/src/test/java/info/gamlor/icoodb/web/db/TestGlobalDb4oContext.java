package info.gamlor.icoodb.web.db;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import info.gamlor.icoodb.web.testutils.TemporaryFiles;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.AssertJUnit.assertEquals;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.08.2010
 */
public class TestGlobalDb4oContext {
    public static final String DATABASE_FILE = "database.db4o";

    @Test
    public void canCreateContainer() {
        GlobalDb4oContext toTest = setupTestObject();
        ObjectContainer container = toTest.newContainer();
        assertNotNull(container);

    }

    @Test
    public void singleTonContainer() {
        GlobalDb4oContext toTest = setupTestObject();
        ObjectContainer container = toTest.newContainer();
        assertNotSame(toTest.newContainer(), container);
    }

    @Test
    public void createsDefaultContainer() {
        GlobalDb4oContext toTest = createContext();
        ObjectContainer container = toTest.newContainer();
        assertNotNull(container);
        container.close();
        toTest.close();
    }

    @Test
    public void registersAutoIncrement() {
        GlobalDb4oContext toTest = createContext();
        ObjectContainer container = toTest.newContainer();
        WithAutoIDs object = new WithAutoIDs();
        container.store(object);
        assertEquals(object.getGeneratedIds(), 1);
        toTest.close();
    }

    @Test(expectedExceptions = DatabaseClosedException.class)
    public void destroysDB() {
        GlobalDb4oContext toTest = createContext();
        toTest.destroy();
        toTest.newContainer();
    }

    @AfterTest
    public void cleanUp() {
        new File(DATABASE_FILE).delete();
    }

    private GlobalDb4oContext createContext() {
        GlobalDb4oContext ctx = new GlobalDb4oContext();
        ctx.setDatabaseFile(DATABASE_FILE);
        return ctx;
    }


    private GlobalDb4oContext setupTestObject() {
        String fileName = TemporaryFiles.tempDirName();
        GlobalDb4oContext toTest = createContext();
        toTest.setDatabaseFile(fileName);
        return toTest;
    }


}
