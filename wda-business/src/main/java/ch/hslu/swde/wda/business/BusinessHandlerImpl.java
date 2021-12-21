package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import ch.hslu.swde.wda.persister.PersistUser;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
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
	private String CSV_Path;

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

		return new ArrayList<WeatherData>(requestedWeatherData);
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
				"Maximale Werte für Temperatur, Druck und Feuchtigkeit für Ortschaft %s:" + " %s, %s and %s", cityname, temp,
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
	public List<String> selectMinTemperatureAllCities(Timestamp inputTimeStamp) throws RemoteException {

		List<WeatherData> weatherDataMinAll = DbHelper.selectMinTemperatureAllCities(inputTimeStamp);
		List<String> descriptionList = new ArrayList();
		String minDescription = "";
		for(WeatherData w : weatherDataMinAll) {
			String temp = String.valueOf(w.getTemp());
			String city = w.getCity().getName();
			 minDescription = String.format(
					"Minimaler Wert für Temperatur in Ortschaft %s:" + '\n' + " %s",
					city,temp);
			 descriptionList.add(minDescription);
		}

		return descriptionList;
	}

	@Override
	public List<String> selectMaxTemperatureAllCities(Timestamp inputTimeStamp) throws RemoteException {

		List<WeatherData> weatherDataMinAll = DbHelper.selectMaxTemperatureAllCities(inputTimeStamp);
		List<String> descriptionList = new ArrayList();
		String minDescription = "";
		for(WeatherData w : weatherDataMinAll) {
			String temp = String.valueOf(w.getTemp());
			String city = w.getCity().getName();
			 minDescription = String.format(
					"Maximaler Wert für Temperatur in Ortschaft %s:" + '\n' + " %s",
					city,temp);
			 descriptionList.add(minDescription);
		}

		return descriptionList;
	}
	
	@Override
	public List<String> selectMinPressureAllCities(Timestamp inputTimeStamp) throws RemoteException {

		List<WeatherData> weatherDataMinAll = DbHelper.selectMinPressureAllCities(inputTimeStamp);
		List<String> descriptionList = new ArrayList();
		String minDescription = "";
		for(WeatherData w : weatherDataMinAll) {
			String pressure = String.valueOf(w.getPressure());
			String city = w.getCity().getName();
			 minDescription = String.format(
					"Minimaler Wert für Luftdruck in Ortschaft %s:" + '\n' + " %s",
					city,pressure);
			 descriptionList.add(minDescription);
		}

		return descriptionList;
	}

	@Override
	public List<String> selectMaxPressureAllCities(Timestamp inputTimeStamp) throws RemoteException {

		List<WeatherData> weatherDataMinAll = DbHelper.selectMaxPressureAllCities(inputTimeStamp);
		List<String> descriptionList = new ArrayList();
		String minDescription = "";
		for(WeatherData w : weatherDataMinAll) {
			String pressure = String.valueOf(w.getPressure());
			String city = w.getCity().getName();
			 minDescription = String.format(
					"Maximaler Wert für Luftdruck in Ortschaft %s:" + '\n' + " %s",
					city,pressure);
			 descriptionList.add(minDescription);
		}

		return descriptionList;
	}
	
	@Override
	public List<String> selectMinHumidityAllCities(Timestamp inputTimeStamp) throws RemoteException {

		List<WeatherData> weatherDataMinAll = DbHelper.selectMinHumidityAllCities(inputTimeStamp);
		List<String> descriptionList = new ArrayList();
		String minDescription = "";
		for(WeatherData w : weatherDataMinAll) {
			String humidity = String.valueOf(w.getHumidity());
			String city = w.getCity().getName();
			 minDescription = String.format(
					"Minimaler Wert für Luftfeuchtigkeit in Ortschaft %s:" + '\n' + " %s",
					city,humidity);
			 descriptionList.add(minDescription);
		}

		return descriptionList;
	}

	@Override
	public List<String> selectMaxHumidityAllCities(Timestamp inputTimeStamp) throws RemoteException {

		List<WeatherData> weatherDataMinAll = DbHelper.selectMaxHumidityAllCities(inputTimeStamp);
		List<String> descriptionList = new ArrayList();
		String minDescription = "";
		for(WeatherData w : weatherDataMinAll) {
			String humidity = String.valueOf(w.getHumidity());
			String city = w.getCity().getName();
			 minDescription = String.format(
					"Maximaler Wert für Luftfeuchtigkeit in Ortschaft %s:" + '\n' + " %s",
					city,humidity);
			 descriptionList.add(minDescription);
		}

		return descriptionList;
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
		Log.info("Called Delete User");
        return true;
    }

    @Override
    public void writeCSV(List<WeatherData> weatherDataList) throws RemoteException {
        // first create file object for file placed at location
        // specified by filepath
        String home = System.getProperty("user.home");
        String fileName = "Weatherdata";
        String path = "Not initialized";
        File file = new File(home + "/Downloads/" + fileName + ".csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"Datatimestamp", "Zipcode", "Cityname", "Temperature", "Pressure", "Humidity"};
            writer.writeNext(header);

            // add data to csv

            for (WeatherData w : weatherDataList) {

                String[] data = {w.getDataTimestamp().toString(), String.valueOf(w.getCity().getZIPCode()),
                        w.getCity().getName(), String.valueOf(w.getTemp()), String.valueOf(w.getPressure()),
                        String.valueOf(w.getHumidity())};
                writer.writeNext(data);

            }

            // closing writer connection
            writer.close();
            path = home + "/Downloads/" + fileName + ".csv";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Now we can send this File to client. Specify the path.
        CSV_Path = path;
    }

    /**
     * This method assumes that it is always called after writeCSV.
     * Depends on the CSV_Path field of this class.
     * @return The cvs File as bytes.
     * @throws RemoteException Throws Exception.
     */
    @Override
    public byte[] downloadWeatherDataAsCSV() throws RemoteException {

        byte[] rawData;

        File serverpathfile = new File(CSV_Path);
        rawData = new byte[(int) serverpathfile.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverpathfile);
            try {
                in.read(rawData, 0, rawData.length);
            } catch (IOException e) {
                Log.warn("Failed to read in FileInputStream");
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.warn("Failed to close the File after using FileInputStream.");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return rawData;
    }

}
