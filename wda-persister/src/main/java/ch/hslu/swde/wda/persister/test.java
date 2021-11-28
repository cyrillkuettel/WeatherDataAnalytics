package ch.hslu.swde.wda.persister;

import java.util.ArrayList;
import java.util.List;


import ch.hslu.swde.wda.domain.City;

public class test {

	public static void main(String[] args) {
	
		
		
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

}
