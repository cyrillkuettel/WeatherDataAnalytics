package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.GlobalConstants;
import ch.hslu.swde.wda.NetworkUtils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Testcases fuer {@link ch.hslu.swde.wda.ui.UI}.
 */


class UITest {
    private static final Logger Log = LogManager.getLogger(UITest.class);

    @Test
    void testVPNConnection() {
        final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities";
        assertTrue(Utils.pingURL(CITY_URL, 10000));
    }

    @Test
    void testRemoveRedundantPathDirecotry() {

        String incorrectPath = "/home/cyrill/Desktop/g07-wda/wda-ui/wda-ui/client.policy";
        String adjustedPath = UI.removeReduntantPathDirectory(incorrectPath);
        assertThat(adjustedPath).isEqualTo("/home/cyrill/Desktop/g07-wda/wda-ui/client.policy");
    }

    @Test
    void testRemoveRedundantPathDirecotry_ShouldNotChangePath() {

        String incorrectPath = "/home/cyrill/Desktop/g07-wda/wda-ui/client.policy";
        String adjustedPath = UI.removeReduntantPathDirectory(incorrectPath);
        assertThat(adjustedPath).isEqualTo("/home/cyrill/Desktop/g07-wda/wda-ui/client.policy");
    }


    @Test
    @Disabled
    void testAllCitiesWithStandardDate() throws RemoteException {
        InputStream sysInBackup = System.in; // backup System.in to restore it later

        ByteArrayInputStream in =    // bedeutet: Alle Ortschaften, mit StandartDatum
                new ByteArrayInputStream("Cyrill Küttel test1234 1 2 1 skip".getBytes());
        System.setIn(in);
        UI ui = new UI();
        ui.startFromBeginning();

        // reset System.in to its original
        System.setIn(sysInBackup);
    }


    @Test
    void test_rmi() {
        UI ui = new UI();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            Date parsedDate = dateFormat.parse("2021-11-28 00:33:30.0");
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            List<String> min = ui.stub.selectMinTemperatureAllCities(timestamp);
            assertThat(min.get(0)).isNotBlank();
            Log.info(min);
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
    }



    @Test
    @Disabled
    void testLoadDataForZurich() throws RemoteException {
        //   simulate user input.

        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in =
                new ByteArrayInputStream("Cyrill Küttel test1234 1 1 39 1 10.08.2021 30.08.2021 2 1 2 3 0".getBytes());
        System.setIn(in);
        UI ui = new UI();
        ui.startFromBeginning();
        // reset System.in to its original
        System.setIn(sysInBackup);
    }

    @Test
    @Disabled
    void testEditUsers() throws RemoteException {
        //   simulate user input.
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in =
                new ByteArrayInputStream("Cyrill Küttel test1234 3 0".getBytes());
        System.setIn(in);
        UI ui = new UI();
        ui.startFromBeginning();
        // reset System.in to its original
        System.setIn(sysInBackup);
    }



    @Test
    void testLoginShouldRefuseEmptyStrings() {
        UI ui = new UI();
        String forename = "";
        String surname = "";
        String password = "";
        String[] creds = {forename, surname, password };
        assertThat(ui.simpleLoginValidationPassed(creds)).isFalse();
    }



    @Test
    void testPasswordAtLeast8Characters() {
        UI ui = new UI();
        String forename = "test123";
        String surname = "blablabla";
        String password = "pw";
        String[] creds = {forename, surname, password };
        assertThat(ui.simpleLoginValidationPassed(creds)).isFalse();
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