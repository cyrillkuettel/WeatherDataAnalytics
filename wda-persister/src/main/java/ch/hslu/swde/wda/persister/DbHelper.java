package ch.hslu.swde.wda.persister;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DbHelper {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);

	/**
	 * This Query returns all Cities, for which weather data is available - Query
	 * A01
	 * 
	 * @return List of all Cities available in table "city" as City-Object
	 */
	public static List<City> selectAllCities() {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c", City.class);

		/* Get all City-Entities from DB */
		List<City> cityFromDb = tQry.getResultList();

		em.close();

		for (City c : cityFromDb) {
			Log.info(c.toString());
		}

		return cityFromDb;
	}

	/**
	 * This Query returns a single City based on the Name of a City specified as 
	 * @param name - Cityname
	 * @return a single City-Entity matching with the where-condition
	 */
	public static City selectCity(String name) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c where c.name = :name", City.class);
		tQry.setParameter("name", name);

		/* Get the single City-Entity from DB (matched by WHERE-Clause */
		City city = tQry.getSingleResult();

		em.close();

		Log.info(city.toString());

		return city;
	}

}
