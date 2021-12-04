package ch.hslu.swde.wda.persister;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;

public class PersistCity {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);

	public static void insertSingleCity(City city) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();

		em.persist(city);
		Log.info("City persisted in DB, going back to PersistWeatherData");

		em.getTransaction().commit();
		em.close();

	}

	public static void insertCities(List<City> cities) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();

		for (City c : cities) {

			if (em.find(City.class, c.getZIPCode()) == null) {
				Log.info("City not yet in Database, persisting into DB now");
				em.persist(c);
			} else {
				Log.info("City already in DB, no need to persist");
			}

		}

		em.getTransaction().commit();
		em.close();

	}

}
