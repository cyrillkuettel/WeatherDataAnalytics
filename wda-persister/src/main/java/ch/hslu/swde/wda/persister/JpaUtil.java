package ch.hslu.swde.wda.persister;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JpaUtil {
	
	private JpaUtil() {
		
	}

	private static final String PRODPERSISTENCEUNIT = "DB_PRODUCTION";
	private static final String TESTPERSISTENCEUNIT = "DB_TEST";
	private static Logger Log = LogManager.getLogger(JpaUtil.class);

	private static EntityManagerFactory entityManagerFactory = null;


	public static EntityManager createEntityManager(String db) {
		if (db.equals("PROD")) {
			entityManagerFactory = Persistence.createEntityManagerFactory(PRODPERSISTENCEUNIT);
			Log.info("Queries running on PROD DB");
		} else if (db.equals("TEST")) {
			entityManagerFactory = Persistence.createEntityManagerFactory(TESTPERSISTENCEUNIT);
			Log.info("Queries running on TEST DB");
		} else {
			Log.warn("Check parameter which is based on Class Constant, value is neither PROD nor TEST");
		}
		return entityManagerFactory.createEntityManager();
	}
}