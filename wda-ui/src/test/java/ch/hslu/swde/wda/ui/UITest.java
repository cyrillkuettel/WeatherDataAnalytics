package ch.hslu.swde.wda.ui;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testcases fuer {@link ch.hslu.swde.wda.ui.UI}.
 */

// create tests to check if date in valid timespan 01.01.2020
    // create tests to check if user added
class UITest {

    @Disabled
    @Test
    void testLoadingCitiesFromDatabaseContainsAllKnownCities() {

        /* This could be a Subset of all the cities. */
          final String[] minimalExpectedCities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
                "Interlaken",
                "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
                "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
                "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
                "Winterthur", "Frauenfeld", "St. Gallen"};

            UI ui = new UI();
            String[] actualCities = ui.getCitiesFromDatabase();
            assertTrue(Arrays.asList(actualCities).containsAll(Arrays.asList(minimalExpectedCities)));
    }

    @Disabled
    @Test
    void testCitiesLengthIfCitiesConstant() {
        UI ui = new UI();
        final int expectedCityLength = 40; // if new cities are added, this will fail!
        assertTrue(ui.getCitiesFromDatabase().length >= expectedCityLength);

    }

    @Test
    void testsimpleLoginValidationPassed_ShouldFailForEmptyStrings() {
        UI ui = new UI();
        String username = "";
        String password = "";
        assertThat(ui.simpleLoginValidationPassed(username, password)).isFalse();
    }

    @Test
    void testMaximumUsernameLengthShouldNotExceedAValue() {
        UI ui = new UI();
        String username = "";
        String password = "";
        for (int i = 0; i < UI.MAX_USERNAME_LEN ; i++) {
            username += "u";
        }
        assertThat(ui.simpleLoginValidationPassed(username, password)).isFalse();
    }

     @Test
     @Disabled
     void testaskForUsernamePassword() {
        // test the scanner in functionality

     }


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