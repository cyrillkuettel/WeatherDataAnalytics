package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.CheckConnection.Utils;
import ch.hslu.swde.wda.GlobalConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

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
    @Disabled
    void testTryToParseDate_usingDateWithWrongYear() { // test what happens when the year is not 2020
        UI ui = new UI();
        String dateWithYearOutOfRange = "27.11.1999";
        assertFalse(ui.isValidDate(dateWithYearOutOfRange));

    }




    @Test
    void testTryToParseDate_usingWeirdDateInput2() {
        String completlyIncorrectDate = "10.11"; // expect everything!
        UI ui = new UI();
        assertFalse(ui.isValidDate(completlyIncorrectDate));
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
        UI ui = new UI();

        assertThat(GlobalConstants.DATE_FORMAT).isEqualTo("dd-MM-yyyy");
        String testDate = "27-11-2020";
        assertTrue(ui.isValidDate(testDate));
    }

    @Test
    void testisValidDateFromStringDifferentFormat() {
        // The validDate method should be able to handle both formats
        UI ui = new UI();

        String testDate = "27.11.2020";

        assertTrue(ui.isValidDate(testDate));
    }


    @Test
    void testDateFormatter() {
        UI ui = new UI();
        String testDate = "27-11-2020";  //
        String testDate2 = "31-01-2020";

        String formattedDate = ui.transformDateToDifferentFormat(testDate);
        assertThat(formattedDate).isEqualTo("2020-11-27");

        String formattedDate2 = ui.transformDateToDifferentFormat(testDate2);
        assertThat(formattedDate2).isEqualTo("2020-01-31");
    }

    @Test
    void testtransformDateToDifferentFormat() {

        UI ui = new UI();
        String[] selectedTimePeriod = {"27-11-2021", "30-11-2021"};
        String[] expectedDateFormat_AfterTranslation = {"2021-11-27", "2021-11-30"};

       selectedTimePeriod =
               Arrays.stream(selectedTimePeriod).map(ui::transformDateToDifferentFormat).toArray(String[]::new);

        assertThat(selectedTimePeriod).isEqualTo(expectedDateFormat_AfterTranslation);
    }

    @Test
    void testReplacePointsWithDashes() {
             UI ui = new UI();

         String dateWithPoints = "27.11.2021";
         String replaced = ui.replacePointsWithDashes(dateWithPoints);

         assertThat(replaced).isEqualTo("27-11-2021");
    }


}