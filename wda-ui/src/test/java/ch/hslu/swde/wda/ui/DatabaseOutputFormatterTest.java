package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseOutputFormatterTest {
    private static final Logger Log = LogManager.getLogger(DatabaseOutputFormatterTest.class);


    /** Query A01 */
    @Test
    void atLeastFortyCitiesInDb() {
        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();
       List<String> availableCities = dof.getCityNamesAsList();
        assertThat(availableCities.size()).isGreaterThanOrEqualTo(40);
    }

    @Test /** Query A02 */
    void testSelectWeatherByDataAndCity() {
        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();
        String start = "2020-12-30";
        String end = "2021-11-28";
        String cityname = "Zug";
        List<WeatherData> list = null;

        try {
            list = dof.selectWeatherByDateAndCity(cityname, start, end);
        } catch (jakarta.persistence.NoResultException e) {
            Log.warn("that city does not (yet) exist in database");
            e.printStackTrace();
        }

        String nameOfCityFromDB = list.get(0).getCity().getName();
        assertThat(nameOfCityFromDB).isEqualTo(cityname);
        assertThat(list.size()).isNotZero();
    }


    @Test /** Query A03 */
    void testAverageForCity() {
        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();
        String start = "2020-12-30";
        String end = "2021-11-28";
        String cityname = "Zug";

        String averageData; // Temperatur, Luftdruck und Feuchtigkeit

        averageData = dof.selectAverageWeatherDataSingleCity(cityname, start, end);


        assertThat(averageData).isNotBlank();
        assertThat(averageData).isNotNull();
    }


    @Test
    void testGetSingleCityFromDatabase() {
        final String cityName = "Zug";
        DbHelper dbHelper = new DbHelper();

        final City city = dbHelper.selectSingleCity(cityName);
        AssertionsForClassTypes.assertThat(city.getName()).isEqualTo(cityName);
    }


    @Test
    void testSelectAllCitiesAsList() {
        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();
        List<String> cities = dof.getCityNamesAsList();
        assertThat(cities.size()).isGreaterThanOrEqualTo(40);
    }



    @Test
    void testLoadingCitiesFromDatabaseContainsAllKnownCities() {
        String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
                "Interlaken",
                "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
                "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
                "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "Zurich",
                "Winterthur", "Frauenfeld"};

        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();
        List<String> actualCitiesInDB = dof.getCityNamesAsList();


        assertTrue(actualCitiesInDB.containsAll(Arrays.asList(cities)));
    }



}