package ch.hslu.swde.wda.persister;

import java.util.ArrayList;
import java.util.List;


import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.Weather;

public class test {

	public static void main(String[] args) {
	
		
		
		insertWeather();


	}
	
	public static void insertCity() {
		//int zipCode,String name, float longitude, float latitude 
		City city1 = new City(4900,"Langenthal",3.0f,22.11f);
		City city2 = new City(4901,"Bern",5.6f,4.922f);
		City city3 = new City(8000,"Zurich",2.7f,5.9343f);
		
		List <City> cityList = new ArrayList();
		cityList.add(city1);
		cityList.add(city2);
		cityList.add(city3);
		PersistCity.insertCity(cityList);
		
	}
	
	public static void insertWeather() {
		//int weatherID, String summary, String description
		Weather weather1 = new Weather("Snow","snow");
		Weather weather2 = new Weather("Clouds","few clouds");
		Weather weather3 = new Weather("Rain","light intensity shower rain");
		
		List <Weather> weatherList = new ArrayList();
		weatherList.add(weather1);
		weatherList.add(weather2);
		weatherList.add(weather3);
		PersistWeather.insertWeather(weatherList);
		
	
	}

}
