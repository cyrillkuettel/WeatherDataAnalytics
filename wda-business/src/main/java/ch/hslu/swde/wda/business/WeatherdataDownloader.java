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

    public static final String ALL_SINCE_JANUARY_2020 = "/since?year=2021&month=1&day=1";





    public void startDownloadForCity(String city) {
        long startTime = System.currentTimeMillis();

        final String URL = BASE_URI + city + ALL_SINCE_JANUARY_2020;
        final String WeatherDataSingleCity = requestRawXMLData( URL);

        Log.info(String.format("Running request for %s ", URL));

        try {
            downloadAndPersistWeather_OfSingleCity(WeatherDataSingleCity);
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;

            Log.info(String.format("\033[32m Download and Persisted %s in total of %s seconds",
                                             city, elapsedSeconds));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Universal function to send Requests. Assumes that the input is already a vaild URL.
     * Sends HttpRequest to get XML String from the web service provider.
     * @param URI Parameter to specicy the ressource to request.
     * @return the raw, unprocessed Data (xml) from the Weatherdata Provider
     */
    public String requestRawXMLData(String URI) {

        HttpClient client = HttpClient.newHttpClient();
        String mimeType = "application/xml";
        URI uriObj = null;
        try {
            uriObj = new URI(URI);
        } catch (URISyntaxException e) {
            Log.warn(String.format("Failed building the URI. Check the syntax. URL is %s",  URI));
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
        }
        Log.error("Http status code not 200! It's " + res.statusCode());

        return "";
    }



    /**
     * @param rawXML Valid XML Data.
     *  This method generates the NodeList of the provided Input. It assumes the input is valid xml.
     *   NodeList are objects which represent an ordered list of nodes. In a NodeList, the nodes are returned in the
     *   order in which they are specified in the XML document.
     * @return a NodeList of XML Tags
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public NodeList generateXMLNodeList(String rawXML) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(rawXML));
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(inStream);
        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        return nodeList;
    }

    /**
     * Download the WeatherData from the Webservice.
     * This method has to be called after requestRawXMLData. It assumes that the input string contains the xml dump
     * of all requested data for a single city.
     * @param xmlString The return value of method requestRawXMLData.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @return The List<WeatherData> for that particular city. The return value can be used ( for testing), but does
     * not have to be
     * used.
     */
    public List<WeatherData> downloadAndPersistWeather_OfSingleCity(String xmlString) throws ParserConfigurationException,
            IOException,
            SAXException {

        final List<WeatherData> completeWeatherDataSingleCity = new ArrayList<>();

        final NodeList nodes = generateXMLNodeList(xmlString);

        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodes.item(i);
                if (element.getNodeName().contains("weatherdata")) {
                    WeatherData weatherData = parseXMLData_ToWeatherDataObjects(element);
                    if (weatherData != null) {
                        completeWeatherDataSingleCity.add(weatherData);
                    }
                }
            }
        }
        final PersistWeatherData persistWeatherData = new PersistWeatherData();
        persistWeatherData.insertWeatherData(completeWeatherDataSingleCity);

        System.out.println(String.format("\033[32m inserted List<WeatherData> of size %d using PersistWeatherData",
                                         completeWeatherDataSingleCity.size()));

        return completeWeatherDataSingleCity;
    }

    /**
     * Extract all the Data of Weather, along with City into a WeatherDataObject.
     * This is the method that does all the "heavy-lifting"
     * @param el
     * @return
     */
    public WeatherData parseXMLData_ToWeatherDataObjects(Element el) {

            WeatherData weatherData = null;

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

                    weatherData = new WeatherData(city, Timestamp.valueOf(timeOfLastUPdate),
                                                               temperatur, pressure, humidity);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.debug("Skipping one WeatherData object, due to unknown Exception");
                e.printStackTrace();
            }
            return weatherData;
    }


    public List<City> downloadAllCities() throws ParserConfigurationException, IOException,
            SAXException {

        final String URL = BASE_URI + "cities";
        String cityNamesUnprocessed = requestRawXMLData(URL);

        List<City> cityList = new ArrayList<>();
        NodeList nodeList = generateXMLNodeList(cityNamesUnprocessed);


        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodeList.item(i);
                if (el.getNodeName().contains("city")) {
                    String name = el.getElementsByTagName("name").item(0).getTextContent();
                    String zip = el.getElementsByTagName("zip").item(0).getTextContent();
                    // We got a City, now create city Object
                    City city = new City(Integer.parseInt(zip), name);
                    cityList.add(city);
                }
            }
        }
        return cityList;
    }

     /*
            Example weatherdata
            ===================
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
