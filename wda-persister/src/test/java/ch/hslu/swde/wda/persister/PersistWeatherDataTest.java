package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PersistWeatherDataTest {

	private static EntityManager em;
	private final static String DBCONNECTION = "TEST";
	private static PersistWeatherData pd;

	@BeforeAll
	static void setup() {
		pd = new PersistWeatherData();

		// Set both Persisterclasses to TEST DB
		pd.selectTestDB();
		pd.getPersistCity().selectTestDB();

	}

	@BeforeEach
	void dbClean() {

		em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		em.createQuery("DELETE FROM WeatherData w").executeUpdate();
		em.createQuery("DELETE FROM City c").executeUpdate();
		em.getTransaction().commit();
		em.close();

	}

	@Test
	void testInsertWeatherData() {

		City bern = new City(3000, "Bern");
		WeatherData weatherData1 = new WeatherData(bern, Timestamp.valueOf("9999-12-31 00:00:00"), 2.0, 4, 3.5);

		List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
		weatherDataList.add(weatherData1);

		// Run the insert
		pd.insertWeatherData(weatherDataList);

		em = JpaUtil.createEntityManager(DBCONNECTION);
		em.getTransaction().begin();
		TypedQuery<WeatherData> tQry = em.createQuery("SELECT w FROM WeatherData w", WeatherData.class);

		List<WeatherData> weatherDataFromDb = tQry.getResultList();

		em.close();
		assertEquals(1, weatherDataFromDb.size());
	}

	@AfterAll
	static void resetDB() {
		// Reset both Persisterclasses to Prod DB
		pd.selectProdDB();
		pd.getPersistCity().selectProdDB();
	}

}
