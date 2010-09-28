package info.gamlor.icoodb.desktop.testutils;

import info.gamlor.icoodb.desktop.utils.ChangeEventSender;
import info.gamlor.icoodb.desktop.utils.NoArgFunc;
import org.testng.Assert;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import static info.gamlor.icoodb.desktop.utils.ExceptionUtils.reThrow;
import static org.testng.Assert.assertEquals;

/**
 * @author roman.stoffel@gamlor.info
 * @since 19.07.2010
 */
public class PropertyChangeEventsTestUtils {
    private static final PropertyChangeListener testListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // noop
        }
    };

    public static void assertCanAddListener(ChangeEventSender addTo) {
        addListener(addTo, testListener);
        removeListener(addTo, testListener);
    }


    public static void assertCanSetAndGetWithEvents(final ChangeEventSender addTo, final String propertyName, Object value) {
        ExpectSettingValueListener expected = new ExpectSettingValueListener(propertyName,
                value, new NoArgFunc<Object>() {
                    @Override
                    public Object invoke() {
                        return invokeGetter(addTo, propertyName);
                    }
                });
        addListener(addTo, expected);
        setAndCheck(addTo, propertyName, value);
        expected.assertWasCalled();
        removeListener(addTo, expected);
    }

    private static void setAndCheck(Object addTo, String propertyName, Object value) {
        try {
            forMethod(addTo, buildSetterName(propertyName), value.getClass()).invoke(addTo, value);
            Object returnValue = invokeGetter(addTo, propertyName);
            Assert.assertEquals(returnValue, value);
        } catch (Exception e) {
            reThrow(e);
        }
    }

    private static Object invokeGetter(Object addTo, String propertyName) {
        try {
            return forMethod(addTo, buildGetterName(propertyName)).invoke(addTo);
        } catch (Exception e) {
            return reThrow(e);
        }
    }

    private static String buildGetterName(String propertyName) {
        return "get" + propertyMethodName(propertyName);
    }

    private static String propertyMethodName(String propertyName) {
        return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    private static String buildSetterName(String propertyName) {
        return "set" + propertyMethodName(propertyName);
    }

    private static void addListener(ChangeEventSender addTo, PropertyChangeListener listener) {
        addTo.addPropertyChangeListener(listener);
    }

    private static void removeListener(ChangeEventSender addTo, PropertyChangeListener listener) {
        addTo.removePropertyChangeListener(listener);
    }


    private static Method forMethod(Object addTo, String methodName, Class... arguments) {
        try {
            return addTo.getClass().getMethod(methodName, arguments);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }

    }

    private static class ExpectSettingValueListener implements PropertyChangeListener {
        private final String expectedProperty;
        private final Object expectedValue;
        private final NoArgFunc<Object> getter;
        private boolean wasCalled;

        private ExpectSettingValueListener(String expectedProperty,
                                           Object expectedValue,
                                           NoArgFunc<Object> getter) {
            this.expectedProperty = expectedProperty;
            this.expectedValue = expectedValue;
            this.getter = getter;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (expectedProperty.equals(evt.getPropertyName())) {
                assertEquals(getter.invoke(), expectedValue);
                wasCalled = true;

            }
        }

        public void assertWasCalled() {
            Assert.assertTrue(wasCalled, "Expect that event for the property " + expectedProperty + " is called");
        }
    }
}
