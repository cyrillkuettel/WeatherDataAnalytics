package ch.hslu.swde.wda.persister;

import ch.hslu.swde.wda.domain.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistUser {

	// Change this value by using the respective selectDB method
	private String DBCONNECTION = "PROD";
	private static final Logger Log = LogManager.getLogger(DbHelper.class);


	/**
	 * 
	 * This query persists/inserts a user in the db according to the User Object 
	 * given. Before a user with the same name and lastname it is checked with
	 * checkExistingUser method if the userid already exists or not. If yes the
	 * userid gets changed for the new user.
	 * 
	 * @param user - User object which is used for the persist
	 * @return User Object matching with the parameters given
	 */
	public void insertUser(User user) throws EntityExistsException {
		
		Log.info("Starting insertUser");

		EntityManager em = JpaUtil.createEntityManager(DBCONNECTION);

		em.getTransaction().begin();

		try {
			user = checkExistingUser(user);
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

	/**
	 * 
	 * This query updates a user. The method checks if the firstname and lastname
	 * are changed or if the password is changed. Depending on the change 
	 * firstname/lastname changed -> user gets deleted and is inserted again
	 * password changed -> password gets set
	 * 
	 * 
	 * 
	 * 
	 * @param user - User object which is used for the update
	 * @return User Object matching with the parameters given
	 */
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
			

			insertUser(user);

		} else {
			Log.info("Only password has been changed");
			dbuser.setUserpwd(user.getUserpwd());

		}

		em.getTransaction().commit();
		em.close();

	}

	/**
	 * 
	 * This method checks if the user which gets inserted is already in the db with the name constellation or not
	 * if yes the setUseridWithIterator is called and the userid which is created gets an incremental addition
	 * 
	 * 
	 * @param user - user - User object which is searched in the db
	 * @return User Object matching with the parameters given
	 */
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

	/**
	 * 
	 * This query deletes a user in the db.
	 * With the find functionality the wanted user is found 
	 * and with the remove functionality deleted
	 * 
	 * @param user - User object which is deleted in the db
	 * 
	 */
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
