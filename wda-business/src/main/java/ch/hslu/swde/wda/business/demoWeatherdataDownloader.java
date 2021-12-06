package ch.hslu.swde.wda.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class demoWeatherdataDownloader{
    private static final Logger Log = LogManager.getLogger(demoWeatherdataDownloader.class);

    public static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St.Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St.Gallen"};


    static final String ZUG_ALL_SINCE_JANUARY_2020 = "Zug/since?year=2020&month=1&day=1";
    public static final String BASE_ALL_SINCE_JANUARY_2020 = "/since?year=2020&month=1&day=1";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    public static final WeatherdataDownloader weatherdatadownloader = new  WeatherdataDownloader();


    public static void downloadAndPersistWeatherDataSingleCit(String city) {
        long startTime = System.currentTimeMillis();

        String WeatherDataSingleCity = weatherdatadownloader.requestRawXMLData(city + BASE_ALL_SINCE_JANUARY_2020);

        try {
            weatherdatadownloader.downloadAndPersistWeatherDataSingleCity(WeatherDataSingleCity);
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;

            System.out.println(String.format("\033[32m Download and Persisted %s in total of %s seconds",
                                             city, elapsedSeconds));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static List<String> logs = new ArrayList<>();
    public static void main(String[] args) {


        for (String city: cities) {
            downloadAndPersistWeatherDataSingleCit(city);
        }

        for (String log : logs) {
            System.out.println(log);
        }



    }
}
