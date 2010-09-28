package info.gamlor.icoodb.desktop.logic;

import info.gamlor.icoodb.desktop.testutils.Modifiable;
import info.gamlor.icoodb.desktop.utils.ChangeEventSender;
import info.gamlor.icoodb.desktop.utils.OneArgAction;
import info.gamlor.icoodb.desktop.utils.PropertyEventsSupport;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 26.07.2010
 */
public class TestBoundValidator {
    private MyBean bean;
    private BoundValidator<MyBean> boundValidator;

    @BeforeMethod
    public void setup() {
        this.bean = new MyBean(false);
        this.boundValidator = BoundValidator.create(bean, new MyBeanValidator());
    }

    @Test
    public void hasValidationStatus() {
        assertFalse(BoundValidator.create(new MyBean(false), new MyBeanValidator()).isValid());
        assertTrue(BoundValidator.create(new MyBean(true), new MyBeanValidator()).isValid());
    }

    @Test
    public void notifiesOnValid() {
        final Modifiable<Boolean> genericCalled = Modifiable.create(false);
        this.boundValidator.onChange(new OneArgAction<Boolean>() {
            @Override
            public void invoke(Boolean value) {
                genericCalled.setValue(value);
            }
        });
        bean.setValid(true);
        assertTrue(genericCalled.getValue());
    }

    @Test
    public void notifiesOnInvalid() {
        final Modifiable<Boolean> genericCalled = Modifiable.create(false);
        this.boundValidator.onChange(new OneArgAction<Boolean>() {
            @Override
            public void invoke(Boolean value) {
                genericCalled.setValue(value);
            }
        });
        bean.setValid(true);
        bean.setValid(false);
        assertFalse(genericCalled.getValue());
    }

    @Test
    public void removesRegistrationOnDispose() {
        boundValidator.dispose();
        bean.assertNoListenersLeft();
    }


    public static class MyBean implements ChangeEventSender {
        private final PropertyEventsSupport changeSupport = new PropertyEventsSupport(this);

        private boolean valid;
        private final Set<PropertyChangeListener> listenerSet = new HashSet<PropertyChangeListener>();

        public MyBean(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
            changeSupport.firePropertyChange("valid", valid);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            listenerSet.add(listener);
            changeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            listenerSet.remove(listener);
            changeSupport.removePropertyChangeListener(listener);
        }

        public void assertNoListenersLeft() {
            assertEquals(listenerSet.size(), 0);
        }
    }

    public static class MyBeanValidator implements BusinessValidator<MyBean> {
        @Override
        public boolean isValid(MyBean value) {
            return value.isValid();
        }
    }
}
