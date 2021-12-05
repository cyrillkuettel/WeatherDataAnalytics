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
	 * This Query returns all Cities, for which weather data is available 
	 * - Query A01
	 * 
	 * @return List of all Cities available in table "city" as City-Object
	 */
	public static List<City> selectAllCities() {

		Log.info("Starting selectAllCities");

		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c", City.class);

		/* Get all City-Entities from DB */
		List<City> citiesFromDb = tQry.getResultList();

		em.close();

		Log.info("Number of Cities found: " + citiesFromDb.size());
		Log.info("These are the Cities found, shown with their toString() method:");
		for (City c : citiesFromDb) {
			Log.info(c.toString());
		}
		
		return citiesFromDb;
	}

	/**
	 * This Query returns a single City based on the Name of a City 
	 * 
	 * @param cityname - Cityname which should be inserted into where-condition
	 * @return a single City-Entity matching with the where-condition
	 */
	public static City selectSingleCity(String cityname) {

		Log.info("Starting selectSingleCity with Parameter: " + cityname);
		
		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c where c.name = :name", City.class);
		tQry.setParameter("name", cityname);

		/* Get the single City-Entity from DB (matched by WHERE-condition */
		City city = tQry.getSingleResult();

		em.close();

		Log.info("City found is: " + city.toString());

		return city;
		
	}

	/**
	 * This Query returns a List off all Weatherdata for a given City and given
	 * timerange (specified by start- and enddate) - Query A02, A03, A04
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param startDate - The startdate for the Timerange
	 * @param endDate   - The enddate for the Timerange
	 * @return List of all Weatherdata matching with the parameters given
	 */

	public static List<WeatherData> selectWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		Log.info("Starting selectWeatherDataSingleCity with Parameters [Cityname: " + cityname + "]" + ", [Startdate: " + startDate + "]" + ", [Enddate: " + endDate + "]"  );

		
		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<WeatherData> tQry = em.createQuery(
				"SELECT w FROM WeatherData w, City c where c.name = :name and w.datatimestamp between :startdate and :enddate",
				WeatherData.class);
		tQry.setParameter("name", cityname);
		tQry.setParameter("startdate", startDate);
		tQry.setParameter("enddate", endDate);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		List<WeatherData> weatherDataList = tQry.getResultList();

		em.close();

		Log.info("Number of Weatherdata found: " + weatherDataList.size());
		Log.info("These are the WeatherData found, shown with their toString() method:");
		for (WeatherData w : weatherDataList) {
			Log.info(w.toString());
		}

		return weatherDataList;
	}

	public static List<WeatherData> selectAverageWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		Log.info("Starting selectWeatherDataSingleCity with Parameters [Cityname: " + cityname + "]" + ", [Startdate: " + startDate + "]" + ", [Enddate: " + endDate + "]"  );

		
		EntityManager em = JpaUtil.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<WeatherData> tQry = em.createQuery(
				"SELECT w.weatherdataid, w.city, w.datatimestamp, w.temp, w.pressure, w.humidity, w.windspeed, w.winddirection, w.summary, w.description FROM WeatherData w, City c where c.name = :name and w.datatimestamp between :startdate and :enddate",
				WeatherData.class);
		tQry.setParameter("name", cityname);
		tQry.setParameter("startdate", startDate);
		tQry.setParameter("enddate", endDate);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		List<WeatherData> weatherDataList = tQry.getResultList();

		em.close();

		Log.info("Number of Weatherdata found: " + weatherDataList.size());
		Log.info("These are the WeatherData found, shown with their toString() method:");
		for (WeatherData w : weatherDataList) {
			Log.info(w.toString());
		}

		return weatherDataList;
	}
	
	
}
