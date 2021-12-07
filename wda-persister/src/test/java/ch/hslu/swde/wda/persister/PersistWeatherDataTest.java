package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class PersistWeatherDataTest {

	private static EntityManager em;

	@BeforeEach
	void dbClean() {

		em = JpaUtilTestDb.createEntityManager();

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

		//Set both Persisterclasses to TEST DB
		PersistWeatherData.JPAUTIL= "TEST";
		PersistCity.JPAUTIL= "TEST";
		
		//Run the insert
		PersistWeatherData.insertWeatherData(weatherDataList);
		
		//Reset both Persisterclasses to Prod DB
		PersistWeatherData.JPAUTIL= "PRODUCTION";
		PersistCity.JPAUTIL= "PRODUCTION";

		em = JpaUtilTestDb.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<WeatherData> tQry = em.createQuery("SELECT w FROM WeatherData w", WeatherData.class);

		List<WeatherData> weatherDataFromDb = tQry.getResultList();

		em.close();
		assertEquals(1, weatherDataFromDb.size());
	}

}
