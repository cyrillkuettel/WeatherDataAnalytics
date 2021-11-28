package ch.hslu.swde.wda.persister;

import java.util.List;

import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PersistCity {

	public static void insertCity(List<City> cities) {
		

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction( ).begin( );
		
		for (City c : cities) {
		em.persist(c);
		}
		
		em.getTransaction().commit();  
		em.close();
		
	}
	
	
}
