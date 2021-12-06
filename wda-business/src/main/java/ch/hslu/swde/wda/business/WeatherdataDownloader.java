package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.PersistWeatherData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public final class WeatherdataDownloader {
    private static final Logger Log = LogManager.getLogger(WeatherdataDownloader.class);

    static final String BASE_URI = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/";


    /**
     * Sends HttpRequest to get XML String from the web service provider.
     *
     * @param cityURL specify what we need.
     * @return XML. It's not yet processed.
     */
    public String requestRawXMLData(String cityURL) {
        HttpClient client = HttpClient.newHttpClient();
        String mimeType = "application/xml";

        URI uriObj = null;
        try {
            uriObj = new URI(BASE_URI + cityURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(uriObj);
        builder.header("Accept", mimeType);
        builder.GET();
        HttpRequest req = builder.build();
        HttpResponse<String> res = null;
        try {
            res = client.send(req, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (res.statusCode() == 200) {
            String XMLBodyDump = res.body(); // the whole thing. For Weatherdata this can be quite big.
            return XMLBodyDump;
            // do something with the retrieved message as String ...
        } else {
            // something went wrong ..
            Log.error("Http status code not 200! It's " + res.statusCode());
        }
        return "";
    }

    /**
     * Download the WeatherData from the Webservice.
     * This method has to be called after requestRawXMLData!
     *
     * @param xmlString The return value of method requestRawXMLData.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void downloadAndPersistWeatherDataSingleCity(String xmlString) throws ParserConfigurationException, IOException,
            SAXException {

        List<WeatherData> completeWeatherDataSingleCity = new ArrayList<>();


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(xmlString));
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(inStream);
        NodeList nl = doc.getDocumentElement().getChildNodes();
        int length = nl.getLength();
        if (length == 0) {
            Log.warn("Found no NodeList items in xmlString!");
        }
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nl.item(i);
                if (el.getNodeName().contains("weatherdata")) {
                    String weatherdataDump = el.getElementsByTagName("data").item(0).getTextContent();
                    String timeOfLastUPdate = el.getElementsByTagName("timeOfLastUpdate").item(0).getTextContent();
                    String cityname = el.getElementsByTagName("name").item(0).getTextContent();
                    String zip = el.getElementsByTagName("zip").item(0).getTextContent();
                    City city = new City(Integer.parseInt(zip), cityname);
                    String temperatur_deliminer_left = "#CURRENT_TEMPERATURE_CELSIUS:";
                    String pressure_deliminer_left = "#PRESSURE:";
                    String humidity_deliminer_left = "#HUMIDITY:";
                    String temperatur_deliminer_right = "#PRESSURE";
                    String pressure_deliminer_right = "#HUMIDITY";
                    String humidity_deliminer_right = "#WIND_SPEED";
                    double temperatur = -1.0;
                    double pressure = -1.0;
                    double humidity = 1.0;

                    try {
                        String temperaturstr =
                                weatherdataDump.substring(weatherdataDump.indexOf(temperatur_deliminer_left),
                                                          weatherdataDump.indexOf(temperatur_deliminer_right));
                        String finalTemper_STRIPPED = temperaturstr.substring(temperaturstr.indexOf(":") + 1);
                        String pressurestr =
                                weatherdataDump.substring(weatherdataDump.indexOf(pressure_deliminer_left),
                                                          weatherdataDump.indexOf(pressure_deliminer_right));
                        String pressurestr_STRIPPED = pressurestr.substring(pressurestr.indexOf(":") + 1);
                        String humiditystr =
                                weatherdataDump.substring(weatherdataDump.lastIndexOf(humidity_deliminer_left),
                                                          weatherdataDump.indexOf(humidity_deliminer_right));
                        String humiditystr_STRIPPED = humiditystr.substring(humiditystr.indexOf(":") + 1);


                        try {
                            temperatur = Double.parseDouble(finalTemper_STRIPPED);
                            pressure = Double.parseDouble(pressurestr_STRIPPED);
                            humidity = Double.parseDouble(humiditystr_STRIPPED);
                            WeatherData weatherData1 = new WeatherData(city, Timestamp.valueOf(timeOfLastUPdate),
                                                                       temperatur, pressure, humidity);

                            completeWeatherDataSingleCity.add(weatherData1);

                            // Log.info(weatherData1);

                        } catch (Exception e) {
                            Log.warn("string Parsing failed");
                            e.printStackTrace();
                        }

                        // System.out.println(weatherData1);
                    } catch (Exception e) {
                        Log.warn("Skipping one WeatherData object, due to unknown Exception");
                    }

                }

            }

        }

         PersistWeatherData.insertWeatherData(completeWeatherDataSingleCity);
        System.out.println(String.format("\033[32m inserted List<WeatherData> of size %d using PersistWeatherData",
                                         completeWeatherDataSingleCity.size()));


            /*
            Example weatherdata

            <?xml version="1.0" encoding="UTF-8"?>
            <weatherdata>
               <city>
                  <name>Zug</name>
                  <zip>6300</zip>
               </city>
               <data>LAST_UPDATE_TIME:2021-09-21 08:17:34#COUNTRY:CH#CITY_ZIP:6300#CITY_NAME:Zug#LONGITUDE:8.5174#LATIUDE:47.1724#STATION_ID:2657908#WEATHER_SUMMARY:Clouds#WEATHER_DESCRIPTION:broken clouds#CURRENT_TEMPERATURE_CELSIUS:11.0#PRESSURE:1025#HUMIDITY:94#WIND_SPEED:0#WIND_DIRECTION:0</data>
               <timeOfLastUpdate>2021-09-21 08:17:34</timeOfLastUpdate>
            </weatherdata>

     */
    }


    /**
     * Parse the elements in xml by the xml tag name. For Example "city" or "zip"
     *
     * @param xmlString String which is the result of a HttpResponse. The method which handles the HttpResponse
     *                  should be called before this method.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public final void parseXMLByTagName(String xmlString) throws ParserConfigurationException, IOException,
            SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(xmlString));
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(inStream);
        NodeList nl = doc.getDocumentElement().getChildNodes();
        int length = nl.getLength();
        if (length == 0) {
            Log.error("Found no NodeList items in xmlString!");
        }
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nl.item(i);
                if (el.getNodeName().contains("city")) {
                    String name = el.getElementsByTagName("name").item(0).getTextContent();
                    String zip = el.getElementsByTagName("zip").item(0).getTextContent();
                    Log.info(String.format("Parsed city from web service: name = %s zipcode = %s ", name, zip));

                    // We got a City, now persist it:
                    //  City city = new City()
                } else if (el.getNodeName().contains("weatherdata")) {
                    Log.info("weatherdata found");
                }

            }
        }
    }

    public List<City> downloadAllCitiesFromWeb() throws ParserConfigurationException, IOException,
            SAXException {

        String xmlString = requestRawXMLData("cities");
        List<City> cityList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(xmlString));
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(inStream);
        NodeList nl = doc.getDocumentElement().getChildNodes();
        int length = nl.getLength();
        if (length == 0) {
            Log.error("Found no NodeList items in xmlString!");
        }
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nl.item(i);
                if (el.getNodeName().contains("city")) {
                    String name = el.getElementsByTagName("name").item(0).getTextContent();
                    String zip = el.getElementsByTagName("zip").item(0).getTextContent();
                    // Log.info(String.format("Parsed city from web service: name = %s zipcode = %s ", name, zip));

                    // We got a City, now create city Object
                    City city = new City(Integer.parseInt(zip), name);
                    cityList.add(city);
                } else if (el.getNodeName().contains("weatherdata")) {
                    Log.info("weatherdata found");
                }

            }
        }
        return cityList;
    }

}
