package ch.hslu.swde.wda.persister;

import java.util.List;

import ch.hslu.swde.wda.domain.Weather;
import jakarta.persistence.EntityManager;

public class PersistWeather {
	
public static void insertCity(List<Weather> weather) {
		

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction( ).begin( );
		
		for (Weather w : weather) {
		em.persist(w);
		}
		
		em.getTransaction().commit();  
		em.close();
		
	}                                                                                                                                                                                                                                                                                                                                                                               

}
