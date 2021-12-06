package ch.hslu.swde.wda.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class demoWeatherdataDownloader{
    private static final Logger Log = LogManager.getLogger(demoWeatherdataDownloader.class);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        WeatherdataDownloader weatherdatadownloader = new  WeatherdataDownloader();


        String WeatherDataSingleCity = weatherdatadownloader.requestRawXMLData(WeatherdataDownloader.ZUG_ALL_SINCE_JANUARY_2020);

        try {
            weatherdatadownloader.downloadAndPersistWeatherDataSingleCity(WeatherDataSingleCity);
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;

            System.out.println(String.format("\033[32m Download and Persisted Data in total of %s seconds",
                                             elapsedSeconds));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
