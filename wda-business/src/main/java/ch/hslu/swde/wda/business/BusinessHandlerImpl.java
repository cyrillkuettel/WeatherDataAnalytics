package ch.hslu.swde.wda.business;

import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;

public class BusinessHandlerImpl implements BusinessHandler {

    private static final Logger Log = LogManager.getLogger(BusinessHandlerImpl.class);
    private DbHelper DbHelper;

	public BusinessHandlerImpl() {
		DbHelper = new DbHelper();
	}
    
    
	@Override
	public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end) {
		
		List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity(cityname, Date.valueOf(start), Date.valueOf(end));
        Log.info(requestedWeatherData);
        return requestedWeatherData;
	}

	@Override
	public List<String> getCityNamesAsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] selectAverageWeatherDataSingleCity(String cityname, String start, String end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> selectMaxMinCity(String cityname, String start, String end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> selectMaxWeatherDataAllCity(String inputTimeStamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUserNamesAsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

}
