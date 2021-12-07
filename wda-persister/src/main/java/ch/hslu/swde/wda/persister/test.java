package ch.hslu.swde.wda.persister;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;

public class test {

	private static PersistWeatherData persistWeatherData;
	
//	private static PersistCity persistCity;

	public static void main(String[] args) {

		persistWeatherData = new PersistWeatherData();
//		persistCity = new PersistCity();
		insertWeatherData();

	}

//	public static void insertCity() {
//		City city1 = new City(4900, "Langenthal");
//		City city2 = new City(4901, "Bern");
//		City city3 = new City(8000, "Zurich");
//		City city4 = new City(8180, "Bulach");
//
//		List<City> cityList = new ArrayList<City>();
//		cityList.add(city1);
//		cityList.add(city2);
//		cityList.add(city3);
//		cityList.add(city4);
//		persistCity.insertCities(cityList);
//
//	}

	public static void insertWeatherData() {
		City city1 = new City(8000, "Zurich");
//		
//
//		List<City> cityList = new ArrayList<City>();
//		cityList.add(city1);
//		persistCity.insertCities(cityList);

		List<WeatherData> weatherDataList = new ArrayList<WeatherData>();

		WeatherData weatherData1 = new WeatherData(city1, Timestamp.valueOf("2021-01-10 13:30:57"), 2.0, 4, 3.5);

		weatherDataList.add(weatherData1);
		persistWeatherData.insertWeatherData(weatherDataList);

	}

}
