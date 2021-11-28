package ch.hslu.swde.wda.persister;

import java.sql.Date;

public class DbHelperTest {

	public static void main(String[] args) {

		String start ="2020-12-30" ;
		String end ="2021-11-28" ;
		
		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);

		
		
		DbHelper.selectWeatherDataSingleCity("Langenthal",startDate ,endDate );
		
		
		
	}

}
