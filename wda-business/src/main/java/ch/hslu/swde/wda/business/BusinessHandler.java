package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

public interface BusinessHandler extends Remote {
	
	String RO_NAME = "BUSINESS_HANDLER_RO";
	
	public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end) throws RemoteException;
		
	public List<String> getCityNamesAsList() throws RemoteException;
	
	public String selectAverageWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;
	
	public String selectMaxWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;

	public String selectMinWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;

	public String selectMaxWeatherDataAllCity(Timestamp inputTimeStamp) throws RemoteException;

	public String selectMinWeatherDataAllCity(Timestamp inputTimeStamp) throws RemoteException;

	public List<User> getUserNamesAsList() throws RemoteException;
	
	public boolean insertUser(User user) throws RemoteException;
	
	public boolean updateUser(User user) throws RemoteException;
	
	public boolean deleteUser(User user) throws RemoteException;

}

