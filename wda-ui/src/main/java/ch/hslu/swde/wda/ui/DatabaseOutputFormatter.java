package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;

/**
 * This class is intended to format the List<WeaatherData> Objects, so that it can be displayed as String
 * in the Terminal.
 *

 *
 There will always be 3 parameters:
     *   City (Always a single city, or EVERY City)
     *   startDate ( user specified )
     *   endDate ( user specified )
 */
public final class DatabaseOutputFormatter {
    private static final Logger Log = LogManager.getLogger(DatabaseOutputFormatter.class);


    public List<WeatherData> selectWeatherByDateAndCity(String cityname, String start, String end) {


        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);
        List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity(cityname, startDate,                                                                            endDate);
        Log.info(requestedWeatherData);
        return requestedWeatherData;
    }



}
