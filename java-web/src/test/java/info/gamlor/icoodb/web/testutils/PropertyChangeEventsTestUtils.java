package info.gamlor.icoodb.web.testutils;

import org.testng.Assert;

import java.lang.reflect.Method;

import static info.gamlor.icoodb.web.utils.ExceptionUtils.reThrow;

/**
 * @author roman.stoffel@gamlor.info
 * @since 03.08.2010
 */
public class PropertyChangeEventsTestUtils {


    public static void assertCanSetAndGetWithEvents(final Object addTo, final String propertyName, Object value) {
        setAndCheck(addTo, propertyName, value);
    }


    private static void setAndCheck(Object addTo, String propertyName, Object value) {
        try {
            forMethod(addTo, buildSetterName(propertyName), mapEventualPrimitives(value)).invoke(addTo, value);
            Object returnValue = invokeGetter(addTo, propertyName);
            Assert.assertEquals(returnValue, value);
        } catch (Exception e) {
            reThrow(e);
        }
    }

    private static Class mapEventualPrimitives(Object value) {
        Class type = value.getClass();
        if (type.equals(Double.class)) {
            return double.class;
        }
        return type;
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

    private static Method forMethod(Object addTo, String methodName, Class... arguments) {
        try {
            return mapEventualPrimitives(addTo).getMethod(methodName, arguments);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }

    }
}
