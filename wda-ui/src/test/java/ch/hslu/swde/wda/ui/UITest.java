package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.CheckConnection.Utils;
import ch.hslu.swde.wda.GlobalConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Testcases fuer {@link ch.hslu.swde.wda.ui.UI}.
 */

// create tests to check if date in valid timespan 01.01.2020 ( integrate this in project afterwards)
// create tests to check if user added

class UITest {
    private static final Logger Log = LogManager.getLogger(UITest.class);

    @Test
    void testVPNConnection() {
        final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities";
        assertTrue(Utils.pingURL(CITY_URL, 10000));
    }



    @Test
    void testLoginShouldRefuseEmptyStrings() {
        UI ui = new UI();
        String username = "";
        String password = "";
        assertThat(ui.simpleLoginValidationPassed(username, password)).isFalse();
    }

    @Test
    void testMaximumUsernameLengthShouldNotExceedAValue() {
        UI ui = new UI();
        String username = "";
        String password = "asdfadsfdsfdsf";
        for (int i = 0; i < GlobalConstants.MAX_USERNAME_LEN * 2; i++) {
            username += "u";
        }
        assertThat(ui.simpleLoginValidationPassed(username, password)).isFalse();
    }

    @Test
    void testPasswordAtLeast8Characters() {
        UI ui = new UI();
        String username = "test";
        String password = "pw";
        assertThat(ui.simpleLoginValidationPassed(username, password)).isFalse();
    }

    @Test
    void testGetTimePeriodForMaximumDateRange() {

        InputStream sysInBackup = System.in; // backup System.in to restore it later
        String[] expectedTimeFrame = {GlobalConstants.MIN_DATE_VALUE, GlobalConstants.MAX_DATE_VALUE};
        ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes()); // Option 2 means we use all dates
        System.setIn(in);
        UI ui = new UI();
        // Test with System.in
        String[] actualTimeFrame = ui.getTimePeriod();
        assertThat(actualTimeFrame).isEqualTo(expectedTimeFrame);

        // reset System.in to its original
        System.setIn(sysInBackup);
    }




    @Test
    void testDummyScannnerParsingMultipleInputs() {

        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("This Works now".getBytes());
        System.setIn(in);
        UI ui = new UI();

        // Test with System.in
        List<String> result = ui.dummyScanner(); // Observe that the scan stops at space
        assertThat(result.get(0)).isEqualTo("This");
        assertThat(result.get(1)).isEqualTo("Works");
        assertThat(result.get(2)).isEqualTo("now");
        System.out.println(result);

        System.setIn(sysInBackup);
    }

    @Test
    @Disabled
    void testTryToParseDate_usingvalidDate() {
        String validDate = "10.01.2099";
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(validDate.getBytes());
        System.setIn(in);
        UI ui = new UI();

        String date = ui.tryToParseDate();
        assertEquals(date, validDate);
        // reset System.in to its original
        System.setIn(sysInBackup);

    }

    @Test
    void testTryToParseDate_usingDateWithoutYear() {
        String dateWithNoYearSpecified = "10.11."; // actually, it's always 2020
        assertTrue(UI.isValidDate(dateWithNoYearSpecified));

    }

    @Test
    @Disabled
    void testTryToParseDate_usingDateWithWrongYear() { // test what happens when the year is not 2020
        String dateWithNoYearSpecified = "27.11.1999";
        assertFalse(UI.isValidDate(dateWithNoYearSpecified));

    }

    @Test
    void testinferYearIfNotPresent() {
        String dateWithoutYear = "10-11-";
        // actually, year always 2020. It should infer year = 2020 if no year is specified
        dateWithoutYear = UI.inferYearIfNotPresent(dateWithoutYear);
        assertThat(dateWithoutYear).isEqualTo("10-11-2020");
    }

    @Test
    @Disabled
    void testTryToParseDate_usingWeirdDateInput() {
        String Not_A_Date = "1-1-1";
        assertFalse(UI.isValidDate(Not_A_Date));
    }

    @Test
    void testTryToParseDate_usingWeirdDateInput2() {
        String completlyIncorrectDate = "10.11"; // expect everything!
        assertFalse(UI.isValidDate(completlyIncorrectDate));
    }


    @Test
    void testaskForUsernamePassword() {
        String[] expectedUserCredidentals = {"username", "ultimate_secret_password"};
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("username ultimate_secret_password".getBytes());
        System.setIn(in);

        UI ui = new UI();
        String[] credentials = ui.askForUsernamePassword(0);
        assertThat(credentials).isEqualTo(expectedUserCredidentals);
        // reset System.in to its original
        System.setIn(sysInBackup);
    }


    @Test
    void testisValidDateFromString() {
        assertThat(GlobalConstants.DATE_FORMAT).isEqualTo("dd-MM-yyyy");
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