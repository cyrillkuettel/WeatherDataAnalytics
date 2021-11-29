package ch.hslu.swde.wda.ui;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testcases fuer {@link ch.hslu.swde.wda.ui.UI}.
 */
class UITest {

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


    @Test
    void testCitiesLengthIfCitiesConstant() {
        UI ui = new UI();
        final int expectedCityLength = 40; // if new cities are added, this will fail!
        assertTrue(ui.getCitiesFromDatabase().length >= expectedCityLength);

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