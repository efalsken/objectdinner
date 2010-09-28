package info.gamlor.icoodb.desktop.database;

import com.db4o.ObjectContainer;
import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author roman.stoffel@gamlor.info
 * @since 21.07.2010
 */
class TransactionHandler implements MethodInterceptor {
    @Inject
    private ObjectContainer toHandle;

    public TransactionHandler(ObjectContainer toHandle) {
        this.toHandle = toHandle;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object result = invocation.proceed();
            toHandle.commit();
            return result;
        } catch (Throwable throwable) {
            toHandle.rollback();
            throw throwable;
        }
    }
}
