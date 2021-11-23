package ch.hslu.swde.wda.ui;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Testcases fuer {@link ch.hslu.swde.wda.ui.UI}.
 */
class UITest {

    @Test
    void testisValidDateFromString() {
        assertThat(UI.DATE_FORMAT).isEqualTo("dd-MM-yyyy");
        String testDate = "27-11-2020";
        assertTrue(UI.isValidDate(testDate));
    }

    @Test
    void testisValidDateFromStringDifferentFormat() {
        // The validDate method should be able to handle both formats
        String testDate = "27.11.2020";
        assertTrue(UI.isValidDate(testDate));
    }


}