package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.domain.WeatherData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DatabaseOutputFormatterTest {

    @Test
    void testSelectWeatherByDataAndCity() {
        DatabaseOutputFormatter dof = new DatabaseOutputFormatter();
       // dof.insertTestCities();

        String start = "2020-12-30";
        String end = "2021-11-28";
        String cityname = "Langenthal";


        List<WeatherData> list = dof.selectWeatherByDateAndCity(cityname, start, end);
        assertThat(list.size()).isNotZero();

    }

}