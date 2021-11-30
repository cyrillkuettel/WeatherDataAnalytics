package ch.hslu.swde.wda.domain;

import java.util.Map;
import java.io.*;

public class ObjectMaker {
	

	
	public static void test(Map data) {
		System.out.println(data);
	}
	
	public static WeatherData create(Map data) {
		//System.out.println(data);
		
		WeatherData wdata = new WeatherData();
		System.out.println(data.get("CURRENT_TEMPERATURE_CELSIUS"));
	
		
	



	//	wdata.setHumidity(Float.parseFloat((String) data.get("Humidity")));
		
		
		
		
	
		
		
		return null;
		
	}

}
