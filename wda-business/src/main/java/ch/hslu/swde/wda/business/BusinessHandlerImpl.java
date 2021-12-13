package ch.hslu.swde.wda.business;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import ch.hslu.swde.wda.persister.PersistUser;

public class BusinessHandlerImpl extends UnicastRemoteObject implements BusinessHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7973402984033177026L;
	
	private static final Logger Log = LogManager.getLogger(BusinessHandlerImpl.class);
	private DbHelper DbHelper;
	private PersistUser persistUser;

	public BusinessHandlerImpl() throws RemoteException {
		DbHelper = new DbHelper();
		persistUser = new PersistUser();
	}

	@Override
	public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end)
			throws RemoteException {

		List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity(cityname, Date.valueOf(start),
				Date.valueOf(end));
		Log.info(requestedWeatherData);
		return requestedWeatherData;
	}

	@Override
	public List<String> getCityNamesAsList() throws RemoteException {
		List<City> cityList;

		try {
			cityList = DbHelper.selectAllCities();
			return cityList.stream().map(City::getName).collect(Collectors.toList());
		} catch (jakarta.persistence.NoResultException e) {
			Log.warn("that city does not (yet) exist in database");
			e.printStackTrace();
		}
		Log.warn("City List is empty! ");
		return Collections.emptyList();
	}

	@Override
	public String selectAverageWeatherDataSingleCity(String cityname, String start, String end) throws RemoteException {

		WeatherData requestedWeatherData;

		requestedWeatherData = DbHelper.selectAverageWeatherDataSingleCity(cityname, Date.valueOf(start),
				Date.valueOf(end));

		String humidity = String.valueOf(requestedWeatherData.getHumidity());
		String pressure = String.valueOf(requestedWeatherData.getPressure());
		String temp = String.valueOf(requestedWeatherData.getTemp());

		String Description = String.format(
				"Durchschnittliche Werte für Temperatur, Druck und Feuchtigkeit" + '\n' + " %s, %s and %s", temp,
				pressure, humidity);
		return Description;
	}

	@Override
	public List<String> selectMaxMinCity(String cityname, String start, String end) throws RemoteException {

		WeatherData weatherDataMAXIMUM = DbHelper.selectMaxWeatherDataSingleCity(cityname, Date.valueOf(start),
				Date.valueOf(end));
		WeatherData weatherDataMINIMUM = DbHelper.selectMinWeatherDataSingleCity(cityname, Date.valueOf(start),
				Date.valueOf(end));

		String humidity = String.valueOf(weatherDataMAXIMUM.getHumidity());
		String pressure = String.valueOf(weatherDataMAXIMUM.getPressure());
		String temp = String.valueOf(weatherDataMAXIMUM.getTemp());

		String maxDescription = String.format(
				"Maximum Values for temperatur, pressure and humidity for %s are:" + '\n' + " %s, %s and %s", cityname,
				temp, pressure, humidity);

		humidity = String.valueOf(weatherDataMINIMUM.getHumidity());
		pressure = String.valueOf(weatherDataMINIMUM.getPressure());
		temp = String.valueOf(weatherDataMINIMUM.getTemp());

		String minDescription = String.format(
				"Minimum Values for temperatur, pressure and humidity for %s are:" + '\n' + " %s, %s and %s", cityname,
				temp, pressure, humidity);
		List<String> max_min = new ArrayList<>();
		max_min.add(maxDescription + '\n');
		max_min.add(minDescription);
		return max_min;
	}

	@Override
	public List<String> selectMaxWeatherDataAllCity(String inputTimeStamp) throws RemoteException {

		WeatherData weatherDataMaxAll = DbHelper.selectMaxWeatherDataAllCity(Timestamp.valueOf(inputTimeStamp));
		WeatherData weatherDataMinAll = DbHelper.selectMinWeatherDataAllCity(Timestamp.valueOf(inputTimeStamp));

		String humidity = String.valueOf(weatherDataMaxAll.getHumidity());
		String pressure = String.valueOf(weatherDataMaxAll.getPressure());
		String temp = String.valueOf(weatherDataMaxAll.getTemp());

		String maxDescription = String.format(
				"Maximum Values for temperatur, pressure and humidity over all cities are:" + '\n' + " %s, %s and %s",
				temp, pressure, humidity);

		humidity = String.valueOf(weatherDataMinAll.getHumidity());
		pressure = String.valueOf(weatherDataMinAll.getPressure());
		temp = String.valueOf(weatherDataMinAll.getTemp());

		String minDescription = String.format(
				"Minimum Values for temperatur, pressure and humidity over all cities are:" + '\n' + " %s, %s and %s",
				temp, pressure, humidity);

		List<String> max_min = new ArrayList<>();
		max_min.add(maxDescription + '\n');
		max_min.add(minDescription);
		return max_min;

	}

	@Override
	public List<User> getUserNamesAsList() throws RemoteException {
		List<User> userList;

		userList = DbHelper.selectAllUserData();
		return userList;
	}

	@Override
	public boolean insertUser(User user) throws RemoteException {

		try {
			persistUser.insertUser(user);
		}
		catch(jakarta.persistence.NoResultException e){
			
		}
		

		return true;
	}

	@Override
	public boolean updateUser(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
