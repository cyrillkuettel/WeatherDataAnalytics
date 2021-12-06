package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.hslu.swde.wda.domain.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class PersistCityTest {

	private EntityManager em;

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
	void testinsertCities() {

		City bern = new City(3000, "Bern");
		City zurich = new City(8000, "Zurich");
		City basel = new City(4000, "Basel");

		List<City> cities = new ArrayList<City>();
		cities.add(bern);
		cities.add(basel);
		cities.add(zurich);

		PersistCity.insertCities(cities);

		EntityManager em = JpaUtilTestDb.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c", City.class);

		/* Get all City-Entities from DB */
		List<City> citiesFromDb = tQry.getResultList();

		em.close();

//		assertEquals(3, citiesFromDb.size());

	}

	@Test
	void testinsertSingleCity() {

		City bern = new City(3000, "Bern");

		PersistCity.insertSingleCity(bern);

		EntityManager em = JpaUtilProdDb.createEntityManager();

		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c", City.class);

		/* Get all City-Entities from DB */
		List<City> citiesFromDb = tQry.getResultList();

		em.close();

//		assertEquals(1, citiesFromDb.size());

	}

}
