package info.gamlor.icoodb.desktop.database;


import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author roman.stoffel@gamlor.info
 * @since 14.07.2010
 */
public class TestDb4oObjectStorage {
    private ObjectContainer containerMock;
    private Db4oObjectStorage toTest;

    @BeforeMethod
    public void setup(){
        this.containerMock = mock(ObjectContainer.class);
        this.toTest = new Db4oObjectStorage(containerMock);
    }

    @Test
    public void delegatesStore(){
        Object obj = new Object();
        toTest.store(obj);

        verify(containerMock).store(obj);
    }

    @Test
    public void delegatesAllQuery(){
        toTest.query(Object.class);

        verify(containerMock).query(Object.class);

    }
    @Test
    public void delegatesConcreteQuery(){
        final Predicate<Object> matchEverthing = new Predicate<Object>() {
            @Override
            public boolean match(Object o) {
                return true;
            }
        };
        toTest.query(matchEverthing);

        verify(containerMock).query(matchEverthing);

    }
}
