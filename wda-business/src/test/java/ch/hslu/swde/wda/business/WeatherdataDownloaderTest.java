package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.City;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherdataDownloaderTest {

    @Test
    void testDownloadAllCities() {

        WeatherdataDownloader weatherdataDownloader = new WeatherdataDownloader();
        List<City> list = null;
        long startTime = System.currentTimeMillis();


        try {
            list = weatherdataDownloader.downloadAllCitiesFromWeb();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;

            System.out.println(String.format("\033[32m Download all cities, got a total number of %d of cities", list.size()));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(list).isNotNull();
        assertThat(list.get(0)).isNotSameAs(list.get(1));
    }


}