package ch.hslu.swde.wda.persister;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbHelperMain {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);
	public static void main(String[] args) {

		String start ="2020-12-30" ;
		String end ="2021-11-28" ;
		
		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		DbHelper DbHelper = new DbHelper();
		DbHelper.selectAllCities();
	//	DbHelper.selectWeatherDataSingleCity("Langenthal",startDate ,endDate );
		
	//	DbHelper.selectAverageWeatherDataSingleCity("Langenthal",startDate ,endDate );
	//	DbHelper.selectMinWeatherDataSingleCity("Langenthal",startDate ,endDate );
	//	DbHelper.selectMaxWeatherDataAllCity(Timestamp.valueOf("2021-01-01 00:00:57"));
//	DbHelper.selectMinWeatherDataAllCity(Timestamp.valueOf("2021-01-01 00:00:57"));

	}

}
