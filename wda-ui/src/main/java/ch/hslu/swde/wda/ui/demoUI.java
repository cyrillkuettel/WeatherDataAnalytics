package ch.hslu.swde.wda.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;

public class demoUI {

	private static final Logger Log = LogManager.getLogger(demoUI.class);

	public static void main(String[] args) throws RemoteException {
		UI ui = new UI();
		ui.startFromBeginning();

/*
		BusinessHandlerImpl b = new BusinessHandlerImpl();

		List<WeatherData> w = b.selectWeatherByDateAndCity("Zurich", "2021-10-01", "2021-12-31");

		Log.info("Preview of Weatherdata, all Weatherdata in Weatherdata.csv in your Downloads folder");
		for (WeatherData we : w)
		Log.info(we.toString());


		List<User> users = b.getUserNamesAsList();
		
		User getuser = users.get(2);
		
		getuser.setFirstname("Maximilian");
		getuser.setLastname("Muster");
		getuser.setUserpwd("test1234");
		
		b.updateUser(getuser);

 */


	}
}