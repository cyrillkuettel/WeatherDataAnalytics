package ch.hslu.swde.wda.persister;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PersistUserTest {
	
	private static EntityManager em;
	private final static String DBCONNECTION = "TEST";
	private static PersistUser pu;

	@BeforeAll
	static void setup() {
		pu = new PersistUser();
		// Set the Persister to Test DB
		pu.selectTestDB();
	}

	@BeforeEach
	void dbClean() {

		em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		em.createQuery("DELETE FROM User u").executeUpdate();
		em.getTransaction().commit();
		em.close();

	}

	 
	
	@Test
	void testInsertUser() {

		User user = new User("Nilukzil", "Fernando", "test");

		// Run the insert
		pu.insertUser(user);

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();
		TypedQuery<User> tQry = em.createQuery("SELECT u FROM User u", User.class);

		/* Get all City-Entities from DB */
		List<User> userFromDb = tQry.getResultList();

		em.close();

		assertEquals(1, userFromDb.size());
	}
	
	
	@AfterAll
	static void resetDB() {
		// Set the Persister to Prod DB
		pu.selectProdDB();
	}
}
