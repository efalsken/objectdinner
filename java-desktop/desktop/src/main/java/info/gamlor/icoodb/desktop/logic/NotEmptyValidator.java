package info.gamlor.icoodb.desktop.logic;

import org.jdesktop.beansbinding.Validator;

/**
 * @author roman.stoffel@gamlor.info
 * @since 25.07.2010
 */
public final class NotEmptyValidator extends Validator<String> implements BusinessValidator<String> {
    @Override
    public Result validate(String value) {
        if (isValid(value)) {
            return null;
        }
        return new Result("Empty", "This field cannot be empty!");
    }

    public boolean isValid(String value) {
        return null != value && validateValue(value);
    }

    private boolean validateValue(String value) {
        if ("".equals(value.trim())) {
            return false;
        }
        return true;
    }
}
