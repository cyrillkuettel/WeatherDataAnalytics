package ch.hslu.swde.wda.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class demoWeatherdataDownloader{
    private static final Logger Log = LogManager.getLogger(demoWeatherdataDownloader.class);

    public static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St. Gallen"};


    public static final String BASE_ALL_SINCE_JANUARY_2020 = "/since?year=2020&month=1&day=1";
    public static final WeatherdataDownloader weatherdatadownloader = new  WeatherdataDownloader();

    static List<String> logs = new ArrayList<>();
    public static void main(String[] args) {
        // Download all Data. This runs only one time
        Arrays.stream(cities).parallel().forEach(demoWeatherdataDownloader::downloadAndPersistWeatherDataSingleCit);
        logs.stream().forEachOrdered(System.out::println);
    }

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

}
