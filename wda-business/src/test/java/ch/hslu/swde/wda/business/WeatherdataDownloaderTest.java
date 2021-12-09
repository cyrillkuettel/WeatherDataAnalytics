package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static ch.hslu.swde.wda.business.WeatherdataDownloader.ALL_SINCE_JANUARY_2020;
import static org.assertj.core.api.Assertions.assertThat;

class WeatherdataDownloaderTest {
    private static final Logger Log = LogManager.getLogger(WeatherdataDownloaderTest.class);

    @Test
    void testDownloadAllCities() {


        WeatherdataDownloader weatherdataDownloader = new WeatherdataDownloader();
        List<City> list = null;
        long startTime = System.currentTimeMillis();


        try {
            list = weatherdataDownloader.downloadAllCities();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;

            System.out.println(String.format("\033[32m Download all cities, got a total number of \033[33m %d \033[32m " +
                                                     "cities", list.size()));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(list).isNotNull();
        assertThat(list.get(0)).isNotSameAs(list.get(1));
        System.out.println("[37m");
        list.stream().map(el -> el.getName()).forEach(System.out::println);
    }

    @Test
    void testDownloadSingleCity() {
        long startTime = System.currentTimeMillis();

        WeatherdataDownloader weatherdataDownloader = new WeatherdataDownloader();
        String city = "Davos";
        List<WeatherData> list;

        String WeatherDataSingleCity = weatherdataDownloader.requestRawXMLData(city + ALL_SINCE_JANUARY_2020);
        try {
           list = weatherdataDownloader.downloadAndPersistWeather_OfSingleCity(WeatherDataSingleCity);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;

        Log.info(String.format("\033[32m Download and Persisted %s in total of %s seconds",
                               city, elapsedSeconds));

    }


}