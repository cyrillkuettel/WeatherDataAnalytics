package ch.hslu.swde.wda.persister;

import java.util.List;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;

public class PersistWeatherData {
	
public static void insertWeatherData(List<WeatherData> weatherData) {
		

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction( ).begin( );
		
		for (WeatherData wd : weatherData) {
			
			wd.setCity(em.find(City.class, wd.getCity().getZIPCode()));
			
		em.persist(wd);
		
		
		}
		
		em.getTransaction().commit();  
		em.close();
		
	}                          

}
