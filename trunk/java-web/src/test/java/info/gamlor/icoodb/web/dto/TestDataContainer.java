package info.gamlor.icoodb.web.dto;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.AssertJUnit.assertFalse;

/**
 * @author roman.stoffel@gamlor.info
 * @since 17.08.2010
 */
public class TestDataContainer {
    @Test
    public void encodesMultiple(){
        DinnerViewModel d1 = createDinnerWithHtml();
        DinnerViewModel d2 = createDinnerWithHtml();
        DataContainer<DinnerViewModel> container = DataContainer.create(Arrays.asList(d1,d2));
        for (DinnerViewModel viewModel : container.getRows()) {
            assertFalse(viewModel.getAddress().contains("<"));
        }
    }

    private DinnerViewModel createDinnerWithHtml() {
        DinnerViewModel dinner = new DinnerViewModel();
        dinner.setAddress("<hi>");
        return dinner;
    }
}
