package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is intended to format the List<WeaatherData> Objects, so that it can be displayed as String
 * in the Terminal.
 * <p>
 * <p>
 * <p>
 * There will always be 3 parameters:
 * City (Always a single city, or EVERY City)
 * startDate ( user specified )
 * endDate ( user specified )
 */
public final class DatabaseOutputFormatter {
    private static final Logger Log = LogManager.getLogger(DatabaseOutputFormatter.class);
    private final DbHelper DbHelper;
    
    public DatabaseOutputFormatter() {
    	DbHelper = new DbHelper();
    }


    public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end) {


        Date startDate = null;
        Date endDate = null;
        try {
            startDate = Date.valueOf(start);
            endDate = Date.valueOf(end);
        } catch (Exception e) {
            Log.info("change the dates ");
        }
                                                   // expected format:  yyyy-mm-dd
        List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity(cityname, startDate, endDate);
        return requestedWeatherData;
    }

    public String[] convertCitiesFromArrayToList() {

        List<String> list = getCityNamesAsList();
        return list.toArray(new String[0]);
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

    public List<String> selectMaxMinCity(String cityname, String start, String end) {
        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);


        WeatherData weatherDataMAXIMUM = DbHelper.selectMaxWeatherDataSingleCity(cityname, startDate, endDate);
        WeatherData weatherDataMINIMUM = DbHelper.selectMinWeatherDataSingleCity(cityname, startDate, endDate);

        String humidity = String.valueOf(weatherDataMAXIMUM.getHumidity());
        String pressure = String.valueOf(weatherDataMAXIMUM.getPressure());
        String temp = String.valueOf(weatherDataMAXIMUM.getTemp());

        String maxDescription =
                String.format("Maximum Values for temperatur, pressure and humidity for %s are:" + '\n' +
                                      " %s, %s and %s",
                              cityname, temp, pressure, humidity);

        humidity = String.valueOf(weatherDataMINIMUM.getHumidity());
        pressure = String.valueOf(weatherDataMINIMUM.getPressure());
        temp = String.valueOf(weatherDataMINIMUM.getTemp());

        String minDescription =
                String.format("Minimum Values for temperatur, pressure and humidity for %s are:" + '\n' +
                                      " %s, %s and %s",
                              cityname, temp, pressure, humidity);
        List<String> max_min = new ArrayList<>();
        max_min.add(maxDescription + '\n');
        max_min.add(minDescription);
        return max_min;

    }

    public List<String> selectMaxWeatherDataAllCity(String inputTimeStamp) {


        WeatherData weatherDataMaxAll = DbHelper.selectMaxWeatherDataAllCity(Timestamp.valueOf(inputTimeStamp));
        WeatherData weatherDataMinAll = DbHelper.selectMinWeatherDataAllCity(Timestamp.valueOf(inputTimeStamp));

        String humidity = String.valueOf(weatherDataMaxAll.getHumidity());
        String pressure = String.valueOf(weatherDataMaxAll.getPressure());
        String temp = String.valueOf(weatherDataMaxAll.getTemp());

        String maxDescription =
                String.format("Maximum Values for temperatur, pressure and humidity over all cities are:" + '\n' +
                                      " %s, %s and %s", temp, pressure, humidity);

        humidity = String.valueOf(weatherDataMinAll.getHumidity());
        pressure = String.valueOf(weatherDataMinAll.getPressure());
        temp = String.valueOf(weatherDataMinAll.getTemp());

        String minDescription =
                String.format("Minimum Values for temperatur, pressure and humidity over all cities are:" + '\n' +
                                      " %s, %s and %s", temp, pressure, humidity);


        List<String> max_min = new ArrayList<>();
        max_min.add(maxDescription + '\n');
        max_min.add(minDescription);
        return max_min;


    }
}
