package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseOutputFormatterTest {
    private static final Logger Log = LogManager.getLogger(DatabaseOutputFormatterTest.class);

    @Test
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

    @Test
    void testSelectAllCities() {
        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();

        String cityname = "Brig";

        List<City> list = null;

        try {
            list = DbHelper.selectAllCities();
        } catch (jakarta.persistence.NoResultException e) {
            Log.warn("that city does not (yet) exist in database");
            e.printStackTrace();
        }

        assertThat(list.size()).isNotZero();
        Log.info(list.size());
     //   list.stream().map(City::getName).findAny(el -> el == cityname);

    }






}