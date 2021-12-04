package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import ch.hslu.swde.wda.persister.PersistCity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Requests Data from Database. Then it does some formatting, because DBHelper returns List<Weatherdata> Objects.
 *
 There will always be 3 parameters:
     *   City (all or one City)
     *   startDate
     *   endDate
 */
public final class DatabaseOutputFormatter {
    private static final Logger Log = LogManager.getLogger(DatabaseOutputFormatter.class);


    public List<WeatherData>  selectWeatherByDateAndCity(String cityname, String start, String end) {


        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);
        List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity(cityname, startDate,
                                                                                              endDate);
        Log.info(requestedWeatherData);
        return requestedWeatherData;
    }

    /**
     * Only for testing
     * copied from ch/hslu/swde/wda/persister/test.java
     */
    public void insertTestCities() {
        //int zipCode,String name, float longitude, float latitude
        City city1 = new City(4900, "Langenthal", 3.0f, 22.11f);


        List<City> cityList = new ArrayList();
        cityList.add(city1);

        PersistCity.insertCity(cityList);
    }


}
