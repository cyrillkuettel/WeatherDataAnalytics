package ch.hslu.swde.wda.persister;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DbHelper {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);

	// Change this value by using the respective selectDB method
	private String DBCONNECTION = "PROD";

	/**
	 * This Query returns all Cities, for which weather data is available - Query
	 * A01
	 * 
	 * @return List of all Cities available in table "city" as City-Object
	 */
	public List<City> selectAllCities() {

		Log.info("Starting selectAllCities");

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c Order by c.name asc", City.class);

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
	 * This Query returns a single City based on the Name specified
	 * 
	 * @param cityname - Cityname which should be inserted into where-condition
	 * @return a single City-Entity matching with the where-condition
	 */
	public City selectSingleCity(String cityname) {

		Log.info("Starting selectSingleCity with Parameter: " + cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

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
	 * This Query returns a List of all Weatherdata for a given City and given
	 * timerange (specified by start- and enddate) - Query A02
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param startDate - The startdate for the Timerange
	 * @param endDate   - The enddate for the Timerange
	 * @return List of all Weatherdata matching with the parameters given
	 */

	public List<WeatherData> selectWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		Log.info("Starting selectWeatherDataSingleCity with Parameters [Cityname: " + cityname + "]" + ", [Startdate: "
				+ startDate + "]" + ", [Enddate: " + endDate + "]");

		City city = selectSingleCity(cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<WeatherData> tQry = em.createQuery(
				"SELECT w FROM WeatherData w where w.city = :city and w.datatimestamp between :startdate and :enddate",
				WeatherData.class);
		tQry.setParameter("city", city);
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

	/**
	 * This Query returns a Weatherdata Object with the average
	 * temperature,pressure,humidity for a given City and given timerange (specified
	 * by start- and enddate) - Query A03
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param startDate - The startdate for the Timerange
	 * @param endDate   - The enddate for the Timerange
	 * @return Weatherdata Object matching with the parameters given
	 */

	public WeatherData selectAverageWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		Log.info("Starting selectAverageWeatherDataSingleCity with Parameters [Cityname: " + cityname + "]"
				+ ", [Startdate: " + startDate + "]" + ", [Enddate: " + endDate + "]");

		City city = selectSingleCity(cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<Object[]> tQry = em.createQuery(
				"SELECT AVG(w.temp), AVG(w.pressure), AVG(w.humidity) FROM WeatherData w where w.city = :city and w.datatimestamp between :startdate and :enddate",
				Object[].class);

		tQry.setParameter("city", city);
		tQry.setParameter("startdate", startDate);
		tQry.setParameter("enddate", endDate);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		Object[] result = tQry.getSingleResult();

		em.close();

		WeatherData wd = new WeatherData(city, null, (Double) result[0], (Double) result[1], (Double) result[2]);

		Log.info("This is the WeatherData found, shown with their toString() method:");
		Log.info(wd.toString());

		return wd;
	}

	/**
	 * This Query returns a Weatherdata Object with the max
	 * temperature,pressure,humidity for a given City and given timerange (specified
	 * by start- and enddate) - Query A04
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param startDate - The startdate for the Timerange
	 * @param endDate   - The enddate for the Timerange
	 * @return Weatherdata Object matching with the parameters given
	 */

	public WeatherData selectMaxWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		Log.info("Starting selectMaxWeatherDataSingleCity with Parameters [Cityname: " + cityname + "]"
				+ ", [Startdate: " + startDate + "]" + ", [Enddate: " + endDate + "]");

		City city = selectSingleCity(cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<Object[]> tQry = em.createQuery(
				"SELECT Max(w.temp), Max(w.pressure), Max(w.humidity) FROM WeatherData w where w.city = :city and w.datatimestamp between :startdate and :enddate",
				Object[].class);

		tQry.setParameter("city", city);
		tQry.setParameter("startdate", startDate);
		tQry.setParameter("enddate", endDate);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		Object[] result = tQry.getSingleResult();

		em.close();

		WeatherData wd = new WeatherData(city, null, (Double) result[0], (Double) result[1], (Double) result[2]);

		Log.info("These are the WeatherData found, shown with their toString() method:");
		Log.info(wd.toString());

		return wd;
	}

	/**
	 * This Query returns a Weatherdata Object with the min
	 * temperature,pressure,humidity for a given City and given timerange (specified
	 * by start- and enddate) - Query A04
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param startDate - The startdate for the Timerange
	 * @param endDate   - The enddate for the Timerange
	 * @return Weatherdata Object matching with the parameters given
	 */
	public WeatherData selectMinWeatherDataSingleCity(String cityname, Date startDate, Date endDate) {

		Log.info("Starting selectMinWeatherDataSingleCity with Parameters [Cityname: " + cityname + "]"
				+ ", [Startdate: " + startDate + "]" + ", [Enddate: " + endDate + "]");

		City city = selectSingleCity(cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<Object[]> tQry = em.createQuery(
				"SELECT Min(w.temp), Min(w.pressure), Min(w.humidity) FROM WeatherData w where w.city = :city and w.datatimestamp between :startdate and :enddate",
				Object[].class);

		tQry.setParameter("city", city);
		tQry.setParameter("startdate", startDate);
		tQry.setParameter("enddate", endDate);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		Object[] result = tQry.getSingleResult();

		em.close();

		WeatherData wd = new WeatherData(city, null, (Double) result[0], (Double) result[1], (Double) result[2]);

		Log.info("These are the WeatherData found, shown with their toString() method:");
		Log.info(wd.toString());

		return wd;
	}

	/**
	 * This Query returns a Weatherdata Object with the max
	 * temperature,pressure,humidity for a all Cities and given timestamp (specified
	 * by timestamp) - Query A05
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param timestamp - The timestamp for which the Weatherdata should be fetched
	 * @return Weatherdata Object matching with the parameters given
	 */
	public WeatherData selectMaxWeatherDataAllCity(Timestamp timestamp) {

		Log.info("Starting selectMaxWeatherDataAllCity with Parameters [Timestamp: " + timestamp + "]");

		// City city = selectSingleCity(cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<Object[]> tQry = em.createQuery(
				"SELECT MAX(w.temp), MAX(w.pressure), MAX(w.humidity) FROM WeatherData w where w.datatimestamp = :timestamp",
				Object[].class);

		tQry.setParameter("timestamp", timestamp);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		Object[] result = tQry.getSingleResult();

		em.close();

		WeatherData wd = new WeatherData(null, null, (Double) result[0], (Double) result[1], (Double) result[2]);

		Log.info("These are the WeatherData found, shown with their toString() method:");
		Log.info(wd.toString());

		return wd;
	}

	/**
	 * This Query returns a Weatherdata Object with the min
	 * temperature,pressure,humidity for a all Cities and given timestamp (specified
	 * by timestamp) - Query A05
	 * 
	 * @param cityname  - City for which the Weatherdata should be fetched
	 * @param timestamp - The timestamp for which the Weatherdata should be fetched
	 * @return Weatherdata Object matching with the parameters given
	 */
	public WeatherData selectMinWeatherDataAllCity(Timestamp timestamp) {

		Log.info("Starting selectMinWeatherDataAllCity with Parameters [Timestamp: " + timestamp + "]");

		// City city = selectSingleCity(cityname);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<Object[]> tQry = em.createQuery(
				"SELECT MIN(w.temp), MIN(w.pressure), MIN(w.humidity) FROM WeatherData w where w.datatimestamp = :timestamp",
				Object[].class);

		tQry.setParameter("timestamp", timestamp);

		/* Get the single City-Entity from DB (matched by WHERE-condition) */
		Object[] result = tQry.getSingleResult();

		em.close();

		WeatherData wd = new WeatherData(null, null, (Double) result[0], (Double) result[1], (Double) result[2]);

		Log.info("These are the WeatherData found, shown with their toString() method:");
		Log.info(wd.toString());

		return wd;
	}
	
	
	public User selectSingleUserData(String username) {
		
		Log.info("Starting selectSingleUserData with Parameters [" + username + "]"  );
		
		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
		em.getTransaction().begin();
		TypedQuery<User> tQry = em.createQuery("SELECT u FROM User u where u.userid = :name", User.class);
		tQry.setParameter("name", username);
	
		User user = tQry.getSingleResult();
		Log.info("User found is: " + user.toString());
		return user;
	}
	
	
	public List<User> selectAllUserData() {
		
		Log.info("Starting selectAllUserData with Parameters []");
		
		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
		em.getTransaction().begin();
		TypedQuery<User> tQry = em.createQuery("SELECT u FROM User u Order by u.firstname asc", User.class);
	
		List<User> usersFromDb = tQry.getResultList();
		
		Log.info("Number of Users found: " + usersFromDb.size());
		Log.info("These are the Users found, shown with their toString() method:");
		for (User u : usersFromDb) {
			Log.info(u.toString());
		}
		return usersFromDb;
	}
	

	

	public void selectTestDB() {

		DBCONNECTION = "TEST";
	}

	public void selectProdDB() {

		DBCONNECTION = "PROD";
	}

}
