package ch.hslu.swde.wda.ui;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.business.BusinessHandlerImpl;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;

public class demoUI {

	private static final Logger Log = LogManager.getLogger(demoUI.class);

	public static void main(String[] args) throws RemoteException {
//		UI ui = new UI();
//		 List<User> userList = ui.selectAllUserData();
//
//      ui.startFromBeginning();

		BusinessHandlerImpl b = new BusinessHandlerImpl();

//		List<WeatherData> w = b.selectWeatherByDateAndCity("Zurich", "2021-10-01", "2021-12-31");
//		
//		Log.info("Preview of Weatherdata, all Weatherdata in Weatherdata.csv in your Downloads folder");
//		for (WeatherData we : w)
//		Log.info(we.toString());
//		

		List<User> users = b.getUserNamesAsList();
		
		User getuser = users.get(4);
		
		getuser.setLastname("Test");
		
		b.updateUser(getuser);
	}
}