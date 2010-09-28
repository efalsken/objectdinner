package info.gamlor.icoodb.desktop.logic;

import info.gamlor.icoodb.desktop.model.RSVP;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class RSVPValidator implements BusinessValidator<RSVP> {
    private final NotEmptyValidator notEmpty = new NotEmptyValidator();

    @Override
    public boolean isValid(RSVP value) {
        return notEmpty.isValid(value.getName());
    }
}
