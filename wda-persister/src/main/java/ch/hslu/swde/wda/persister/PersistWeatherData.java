package ch.hslu.swde.wda.persister;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PersistWeatherData {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);

	// Change this value by using the respective selectDB method
	private String DBCONNECTION = "PROD";
	private PersistCity persistCity;

	public PersistWeatherData() {
		persistCity = new PersistCity();

	}

	/**
	 * This Method persists a single WeatherData entity. For each WeatherData object
	 * there is a check to see whether the City object related to it has already
	 * been persisted into the DB. If the City isn't persisted yet, then
	 * PersistCity.insertSingleCity() is called to persist the City object. If the
	 * City was already persisted or has been persisted as mentioned before, the
	 * City object is being retrieved from the DB to ensure the correct relation
	 * mapping. As a last step, the WeatherData object is being persisted into the
	 * DB. This method can be safely accessed from any class as the needed DB checks
	 * are implemented.
	 * 
	 * @param wd The WeatherData object to be inserted.
	 */
	public void insertSingleWeatherData(WeatherData wd) {

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		// Setting DBCONNECTION Constant of persistCity to the same value as the
		// DBCONNECTION in this class
		setPersistCityDB();

		em.getTransaction().begin();

		if (em.find(City.class, wd.getCity().getZIPCode()) == null) {
			Log.info("Tried to insert WeatherData without City being persisted in DB, handing over to PersistCity");
			persistCity.insertSingleCity(wd.getCity());
		} else {
			Log.info("Retrieving City from DB to ensure correct relation mapping");
			wd.setCity(em.find(City.class, wd.getCity().getZIPCode()));
		}
		em.persist(wd);
		Log.info("The following WeatherData has been persisted: " + wd.toString());

		em.getTransaction().commit();
		em.close();

	}

	/**
	 * This Method persists a List of WeatherData entities. For each WeatherData
	 * object there is a check to see whether the City object related to it has
	 * already been persisted into the DB. If the City isn't persisted yet, then
	 * PersistCity.insertSingleCity() is called to persist the City object. If the
	 * City was already persisted or has been persisted as mentioned before, the
	 * City object is being retrieved from the DB to ensure the correct relation
	 * mapping. As a last step, the WeatherData object is being persisted into the
	 * DB. This method can be safely accessed from any class as the needed DB checks
	 * are implemented.
	 * 
	 * @param weatherData The list of city objects which should be persisted
	 */
	public void insertWeatherData(List<WeatherData> weatherData) {

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		// Setting DBCONNECTION Constant of persistCity to the same value as the
		// DBCONNECTION in this class
		setPersistCityDB();

		em.getTransaction().begin();

		for (WeatherData wd : weatherData) {

			if (em.find(City.class, wd.getCity().getZIPCode()) == null) {
				Log.info("Tried to insert WeatherData without City being persisted in DB, handing over to PersistCity");
				persistCity.insertSingleCity(wd.getCity());
			} else {
				Log.info("Retrieving City from DB to ensure correct relation mapping");
				wd.setCity(em.find(City.class, wd.getCity().getZIPCode()));
			}
			em.persist(wd);
			Log.info("The following WeatherData has been persisted: " + wd.toString());

		}

		em.getTransaction().commit();
		em.close();

	}

	public void selectTestDB() {

		DBCONNECTION = "TEST";
	}

	public void selectProdDB() {

		DBCONNECTION = "PROD";
	}

	public PersistCity getPersistCity() {
		return persistCity;
	}

	private void setPersistCityDB() {
		if (this.DBCONNECTION.equals("PROD")) {
			persistCity.selectProdDB();
		} else {
			persistCity.selectTestDB();
		}
	}

}
