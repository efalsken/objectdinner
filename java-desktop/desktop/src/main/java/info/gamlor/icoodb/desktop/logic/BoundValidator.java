package info.gamlor.icoodb.desktop.logic;

import info.gamlor.icoodb.desktop.utils.Disposable;
import info.gamlor.icoodb.desktop.utils.EventListeners;
import info.gamlor.icoodb.desktop.utils.OneArgAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import static info.gamlor.icoodb.desktop.utils.ExceptionUtils.reThrow;

/**
 * @author roman.stoffel@gamlor.info
 * @since 26.07.2010
 */
public final class BoundValidator<T> implements Disposable {
    private final T bean;
    private final BusinessValidator<T> validator;
    private final EventListeners<OneArgAction<Boolean>> onChange = EventListeners.create(OneArgAction.class);
    private boolean lastValidationResult;
    private PropertyChangeListener listener;

    private BoundValidator(T bean, BusinessValidator<T> validator) {
        this.bean = bean;
        this.validator = validator;
        listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                reValidate();
            }
        };
        addListener(bean, listener);
    }

    public static <T> BoundValidator<T> create(T bean, BusinessValidator<T> validator) {
        return new BoundValidator<T>(bean, validator);
    }

    public boolean isValid() {
        return internalValidCheck();
    }

    @Override
    public void dispose() {
        removeListener(bean, listener);
    }

    public Disposable onChange(OneArgAction<Boolean> listener) {
        return onChange.add(listener);
    }


    private void reValidate() {
        boolean currentState = internalValidCheck();
        fireEventsIfNeeded(currentState);
        lastValidationResult = currentState;
    }

    private void fireEventsIfNeeded(boolean currentState) {
        if (stateHasChanged(currentState)) {
            onChange.invoker().invoke(currentState);
        }
    }

    private boolean stateHasChanged(boolean currentState) {
        return currentState != lastValidationResult;
    }

    private boolean internalValidCheck() {
        return validator.isValid(bean);
    }

    private static void addListener(Object addTo, PropertyChangeListener listener) {
        try {
            listenerMethod(addTo, "addPropertyChangeListener").invoke(addTo, listener);
        } catch (Exception e) {
            reThrow(e);
        }
    }

    private static void removeListener(Object addTo, PropertyChangeListener listener) {
        try {
            listenerMethod(addTo, "removePropertyChangeListener").invoke(addTo, listener);
        } catch (Exception e) {
            reThrow(e);
        }
    }

    private static Method listenerMethod(Object addTo, String methodName) {
        return forMethod(addTo, methodName, PropertyChangeListener.class);
    }

    private static Method forMethod(Object addTo, String methodName, Class... arguments) {
        try {
            return addTo.getClass().getMethod(methodName, arguments);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }

    }
}
