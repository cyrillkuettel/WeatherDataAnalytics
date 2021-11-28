package ch.hslu.swde.wda.persister;

import java.sql.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
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
	 * @param cityname - Cityname which should be inserted into where-condition
	 * @return a single City-Entity matching with the where-condition
	 */
	public static City selectSingleCity(String cityname) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c where c.name = :name", City.class);
		tQry.setParameter("name", cityname);

		/* Get the single City-Entity from DB (matched by WHERE-condition */
		City city = tQry.getSingleResult();

		em.close();

		Log.info(city.toString());

		return city;
	}
	
	/**
	 * This Query returns a List off all Weatherdata for a given City and given timerange (specified by start- and enddate)
	 * @param cityname - City for which the Weatherdata should be fetched
	 * @param startDate - The startdate for the Timerange
	 * @param endDate - The enddate for the Timerange
	 * @return List of all Weatherdata matching with the parameters given
	 */
	
	public static List<WeatherData> selectWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<WeatherData> tQry = em.createQuery("SELECT w FROM WeatherData w, City c where c.name = :name and w.timestamp between :startdate and :enddate", WeatherData.class);
		tQry.setParameter("name", cityname);
		tQry.setParameter("startdate", startDate);
		tQry.setParameter("enddate", endDate);

		/* Get the single City-Entity from DB (matched by WHERE-condition */
		List<WeatherData> weatherDataList = tQry.getResultList();

		em.close();

		for (WeatherData w : weatherDataList) {
			Log.info(w.toString());
		}


		return weatherDataList;
	}
	

}
