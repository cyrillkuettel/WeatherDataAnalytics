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
	private String DBCONNECTION = "PROD";
	private static final Logger Log = LogManager.getLogger(DbHelper.class);

	/**
	 * 
	 * @param
	 */
	public void insertUser(User user) throws EntityExistsException {
		
		Log.info("Starting insertUser");

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();

		try {
			em.persist(user);
			Log.info(user.toString() + " persisted in DB");

			em.getTransaction().commit();
			em.close();
		} catch (EntityExistsException e) {
			Log.warn("This User already exists in DB!");
			e.printStackTrace();
			throw (e);
		}

	}

	public void updateUser(User user) throws EntityExistsException {

		Log.info("Starting updateUser");

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
		em.getTransaction().begin();

		User dbuser = em.find(User.class, user.getUserid());

		if (!(dbuser.getFirstname().equals(user.getFirstname()))
				|| !(dbuser.getLastname().equals(user.getLastname()))) {

			Log.info(
					"Firstname or Lastname have changed, this means userid will change as well. As we can't update primary key, the user will be deleted and reinserted with his new values");

			deleteUser(dbuser);

			user.setUserid(user.getFirstname(), user.getLastname());
			
			//Checking if new userid is already taken
			user = checkExistingUser(user);
			insertUser(user);

		} else {
			Log.info("Only password has been changed");
			dbuser.setUserpwd(user.getUserpwd());

		}

		em.getTransaction().commit();
		em.close();

	}

	public User checkExistingUser(User user) {
		
		Log.info("Starting checkExistingUser ");

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
		int i = 1;
		em.getTransaction().begin();

		//Adding an incremental number at the end of the username if username exists already in DB
		while (em.find(User.class, user.getUserid()) != null) {

			user.setUseridWithIterator(user.getFirstname(), user.getLastname(),i);
			i++;

		}
		Log.info("Userid " +user.getUserid() + " was the next available Userid");

		em.getTransaction().commit();
		em.close();

		return user;
	}

	public void deleteUser(User user) {

		Log.info("Starting deleteUser");

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);
		em.getTransaction().begin();

		user = em.find(User.class, user.getUserid());
		em.remove(user);

		Log.info(user.toString() + " has been removed from DB");
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
