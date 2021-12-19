package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import ch.hslu.swde.wda.persister.PersistUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is intended to format the List of WeaatherData Objects, so that it can be displayed as String
 * in the Terminal.
 * There will always be 3 parameters:
 * City (Always a single city, or EVERY City)
 * startDate ( user specified )
 * endDate ( user specified )
 */
public final class DatabaseOutputFormatter {
    private static final Logger Log = LogManager.getLogger(DatabaseOutputFormatter.class);
    private final DbHelper DbHelper;
    private final PersistUser persistUser;

    public DatabaseOutputFormatter() {
        DbHelper = new DbHelper();
        persistUser = new PersistUser();
    }


    public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end) {


        Date startDate = null;
        Date endDate = null;
        try {
            startDate = Date.valueOf(start);
            endDate = Date.valueOf(end);
        } catch (Exception e) {
            Log.warn("change the dates ");
        }
        // expected format:  yyyy-mm-dd
        List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity(cityname, startDate, endDate);
        return requestedWeatherData;
    }


    public List<List<WeatherData>> selectWeatherOfAllCitiesInTimeframe(final String start, final String end,
                                                                       final String[] availableCities) {
        List<List<WeatherData>> dataOfAllCities = new ArrayList<>();
        for (String city : availableCities) {
            List<WeatherData> list = selectWeatherByDateAndCity(city, start, end);
            dataOfAllCities.add(list);
        }
        return dataOfAllCities;
    }


    public List<String> getCityNamesAsList() {

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


    public String selectAverageWeatherDataSingleCity(String cityname, String start, String end) {
        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);

        WeatherData requestedWeatherData;

        requestedWeatherData = DbHelper.selectAverageWeatherDataSingleCity(cityname, startDate, endDate);


        String humidity = String.valueOf(requestedWeatherData.getHumidity());
        String pressure = String.valueOf(requestedWeatherData.getPressure());
        String temp = String.valueOf(requestedWeatherData.getTemp());
        // String[] averageValues = {humidity, pressure, temp};

        String Description =
                String.format("Durchschnittliche Werte für Temperatur, Druck und Feuchtigkeit" + '\n' +
                                      " %s, %s and %s", temp, pressure, humidity);
        return Description;


    }

    public String selectMaxWeatherDataSingleCity(String cityname, String start, String end) {
        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);

        WeatherData weatherDataMAXIMUM = DbHelper.selectMaxWeatherDataSingleCity(cityname, startDate, endDate);
        String humidity = String.valueOf(weatherDataMAXIMUM.getHumidity());
        String pressure = String.valueOf(weatherDataMAXIMUM.getPressure());
        String temp = String.valueOf(weatherDataMAXIMUM.getTemp());

        String maxDescription =
                String.format("Maximum Values for temperatur, pressure and humidity for %s:" +
                                      " %s, %s and %s",
                              cityname, temp, pressure, humidity);

        return maxDescription;

    }

    public String selectMinWeatherDataSingleCity(String cityname, String start, String end) {
        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);

        WeatherData weatherDataMINIMUM = DbHelper.selectMinWeatherDataSingleCity(cityname, startDate, endDate);

        String humidity = String.valueOf(weatherDataMINIMUM.getHumidity());
        String pressure = String.valueOf(weatherDataMINIMUM.getPressure());
        String temp = String.valueOf(weatherDataMINIMUM.getTemp());

        String minDescription =
                String.format("Minimum Values for temperatur, pressure and humidity for %s :" +
                                      " %s, %s and %s",
                              cityname, temp, pressure, humidity);
        return minDescription;

    }

    public String selectMinWeatherDataAllCity(Timestamp inputTimeStamp) {

        WeatherData weatherDataMinAll = DbHelper.selectMinWeatherDataAllCity(inputTimeStamp);
        String humidity = String.valueOf(weatherDataMinAll.getHumidity());
        String pressure = String.valueOf(weatherDataMinAll.getPressure());
        String temp = String.valueOf(weatherDataMinAll.getTemp());

        String minDescription =
                String.format("Minimale Werte für Temperatur, Druck und Feuchtigkeit über alle Ortschaften:" + '\n' +
                                      " %s, %s and %s", temp, pressure, humidity);

        return minDescription;
    }

    public String selectMaxWeatherDataAllCity(Timestamp inputTimeStamp) {

        WeatherData weatherDataMaxAll = DbHelper.selectMaxWeatherDataAllCity(inputTimeStamp);

        String humidity = String.valueOf(weatherDataMaxAll.getHumidity());
        String pressure = String.valueOf(weatherDataMaxAll.getPressure());
        String temp = String.valueOf(weatherDataMaxAll.getTemp());

        String maxDescription =
                String.format("Maximale Werte für Temperatur, Druck und Feuchtigkeit über alle Ortschaften:" + " %s, %s and %s", temp, pressure, humidity);
        return maxDescription;
    }



    /*  new functions: various user administration stuff */
    public boolean insertUser(User user) {

        try {
            persistUser.insertUser(user);
            return true;
        } catch (Exception e) {
            Log.warn("Error inserting User to database");
        }
        return false;
    }

    public List<User> selectAllUserData() {
        return DbHelper.selectAllUserData();
    }

    public void persistUser(User user) {
        persistUser.insertUser(user);
    }

}
