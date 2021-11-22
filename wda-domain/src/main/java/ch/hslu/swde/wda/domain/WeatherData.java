package ch.hslu.swde.wda.domain;

import java.sql.Timestamp;

public class WeatherData {

	
	private int WeatherdataID;
	private int ZIPCode;
	private int WeatherID;
	
	Timestamp timestamp;
	
	private float Current_temp_celcius;
	private int Pressure;
	private float Humidity;
	private float Windspeed;
	private float Winddirection;
	
	
	public int getWeatherdataID() {
		return WeatherdataID;
	}
	public void setWeatherdataID(int weatherdataID) {
		WeatherdataID = weatherdataID;
	}
	public int getZIPCode() {
		return ZIPCode;
	}
	public void setZIPCode(int zIPCode) {
		ZIPCode = zIPCode;
	}
	public int getWeatherID() {
		return WeatherID;
	}
	public void setWeatherID(int weatherID) {
		WeatherID = weatherID;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public float getCurrent_temp_celcius() {
		return Current_temp_celcius;
	}
	public void setCurrent_temp_celcius(float current_temp_celcius) {
		Current_temp_celcius = current_temp_celcius;
	}
	public int getPressure() {
		return Pressure;
	}
	public void setPressure(int pressure) {
		Pressure = pressure;
	}
	public float getHumidity() {
		return Humidity;
	}
	public void setHumidity(float humidity) {
		Humidity = humidity;
	}
	public float getWindspeed() {
		return Windspeed;
	}
	public void setWindspeed(float windspeed) {
		Windspeed = windspeed;
	}
	public float getWinddirection() {
		return Winddirection;
	}
	public void setWinddirection(float winddirection) {
		Winddirection = winddirection;
	}
	
	
	
	
}
