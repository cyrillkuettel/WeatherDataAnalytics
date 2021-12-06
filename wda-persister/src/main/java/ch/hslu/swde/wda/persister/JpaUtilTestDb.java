package ch.hslu.swde.wda.persister;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JpaUtilTestDb {
	
	private JpaUtilTestDb() {
		
	}

	private static final String TESTPERSISTENCEUNIT = "DB_TEST";
	private static Logger logger = LogManager.getLogger(JpaUtilTestDb.class);

	private static EntityManagerFactory entityManagerFactory = null;

	static {
		try {
			/* EntityManagerFactory erzeugen */
			entityManagerFactory = Persistence.createEntityManagerFactory(TESTPERSISTENCEUNIT);
		} catch (Exception e) {
			logger.error("ERROR: ", e);
			throw new RuntimeException(e);
		}
	}

	public static EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
	
}