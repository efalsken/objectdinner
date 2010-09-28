package info.gamlor.icoodb.desktop.logic;

import info.gamlor.icoodb.desktop.model.Dinner;

/**
 * @author roman.stoffel@gamlor.info
 * @since 26.07.2010
 */
public class DinnerValidator implements BusinessValidator<Dinner> {
    private final NotEmptyValidator notEmpty = new NotEmptyValidator();

    public boolean isValid(Dinner dinner) {
        return hasTitle(dinner)
                && hasHost(dinner)
                && hasAddress(dinner);
    }

    private boolean hasHost(Dinner dinner) {
        return notEmpty.isValid(dinner.getHostedBy());
    }

    private boolean hasAddress(Dinner dinner) {
        return notEmpty.isValid(dinner.getAddress());
    }

    private boolean hasTitle(Dinner dinner) {
        return notEmpty.isValid(dinner.getTitle());
    }
}
