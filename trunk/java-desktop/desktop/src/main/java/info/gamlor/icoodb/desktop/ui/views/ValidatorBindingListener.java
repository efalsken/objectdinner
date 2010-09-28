package info.gamlor.icoodb.desktop.ui.views;

import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;

import java.awt.*;

/**
 * @author roman.stoffel@gamlor.info
 * @since 25.07.2010
 */
class ValidatorBindingListener extends AbstractBindingListener {

    public ValidatorBindingListener() {
    }

    @Override
    public void syncFailed(Binding binding, Binding.SyncFailure fail) {

        if (foundValidationError(binding, fail)) {
            changeBackground(binding, ColorScheme.INVALID);
        }
    }

    @Override
    public void synced(Binding binding) {

        if (foundValidationError(binding, null)) {
            changeBackground(binding, ColorScheme.VALID);
        }
    }

    private void changeBackground(Binding binding, Color color) {
        Component occured = (Component) binding.getTargetObject();
        occured.setBackground(color);
    }

    private boolean foundValidationError(Binding binding, Binding.SyncFailure fail) {
        //fail isn't existing, if it's a successful sync
        if (fail != null) {
            //Only handle signal if it's a validation error
            if (fail.getType().equals(Binding.SyncFailureType.VALIDATION_FAILED)
                    || fail.getType().equals(Binding.SyncFailureType.CONVERSION_FAILED)) {
                return isComponent(binding);
            }
        } else if (binding.getValidator() != null) {
            return isComponent(binding);
        }
        return false;
    }

    private boolean isComponent(Binding binding) {
        return Component.class.isAssignableFrom(binding.getTargetObject().getClass());
    }
}