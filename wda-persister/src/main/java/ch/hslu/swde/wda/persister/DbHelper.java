package ch.hslu.swde.wda.persister;

import java.util.List;

import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DbHelper {

	
	public static void selectCity(String name) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction( ).begin( );
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c where c.name= :name", City.class);
		tQry.setParameter("name", name);

		/* Alle Adresse-Entities aus der DB holen */
		List<City> cityFromDb = tQry.getResultList();

	
		em.close();
		
		for (City c : cityFromDb) {
			System.out.println(c.toString());
		}
	}
	


}
