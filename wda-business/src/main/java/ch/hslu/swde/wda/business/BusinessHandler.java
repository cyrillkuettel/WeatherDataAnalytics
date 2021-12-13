package ch.hslu.swde.wda.business;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;

public interface BusinessHandler extends Remote {
	
	String RO_NAME = "BUSINESS_HANDLER_RO";
	
	public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end) throws RemoteException;
		
	public List<String> getCityNamesAsList() throws RemoteException;
	
	public String selectAverageWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;
	
	public List<String> selectMaxMinCity(String cityname, String start, String end) throws RemoteException;
	
	public List<String> selectMaxWeatherDataAllCity(String inputTimeStamp) throws RemoteException;
	
	public List<User> getUserNamesAsList() throws RemoteException;
	
	public boolean insertUser(User user) throws RemoteException;
	
	public boolean updateUser(User user) throws RemoteException;
	
	public boolean deleteUser(User user) throws RemoteException;

}

