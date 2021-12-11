package ch.hslu.swde.wda.persister;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import jakarta.persistence.EntityManager;

public class PersistUser {
	
	// Change this value by using the respective selectDB method
		public String DBCONNECTION = "PROD";
		private static final Logger Log = LogManager.getLogger(DbHelper.class);


		/**
		 * 
		 * @param city The city object which should be persisted
		 */
		public void insertUser(User user) {

			EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

			em.getTransaction().begin();

			em.persist(user);
			Log.info("User persisted in DB");

			em.getTransaction().commit();
			em.close();

		}
		


		public void selectTestDB() {

			DBCONNECTION = "TEST";
		}

		public void selectProdDB() {

			DBCONNECTION = "PROD";
		}

}
