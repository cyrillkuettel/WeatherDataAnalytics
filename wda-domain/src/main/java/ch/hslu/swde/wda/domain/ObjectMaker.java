package ch.hslu.swde.wda.domain;

import java.util.Map;

public class ObjectMaker {
	

	
	public static void test(Map data) {
		System.out.println(data);
	}
	
	public static WeatherData create(Map data) {
		//Hier eine Objekt der Klasse WeatherData erstellen welches wir in der Datenbank speichern.
		WeatherData wdata = new WeatherData();
		System.out.println(data.get("CITY_ZIP").getClass());
		
		
	
		
		
		return null;
		
	}

}
