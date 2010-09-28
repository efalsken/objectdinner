package info.gamlor.icoodb.web.db;

import com.db4o.ObjectContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * @author roman.stoffel@gamlor.info
 * @since 16.08.2010
 */
public class Db4oTxManager extends AbstractPlatformTransactionManager {
    @Autowired
    private ApplicationContext context;
    private final static Logger log = LoggerFactory.getLogger(Db4oTxManager.class);

    public Db4oTxManager() {
    }

    public Db4oTxManager(ApplicationContext ctx) {
        this.context = ctx;
    }

    @Override
    protected Object doGetTransaction() throws TransactionException {
        ObjectContainer container = (ObjectContainer)context.getBean(ObjectContainer.class.getName());
        return new TransactionId(container);
    }

    @Override
    protected void doBegin(Object o, TransactionDefinition transactionDefinition) throws TransactionException {
        log.info("Start transaction: "+o);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
        final TransactionId transaction = (TransactionId) defaultTransactionStatus.getTransaction();
        log.info("Committing transaction: "+ transaction);
        transaction.commit();
    }

    @Override
    protected void doRollback(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
        final TransactionId transaction = (TransactionId) defaultTransactionStatus.getTransaction();
        log.info("Rollback transaction: "+ transaction);
        transaction.rollback();
    }


    static class TransactionId{
        private final ObjectContainer container;

        TransactionId(ObjectContainer container) {
            this.container = container;
        }

        public void commit() {
            container.commit();
        }

        public void rollback() {
            container.rollback();
        }

        @Override
        public String toString() {
            return "Transaction"
                    + container
                    + '}';
        }
    }
}
