package ch.hslu.swde.wda.persister;

import java.sql.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbHelperTest {

	private static final Logger Log = LogManager.getLogger(DbHelper.class);
	public static void main(String[] args) {

		String start ="2020-12-30" ;
		String end ="2021-11-28" ;
		
		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

//		DbHelper.selectAllCities();

//		DbHelper.selectWeatherDataSingleCity("Langenthal",startDate ,endDate );
		
//		DbHelper.selectAverageWeatherDataSingleCity("Langenthal",startDate ,endDate );

	}

}
