package info.gamlor.icoodb.web.db;

import com.db4o.ObjectContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;

/**
 * @author roman.stoffel@gamlor.info
 * @since 16.08.2010
 */
public class TestTransactionManager {
    private Db4oTxManager toTest;
    private ObjectContainer transaction;

    @BeforeMethod
    public void setup(){
        this.transaction = mock(ObjectContainer.class);
        ApplicationContext appContext = mock(ApplicationContext.class);
        when(appContext.getBean(ObjectContainer.class.getName())).thenReturn(transaction);
        this.toTest = new Db4oTxManager(appContext);
    }

    @Test
    public void beginTxAndCommit(){
        toTest.doBegin(null,null);
        toTest.doCommit(createTxStatus());
        verify(transaction).commit();

    }

    @Test
    public void rollBackTransaction(){
        toTest.doBegin(null,null);
        toTest.doRollback(createTxStatus());
        verify(transaction).rollback();

    }
    @Test
    public void getTransaction(){
        Object tx = toTest.getTransaction(null);
        assertNotNull(tx);

    }

    private DefaultTransactionStatus createTxStatus() {
        return new DefaultTransactionStatus(new Db4oTxManager.TransactionId(transaction),true,true,false,false,null);
    }

}
