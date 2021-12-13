package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;

import java.rmi.Remote;
import java.util.List;

public interface BusinessHandler extends Remote {
	
	
	public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end);
		
	public List<String> getCityNamesAsList();
	
	public String[] selectAverageWeatherDataSingleCity(String cityname, String start, String end);
	
	public List<String> selectMaxMinCity(String cityname, String start, String end);
	
	public List<String> selectMaxWeatherDataAllCity(String inputTimeStamp);
	
	public List<User> getUserNamesAsList();
	
	public boolean insertUser(User user);
	
	public boolean updateUser(User user);
	
	public boolean deleteUser(User user);

}
