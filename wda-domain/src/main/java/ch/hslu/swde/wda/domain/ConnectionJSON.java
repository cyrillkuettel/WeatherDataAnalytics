package ch.hslu.swde.wda.domain;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConnectionJSON {
	
	 public static void main(String[] args) {
		 jsonGetRequest("http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities");
	
		  }

		  private static String streamToString(InputStream inputStream) {
		    String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
		    return text;
		  }

		  //Gets all the cities
		  public static void jsonGetRequest(String urlQueryString) {
		    String json = null;
		    try {
		      URL url = new URL(urlQueryString);
		      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		      connection.setDoOutput(true);
		      connection.setInstanceFollowRedirects(false);
		      connection.setRequestMethod("GET");
		      connection.setRequestProperty("Content-Type", "application/json");
		      connection.setRequestProperty("charset", "utf-8");
		      connection.connect();
		      InputStream inStream = connection.getInputStream();
		      json = streamToString(inStream); // input stream to string
		      //System.out.println(json);
		    } catch (IOException ex) {
		      ex.printStackTrace();
		    }
		    
		    System.out.println("Cities loaded");
		    jsonarray(json);
		    
		  }
		  
		  //Gets the weatherData for the City since on year and calls method jsonarrayAll on result
		  public static String jsonGetRequestAll(String urlQueryString) {
			    
			    String json = null;
			    try {
			      URL url = new URL("http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/"+urlQueryString+"/since?year=2021&month=01&day=01");
			      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			      connection.setDoOutput(true);
			      connection.setInstanceFollowRedirects(false);
			      connection.setRequestMethod("GET");
			      connection.setRequestProperty("Content-Type", "application/json");
			      connection.setRequestProperty("charset", "utf-8");
			      connection.connect();
			      InputStream inStream = connection.getInputStream();
			      json = streamToString(inStream); // input stream to string
			    } catch (IOException ex) {
			      ex.printStackTrace();
			    }
			    //System.out.println(json);
			    jsonarrayAll(json);
			    return json;
			  }
		  
		  //Makes the String to a JSONArray and calls jsonGetRequestAll for every city
		  public static void jsonarray(String var) {
			  int i;
			  //Create a JSON Array 
			  JSONArray myJson = new JSONArray(var);
			  //Iterate over JSONArray
			  for(i=0; i<myJson.length(); i++) {
				  //Get every Item in JSONArray as a JSONObject
				  JSONObject obj = (JSONObject) myJson.get(i);
				  //Get every City
				  String city = obj.getString("name");
				  city = city.replace(" ", "");
				  System.out.println("Get Data for City: "+city);
				  String response = jsonGetRequestAll(city);
			  }
		

				  
			  }
			  
		//Extracts data entry and also lastupdatetime from every data entry and call jsonarrayAllWeather on both
			  public static void jsonarrayAll(String var) {
				  int i;
				  //Create a JSON Array 
				  JSONArray myJson = new JSONArray(var);
				  //Iterate over JSONArray
				  for(i=0; i<myJson.length(); i++) {
					  //Get every Item in JSONArray as a JSONObject
					  JSONObject obj = (JSONObject) myJson.get(i);
					  //Get every Entry per city
					  String data = obj.getString("data");
					  String update = obj.getString("lastUpdateTime");
					  jsonarrayAllWeather(data, update);
	  
				  }
			  }
			  
				  public static void jsonarrayAllWeather(String var, String update) {
					  String[] str = var.split("#");
					  String[] hold = str[0].split(" ");
					  hold[1] = hold[1].replace(":", "");
					  hold[0] = hold[0].replace("LAST_UPDATE_TIME:", "");
				
					  Map<String, String> Map = new HashMap<String, String>();
					  for(int i = 0; i<str.length; i++){
						  String s = str[i];
						  String wData[] = s.split(":");
						  String key = wData[0].trim();
						  String value = wData[1].trim();
						  Map.put(key, value);
						  }
					  Map.put("LAST_UPDATE_DATE", hold[0]);
					  Map.put("LAST_UPDATE_TIME", hold[1]);
					  ObjectMaker.create(Map);
					  
					  
					  }
				  
				  //Hier noch anpassen. Ich kann die Daten direkt aus dem JSON Objekt holen gleich im richtigen Typ.
				  //Siehe ConnectionJSONCities
  
		  }


