package ch.hslu.swde.wda.persister;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JpaUtilProdDb {
	
	private JpaUtilProdDb() {
		
	}

	private static final String PRODPERSISTENCEUNIT = "DB_PRODUCTION";
	private static Logger logger = LogManager.getLogger(JpaUtilProdDb.class);

	private static EntityManagerFactory entityManagerFactory = null;

	static {
		try {
			/* EntityManagerFactory erzeugen */
			entityManagerFactory = Persistence.createEntityManagerFactory(PRODPERSISTENCEUNIT);
		} catch (Exception e) {
			logger.error("ERROR: ", e);
			throw new RuntimeException(e);
		}
	}

	public static EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
	
}