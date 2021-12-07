package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;

public class DbHelperTest {

	private static EntityManager em;

	@BeforeAll
	static void dbSetup() {

		dbClean();

		City bern = new City(3000, "Bern");
		City zurich = new City(8000, "Zurich");
		City basel = new City(4000, "Basel");

		WeatherData weatherData1 = new WeatherData(bern, Timestamp.valueOf("2021-01-01 12:00:00"), 0.0, 0, 0);
		WeatherData weatherData2 = new WeatherData(bern, Timestamp.valueOf("2021-01-10 00:00:00"), 50.0, 100, 60);

		WeatherData weatherData3 = new WeatherData(zurich, Timestamp.valueOf("2021-01-01 12:00:00"), 0, 0, 0);
		WeatherData weatherData4 = new WeatherData(zurich, Timestamp.valueOf("2021-01-10 00:00:00"), 50.0, 100, 60);

		WeatherData weatherData5 = new WeatherData(basel, Timestamp.valueOf("2021-01-01 12:00:00"), 0, 0, 0);
		WeatherData weatherData6 = new WeatherData(basel, Timestamp.valueOf("2021-01-10 00:00:00"), 50.0, 100, 60);

		List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
		weatherDataList.add(weatherData1);
		weatherDataList.add(weatherData2);
		weatherDataList.add(weatherData3);
		weatherDataList.add(weatherData4);
		weatherDataList.add(weatherData5);
		weatherDataList.add(weatherData6);

		// Set both Persisterclasses to TEST DB
		PersistWeatherData.JPAUTIL = "TEST";
		PersistCity.JPAUTIL = "TEST";

		// Run the insert
		PersistWeatherData.insertWeatherData(weatherDataList);

		// Reset both Persisterclasses to Prod DB
		PersistWeatherData.JPAUTIL = "PRODUCTION";
		PersistCity.JPAUTIL = "PRODUCTION";

	}

	static void dbClean() {

		em = JpaUtilTestDb.createEntityManager();

		em.getTransaction().begin();
		em.createQuery("DELETE FROM WeatherData w").executeUpdate();
		em.createQuery("DELETE FROM City c").executeUpdate();
		em.getTransaction().commit();
		em.close();

	}

	@Test
	void testSelectAllCities() {
		DbHelper.JPAUTIL = "TEST";

		// Run the select
		List<City> cities = DbHelper.selectAllCities();

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals(3, cities.size());
	}

	@Test
	void testSelectSingleCity() {

		DbHelper.JPAUTIL = "TEST";

		// Run the select
		City city = DbHelper.selectSingleCity("Basel");

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals("Basel", city.getName());

	}

	@Test
	void testSelectWeatherDataSingleCity() {

		DbHelper.JPAUTIL = "TEST";

		String start = "2020-01-01";
		String end = "2021-01-02";

		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		// Run the select
		List<WeatherData> weatherData = DbHelper.selectWeatherDataSingleCity("Basel", startDate, endDate);

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals(1, weatherData.size());
	}

	@Test
	void testselectAverageWeatherDataSingleCity() {

		DbHelper.JPAUTIL = "TEST";

		// Run the select
		City city = DbHelper.selectSingleCity("Basel");

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals("Basel", city.getName());

	}

	@Test
	void testSelectMaxWeatherDataSingleCity() {

		DbHelper.JPAUTIL = "TEST";

		// Run the select
		City city = DbHelper.selectSingleCity("Basel");

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals("Basel", city.getName());

	}

	@Test
	void testSelectMinWeatherDataSingleCity() {

		DbHelper.JPAUTIL = "TEST";

		// Run the select
		City city = DbHelper.selectSingleCity("Basel");

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals("Basel", city.getName());

	}

	@Test
	void testselectSingleCity() {

		DbHelper.JPAUTIL = "TEST";

		// Run the select
		City city = DbHelper.selectSingleCity("Basel");

		// Reset Persisterclass to Prod DB
		DbHelper.JPAUTIL = "PRODUCTION";

		assertEquals("Basel", city.getName());

	}

}
