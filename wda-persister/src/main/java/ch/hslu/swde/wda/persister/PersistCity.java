package ch.hslu.swde.wda.persister;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;

public class PersistCity {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);
	
	//Set this value to either PRODUCTION or TEST to switch between PROD and TEST DB
	public static String JPAUTIL = "PRODUCTION";
	
	/**
	 * DO NOT USE THIS METHOD WITH ANY OTHER CLASS AS IT IS NOT CHECKING WHETHER THE CITY ALREADY EXISTS IN THE DB (THIS CHECK IS PERFORMED IN ADVANCE IN PERSISTWEATHERDATA)
	 * This Method persists a single City entity and should only be called from PersistWeatherData if the City object related to a WeatherData Object is not yet in the DB.
	 * 
	 * @param city The city object which should be persisted
	 */
	public static void insertSingleCity(City city) {
		
		EntityManager em = null;
		
		if (JPAUTIL == "PRODUCTION") {
			em = JpaUtilProdDb.createEntityManager();
			Log.info("Queries running on PROD DB");
		} else if (JPAUTIL == "TEST") {
			em = JpaUtilTestDb.createEntityManager();
			Log.info("Queries running on TEST DB");
		} else {
			Log.warn("Check definition of Constant JPAUTIL, value is neither PRODUCTION nor TEST");
		}
		

		em.getTransaction().begin();

		em.persist(city);
		Log.info("City persisted in DB, going back to PersistWeatherData");

		em.getTransaction().commit();
		em.close();

	}
	
	/**
	 * This Method persists a List of City entities.
	 * The method checks in the DB whether the City is already persisted to prevent errors due to the City Primary Key (Zip Code) being defined as unique.
	 * This method can be safely accessed from any class as the needed DB checks are implemented.
	 * 
	 * @param city The list of city objects which should be persisted
	 */
	public static void insertCities(List<City> cities) {

		EntityManager em =null;
		if (JPAUTIL == "PRODUCTION") {
			em = JpaUtilProdDb.createEntityManager();
			Log.info("Queries running on PROD DB");
		} else if (JPAUTIL == "TEST") {
			em = JpaUtilTestDb.createEntityManager();
			Log.info("Queries running on TEST DB");
		} else {
			Log.warn("Check definition of Constant JPAUTIL, value is neither PRODUCTION nor TEST");
		}
		
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
