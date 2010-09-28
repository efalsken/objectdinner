package info.gamlor.icoodb.desktop.logic;

import org.jdesktop.beansbinding.Validator;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;


/**
 * @author roman.stoffel@gamlor.info
 * @since 25.07.2010
 */
public class TestNotEmptyValidator {
    private final NotEmptyValidator toTest = new NotEmptyValidator();

    @Test
    public void canHandleNull() {
        final Validator.Result result = toTest.validate(null);
        assertNotNull(result);
    }

    @Test
    public void nonEmptyIsValid() {
        final Validator.Result result = toTest.validate("content");
        assertNull(result);
    }

    @Test
    public void emptyIsNotValid() {
        final Validator.Result result = toTest.validate("");
        assertNotNull(result);
    }

    @Test
    public void nonVisibleCharactersAreAlsoEmpty() {
        final Validator.Result result = toTest.validate(" \t");
        assertNotNull(result);
    }
}
