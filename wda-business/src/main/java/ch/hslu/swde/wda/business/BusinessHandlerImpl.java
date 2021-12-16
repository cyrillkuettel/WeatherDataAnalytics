package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import ch.hslu.swde.wda.persister.PersistUser;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
		//CSV is being created automatically with the full Weatherdata information
		writeCSV(requestedWeatherData);
		
		return requestedWeatherData.subList(0, 10);
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
	public String selectMaxWeatherDataSingleCity(String cityname, String start, String end) {
		java.sql.Date startDate = java.sql.Date.valueOf(start);
		java.sql.Date endDate = Date.valueOf(end);

		WeatherData weatherDataMAXIMUM = DbHelper.selectMaxWeatherDataSingleCity(cityname, startDate, endDate);
		String humidity = String.valueOf(weatherDataMAXIMUM.getHumidity());
		String pressure = String.valueOf(weatherDataMAXIMUM.getPressure());
		String temp = String.valueOf(weatherDataMAXIMUM.getTemp());

		String maxDescription = String.format(
				"Maximum Values for temperatur, pressure and humidity for %s:" + " %s, %s and %s", cityname, temp,
				pressure, humidity);

		return maxDescription;

	}

	@Override
	public String selectMinWeatherDataSingleCity(String cityname, String start, String end) {
		java.sql.Date startDate = java.sql.Date.valueOf(start);
		java.sql.Date endDate = Date.valueOf(end);

		WeatherData weatherDataMINIMUM = DbHelper.selectMinWeatherDataSingleCity(cityname, startDate, endDate);

		String humidity = String.valueOf(weatherDataMINIMUM.getHumidity());
		String pressure = String.valueOf(weatherDataMINIMUM.getPressure());
		String temp = String.valueOf(weatherDataMINIMUM.getTemp());

		String minDescription = String.format(
				"Maximale Werte für Temperatur, Druck und Feuchtigkeit über alle Ortschaften:" + " %s, %s and %s", temp,
				pressure, humidity);
		return minDescription;

	}

	@Override
	public String selectMinWeatherDataAllCity(Timestamp inputTimeStamp) throws RemoteException {

		WeatherData weatherDataMinAll = DbHelper.selectMinWeatherDataAllCity(inputTimeStamp);
		String humidity = String.valueOf(weatherDataMinAll.getHumidity());
		String pressure = String.valueOf(weatherDataMinAll.getPressure());
		String temp = String.valueOf(weatherDataMinAll.getTemp());

		String minDescription = String.format(
				"Minimum Values for temperatur, pressure and humidity over all cities are:" + '\n' + " %s, %s and %s",
				temp, pressure, humidity);

		return minDescription;
	}

	@Override
	public String selectMaxWeatherDataAllCity(Timestamp inputTimeStamp) throws RemoteException {

		WeatherData weatherDataMaxAll = DbHelper.selectMaxWeatherDataAllCity(inputTimeStamp);

		String humidity = String.valueOf(weatherDataMaxAll.getHumidity());
		String pressure = String.valueOf(weatherDataMaxAll.getPressure());
		String temp = String.valueOf(weatherDataMaxAll.getTemp());

		String maxDescription = String.format(
				"Maximale Werte für Temperatur, Druck und Feuchtigkeit über alle Ortschaften:" + " %s, %s and %s", temp,
				pressure, humidity);
		return maxDescription;
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
		} catch (jakarta.persistence.NoResultException e) {

		}

		return true;
	}

    @Override
    public boolean updateUser(User user) throws RemoteException {
		try {
			persistUser.updateUser(user);
		} catch (jakarta.persistence.NoResultException e) {

		}

		return true;
    }

    @Override
    public boolean deleteUser(User user) throws RemoteException {

		persistUser.deleteUser(user);
        return true;
    }
    
	@Override
	public void writeCSV(List<WeatherData> weatherDataList) throws RemoteException {
		// first create file object for file placed at location
		// specified by filepath
		String home = System.getProperty("user.home");
		String fileName = "Weatherdata";
		File file = new File(home + "/Downloads/" + fileName + ".csv");
		try {
			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// adding header to csv
			String[] header = { "Datatimestamp", "Zipcode", "Cityname", "Temperature", "Pressure", "Humidity" };
			writer.writeNext(header);

			// add data to csv

			for (WeatherData w : weatherDataList) {

				String[] data = { w.getDataTimestamp().toString(), String.valueOf(w.getCity().getZIPCode()),
						w.getCity().getName(), String.valueOf(w.getTemp()), String.valueOf(w.getPressure()),
						String.valueOf(w.getHumidity()) };
				writer.writeNext(data);

			}

			// closing writer connection
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
