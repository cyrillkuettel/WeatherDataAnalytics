package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

public interface BusinessHandler extends Remote {

	String RO_NAME = "BUSINESS_HANDLER_RO";

	/*
	 * This method returns a list of WeatherData based on the given cityname and
	 * start- & enddate.
	 * 
	 * @param cityname The Name of the city for which the data should be fetched
	 * 
	 * @param start The Startdate for the daterange
	 * 
	 * @param end The Enddate for the daterange
	 * 
	 */
	public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end)
			throws RemoteException;

	/*
	 * This method returns all CityNames as a List of Strings to display them in the
	 * UI
	 */
	public List<String> getCityNamesAsList() throws RemoteException;

	/*
	 * This method returns the Average WeatherData (temp, pressure, humidity) for a
	 * specific City and daterange defined by the parameters. Be aware that there is
	 * no timestamp returned with the record as an average can not be mapped to a
	 * specific timestamp.
	 * 
	 * @param cityname The Name of the city for which the data should be fetched
	 * 
	 * @param start The Startdate for the daterange
	 * 
	 * @param end The Enddate for the daterange
	 */
	public String selectAverageWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;

	/*
	 * This method returns the Maximum WeatherData (temp, pressure, humidity) for a
	 * specific City and daterange defined by the parameters. Be aware that there is
	 * no timestamp returned with the record as a Maximum can not be mapped to a
	 * specific timestamp.
	 * 
	 * @param cityname The Name of the city for which the data should be fetched
	 * 
	 * @param start The Startdate for the daterange
	 * 
	 * @param end The Enddate for the daterange
	 */
	public String selectMaxWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;

	
	/*
	 * This method returns the Minimum WeatherData (temp, pressure, humidity) for a
	 * specific City and daterange defined by the parameters. Be aware that there is
	 * no timestamp returned with the record as a Minimum can not be mapped to a
	 * specific timestamp.
	 * 
	 * @param cityname The Name of the city for which the data should be fetched
	 * 
	 * @param start The Startdate for the daterange
	 * 
	 * @param end The Enddate for the daterange
	 */
	public String selectMinWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException;

	/*
	 * This method returns the Maximum WeatherData (temp, pressure, humidity) for
	 * all Cities and a specific timestamp defined by the parameters. Be aware that there is
	 * no timestamp returned with the record as a Maximum can not be mapped to a
	 * specific timestamp.
	 * 
	 * 
	 * @param timestamp The timestamp for which the data should be fetched
	 */
	public List<String> selectMaxTemperatureAllCities(Timestamp inputTimeStamp) throws RemoteException;

	
	public List<String> selectMinTemperatureAllCities(Timestamp inputTimeStamp) throws RemoteException;
	
	
	public List<String> selectMaxPressureAllCities(Timestamp inputTimeStamp) throws RemoteException;
	
	public List<String> selectMinPressureAllCities(Timestamp inputTimeStamp) throws RemoteException;
	
	public List<String> selectMaxHumidityAllCities(Timestamp inputTimeStamp) throws RemoteException;

	
	public List<String> selectMinHumidityAllCities(Timestamp inputTimeStamp) throws RemoteException;

	
	public List<User> getUserNamesAsList() throws RemoteException;

	
	public boolean insertUser(User user) throws RemoteException;

	
	public boolean updateUser(User user) throws RemoteException;

	
	public boolean deleteUser(User user) throws RemoteException;

	
	public void writeCSV(List<WeatherData> weatherDataList) throws RemoteException;

	public byte[] downloadWeatherDataAsCSV() throws RemoteException;
	
}