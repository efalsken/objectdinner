package info.gamlor.icoodb.desktop.database;

import com.db4o.ObjectContainer;
import org.aopalliance.intercept.MethodInvocation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.07.2010
 */
public class TestTransactionHandler {
    private TransactionHandler toTest;
    private ObjectContainer mockContainer;
    private MethodInvocation invocation;

    @BeforeMethod
    public void setup(){
        this.mockContainer = mock(ObjectContainer.class);
        this.toTest = new TransactionHandler(mockContainer);
        this.invocation = mock(MethodInvocation.class);
    }

    @Test
    public void runsMethod() throws Throwable {
        toTest.invoke(invocation);

        verify(invocation).proceed();
    }
    @Test
    public void returnsResult() throws Throwable {
        Object obj = new Object();
        when(invocation.proceed()).thenReturn(obj);
        Object result = toTest.invoke(invocation);

        Assert.assertSame(obj,result);
    }
    @Test
    public void commitsAfterOperation() throws Throwable {
        toTest.invoke(invocation);
        verify(mockContainer).commit();
    }
    @Test
    public void letsExceptionBubbleUp() throws Throwable {
        final Exception myException = new Exception();
        when(invocation.proceed()).thenThrow(myException);


        try {
            toTest.invoke(invocation);
        } catch (Throwable throwable) {
            Assert.assertSame(myException,throwable);
            return;
        }
        Assert.fail();
    }
    @Test
    public void rollsbackAfterException() throws Throwable {
        final Exception myException = new Exception();
        when(invocation.proceed()).thenThrow(myException);

        try {
            toTest.invoke(invocation);
        } catch (Throwable throwable) {
        }
        verify(mockContainer).rollback();
    }


}
