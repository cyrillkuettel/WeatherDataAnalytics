package ch.hslu.swde.wda.ui;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import ch.hslu.swde.wda.business.BusinessHandlerImpl;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;

public class demoUI {

	public static void main(String[] args) throws RemoteException {
		UI ui = new UI();
		// List<User> userList = ui.selectAllUserData();

      ui.startFromBeginning();

//		BusinessHandlerImpl b = new BusinessHandlerImpl();
//		DbHelper d = new DbHelper();
//
//		Date startDate = Date.valueOf("2021-10-01");
//		Date endDate = Date.valueOf("2021-12-31");
//
//		List<WeatherData> w = d.selectWeatherDataSingleCity("Zurich", startDate, endDate);
//
//		b.writeCSV(w);

	}
}