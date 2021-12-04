package ch.hslu.swde.wda.persister;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;

public class PersistWeatherData {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);

	
	public static void insertWeatherData(List<WeatherData> weatherData) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();

		for (WeatherData wd : weatherData) {

			if (em.find(City.class, wd.getCity().getZIPCode()) == null) {
				Log.info("Tried to insert WeatherData without City being persisted in DB, handing over to PersistCity");
				PersistCity.insertSingleCity(wd.getCity());
			}
			else {	
				Log.info("Retrieving City from DB to ensure correct relation mapping");
				wd.setCity(em.find(City.class, wd.getCity().getZIPCode()));
			}
			em.persist(wd);
			Log.info("The following WeatherData has been persisted: " + wd.toString() );

		}

		em.getTransaction().commit();
		em.close();

	}

}
