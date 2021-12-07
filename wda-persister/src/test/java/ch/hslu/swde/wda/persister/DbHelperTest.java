package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;

public class DbHelperTest {

	private static EntityManager em;
	private final static String DBCONNECTION ="TEST";
	private static DbHelper DbHelper;
	private static PersistWeatherData pd;

	@BeforeAll
	static void dbSetup() {

		dbClean();

		DbHelper = new DbHelper();
		pd = new PersistWeatherData();

		
		
		// Create Test Data
		City bern = new City(3000, "Bern");
		City zurich = new City(8000, "Zurich");
		City basel = new City(4000, "Basel");

		WeatherData weatherData1 = new WeatherData(bern, Timestamp.valueOf("2021-01-01 12:00:00"), 0, 100, 200);
		WeatherData weatherData2 = new WeatherData(bern, Timestamp.valueOf("2021-01-10 00:00:00"), 50, 150, 250);

		WeatherData weatherData3 = new WeatherData(zurich, Timestamp.valueOf("2021-01-01 12:00:00"), 10, 110, 210);
		WeatherData weatherData4 = new WeatherData(zurich, Timestamp.valueOf("2021-01-10 00:00:00"), 70, 170, 270);

		WeatherData weatherData5 = new WeatherData(basel, Timestamp.valueOf("2021-01-01 12:00:00"), 20, 120, 220);
		WeatherData weatherData6 = new WeatherData(basel, Timestamp.valueOf("2021-01-10 00:00:00"), 100, 200, 300);

		List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
		weatherDataList.add(weatherData1);
		weatherDataList.add(weatherData2);
		weatherDataList.add(weatherData3);
		weatherDataList.add(weatherData4);
		weatherDataList.add(weatherData5);
		weatherDataList.add(weatherData6);

		// Set both Persisterclasses to TEST DB
		pd.selectTestDB();
		pd.getPersistCity().selectTestDB();

		// Run the insert
		pd.insertWeatherData(weatherDataList);

		// Reset both Persisterclasses to Prod DB
		pd.selectProdDB();
		pd.getPersistCity().selectProdDB();
		
		//Set DbHelper to Test DB, will be set to Prod DB with @AfterAll
		DbHelper.selectTestDB();

	}

	static void dbClean() {

		em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		em.createQuery("DELETE FROM WeatherData w").executeUpdate();
		em.createQuery("DELETE FROM City c").executeUpdate();
		em.getTransaction().commit();
		em.close();

	}

	@Test
	void testSelectAllCities() {


		// Run the select
		List<City> cities = DbHelper.selectAllCities();

		assertEquals(3, cities.size());
	}

	@Test
	void testSelectSingleCity() {


		// Run the select
		City city = DbHelper.selectSingleCity("Basel");

		assertEquals("Basel", city.getName());

	}

	// Test for Query A02
	@Test
	void testSelectWeatherDataSingleCity() {

		String start = "2020-01-01";
		String end = "2021-01-02";

		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		// Run the select
		List<WeatherData> weatherData = DbHelper.selectWeatherDataSingleCity("Basel", startDate, endDate);

		assertEquals(1, weatherData.size());
	}

	// Test for Query A03
	@Test
	void testselectAverageWeatherDataSingleCity() {

		String start = "2020-01-01";
		String end = "2021-01-10";

		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		// Run the select
		WeatherData weatherData = DbHelper.selectAverageWeatherDataSingleCity("Zurich", startDate, endDate);

		assertEquals("Zurich", weatherData.getCity().getName());
		assertEquals(40, weatherData.getTemp());
		assertEquals(140, weatherData.getPressure());
		assertEquals(240, weatherData.getHumidity());

	}

	// Test for Query A04 - MAX Value
	@Test
	void testSelectMaxWeatherDataSingleCity() {

		String start = "2020-01-01";
		String end = "2021-01-10";

		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		// Run the select
		WeatherData weatherData = DbHelper.selectMaxWeatherDataSingleCity("Bern", startDate, endDate);


		assertEquals("Bern", weatherData.getCity().getName());
		assertEquals(50, weatherData.getTemp());
		assertEquals(150, weatherData.getPressure());
		assertEquals(250, weatherData.getHumidity());

	}

	// Test for Query A04 - MIN Value

	@Test
	void testSelectMinWeatherDataSingleCity() {

		String start = "2020-01-01";
		String end = "2021-01-10";

		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		// Run the select
		WeatherData weatherData = DbHelper.selectMinWeatherDataSingleCity("Basel", startDate, endDate);


		assertEquals("Basel", weatherData.getCity().getName());
		assertEquals(20, weatherData.getTemp());
		assertEquals(120, weatherData.getPressure());
		assertEquals(220, weatherData.getHumidity());

	}

	// Test for Query A05 - MAX Value
	@Test
	void testSelectMaxWeatherDataAllCity() {


		// Run the select
		WeatherData weatherData = DbHelper.selectMaxWeatherDataAllCity(Timestamp.valueOf("2021-01-01 12:00:00"));


		assertEquals(20, weatherData.getTemp());
		assertEquals(120, weatherData.getPressure());
		assertEquals(220, weatherData.getHumidity());

	}

	// Test for Query A05 - MIN Value

	@Test
	void testSelectMinWeatherDataAllCity() {

		// Run the select
		WeatherData weatherData = DbHelper.selectMinWeatherDataAllCity(Timestamp.valueOf("2021-01-01 12:00:00"));

		assertEquals(0, weatherData.getTemp());
		assertEquals(100, weatherData.getPressure());
		assertEquals(200, weatherData.getHumidity());

	}
	
	
	@AfterAll
	static void resetDB() {
		DbHelper.selectProdDB();

	}

}
