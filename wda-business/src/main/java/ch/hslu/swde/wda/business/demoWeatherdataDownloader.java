package ch.hslu.swde.wda.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class demoWeatherdataDownloader{
    private static final Logger Log = LogManager.getLogger(demoWeatherdataDownloader.class);

    public static void main(String[] args) {

        WeatherdataDownloader weatherdatadownloader = new  WeatherdataDownloader();

        String cities = weatherdatadownloader.requestRawXMLData("cities/");
        // System.out.println(cities);
        /*
        try {
            wd.parseXML(cities);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

         */

        String WeatherDataSingleCity = weatherdatadownloader.requestRawXMLData(WeatherdataDownloader.TESTZ_ZUG_ALL_SINCE_2020);

        try {
            weatherdatadownloader.parseWeatherData(WeatherDataSingleCity);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
