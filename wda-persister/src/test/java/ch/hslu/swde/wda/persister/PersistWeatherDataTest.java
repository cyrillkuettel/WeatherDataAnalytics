package ch.hslu.swde.wda.persister;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;

public class PersistWeatherDataTest {

	@Test
	void testSelectWeatherByDataAndCity() {

		City bern = new City(3000, "Bern");
		WeatherData weatherData1 = new WeatherData(bern,Timestamp.valueOf("9999-12-31 00:00:00"),2.0,4,3.5);

		List <WeatherData> weatherDataList = new ArrayList<WeatherData>();
		weatherDataList.add(weatherData1);
		
//		PersistWeatherData.insertWeatherData(weatherDataList);
//		PersistCity.insertSingleCity(bern);

//		assertThat(cities.size()).isNotZero();

	}

}
