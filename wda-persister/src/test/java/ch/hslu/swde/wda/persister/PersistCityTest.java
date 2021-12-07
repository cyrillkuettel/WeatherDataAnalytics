package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class PersistCityTest {

	private static EntityManager em;
	private final static String DBCONNECTION = "TEST";
	private static PersistCity pc;

	@BeforeAll
	static void setup() {
		pc = new PersistCity();
		// Set the Persister to Test DB
		pc.selectTestDB();
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
	void testInsertCities() {

		City bern = new City(3000, "Bern");
		City zurich = new City(8000, "Zurich");
		City basel = new City(4000, "Basel");

		List<City> cities = new ArrayList<City>();
		cities.add(bern);
		cities.add(basel);
		cities.add(zurich);

		// Run the insert
		pc.insertCities(cities);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c", City.class);

		/* Get all City-Entities from DB */
		List<City> citiesFromDb = tQry.getResultList();

		em.close();

		assertEquals(3, citiesFromDb.size());
	}

	@Test
	void testInsertSingleCity() {

		City bern = new City(3000, "Bern");


		// Run the insert
		pc.insertSingleCity(bern);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c", City.class);

		/* Get all City-Entities from DB */
		List<City> citiesFromDb = tQry.getResultList();

		em.close();

		assertEquals(1, citiesFromDb.size());
	}

	@AfterAll
	static void resetDB() {
		// Set the Persister to Prod DB
		pc.selectProdDB();
	}
}
