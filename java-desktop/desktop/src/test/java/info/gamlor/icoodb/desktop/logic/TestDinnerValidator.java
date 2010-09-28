package info.gamlor.icoodb.desktop.logic;

import info.gamlor.icoodb.desktop.model.Dinner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author roman.stoffel@gamlor.info
 * @since 26.07.2010
 */
public class TestDinnerValidator {
    private Dinner dinner;
    private final DinnerValidator toTest = new DinnerValidator();

    @BeforeMethod
    public void setup() {
        this.dinner = new Dinner();
    }

    @Test
    public void isValid() {
        fillDinner();
        assertTrue(toTest.isValid(dinner));
    }

    @Test
    public void emptyIsInvalid() {
        assertFalse(toTest.isValid(dinner));
    }

    @Test
    public void notTitleIsInvalid() {
        fillDinner();
        dinner.setTitle("");
        assertFalse(toTest.isValid(dinner));
    }

    @Test
    public void notHostIsInvalid() {
        fillDinner();
        dinner.setHostedBy("");
        assertFalse(toTest.isValid(dinner));
    }

    @Test
    public void notAddressIsInvalid() {
        fillDinner();
        dinner.setAddress("");
        assertFalse(toTest.isValid(dinner));
    }

    private void fillDinner() {
        dinner.setTitle("Title");
        dinner.setHostedBy("Me");
        dinner.setAddress("Addr");
    }
}
