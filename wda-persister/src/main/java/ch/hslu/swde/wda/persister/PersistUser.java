package ch.hslu.swde.wda.persister;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PersistUser {
	
	// Change this value by using the respective selectDB method
		public String DBCONNECTION = "PROD";
		private static final Logger Log = LogManager.getLogger(DbHelper.class);


		/**
		 * 
		 * @param 
		 */
		public void insertUser(User user) throws EntityExistsException {

			EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

			em.getTransaction().begin();

			try {
			em.persist(user);
			Log.info("User persisted in DB");

			em.getTransaction().commit();
			em.close();}
			catch (EntityExistsException e){
				Log.warn("This User already exists in DB!");
				e.printStackTrace();
				throw (e);
			}

		}
		
		
		public void updateUsers(User user) throws EntityExistsException {

			//	Log.info("Starting selectSingleUserData with Parameters [" + username + "]"  );

				EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
				em.getTransaction().begin();
				
				User dbuser = em.find(User.class, user.getUserid());
				
				//check if username is already in DB
				

				
				dbuser.setFirstname(user.getFirstname());
				dbuser.setLastname(user.getLastname());
				dbuser.setUserpwd(user.getUserpwd());
				
				
				dbuser.setUserid(user.getFirstname(), user.getLastname());

				Log.info("User values are: " + dbuser.toString());
				em.getTransaction().commit();
				em.close();
				
		}
	
		public void checkExistingUser(User user) {
			EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
			em.getTransaction().begin();
		
			User dbuser = em.find(User.class, user.getUserid());
			if (dbuser == null) {
				dbuser.setUserid(user.getFirstname(), user.getLastname());
			}else {
				dbuser.setUserid(user.getFirstname(), user.getLastname());
			}
					
			em.getTransaction().commit();
			em.close();
		}

			
		public void deleteUsers(User user) throws EntityExistsException {

			//	Log.info("Starting selectSingleUserData with Parameters [" + username + "]"  );
				
				EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
				em.getTransaction().begin();
				
				User getuser = em.find(User.class, user.getUserid());
				getuser.setLastname("Test");
				Log.info("User values are: " + user.toString());
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
