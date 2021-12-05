package ch.hslu.swde.wda.persister;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;

public class test {

	public static void main(String[] args) {
		
		insertWeatherData();

	}
	
	public static void insertCity() {
		City city1 = new City(4900,"Langenthal");
		City city2 = new City(4901,"Bern");
		City city3 = new City(8000,"Zurich");
		City city4 = new City(8180,"Bulach");
		
		List <City> cityList = new ArrayList<City>();
		cityList.add(city1);
		cityList.add(city2);
		cityList.add(city3);
		cityList.add(city4);
		PersistCity.insertCities(cityList);
		
	}
	
	
	public static void insertWeatherData() {
		City city1 = new City(4900,"Langenthal");
		City city2 = new City(4901,"Bern");
		City city3 = new City(8000,"Zurich");
		City city4 = new City(8180,"Bulach");
		City city5 = new City(9999,"TestCity");
		
		List <City> cityList = new ArrayList<City>();
		cityList.add(city1);
		cityList.add(city2);
		cityList.add(city3);
		cityList.add(city4);
		cityList.add(city5);
		PersistCity.insertCities(cityList);
		
		List <WeatherData> weatherDataList = new ArrayList<WeatherData>();
		
		WeatherData weatherData1 = new WeatherData(city4,Timestamp.valueOf("2021-01-10 13:30:57"),2.0,4,3.5);
		
		weatherDataList.add(weatherData1);
		PersistWeatherData.insertWeatherData(weatherDataList);

	}

}
