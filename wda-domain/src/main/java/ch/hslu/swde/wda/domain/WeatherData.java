package ch.hslu.swde.wda.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "weatherData")
public class WeatherData implements Serializable {

	@Id
	@GeneratedValue
	private int weatherdataID;
	private int zipCode;
	private int weatherID;

	Timestamp timestamp;

	private float current_temp_celcius;
	private int pressure;
	private float humidity;
	private float windspeed;
	private float winddirection;

	public WeatherData() {
	}

	public WeatherData( int zipCode, int weatherID, Timestamp timestamp, float current_temp_celcius,
			int pressure, float humidity, float windspeed, float winddirection) {

		
		
		this.zipCode = zipCode;
		this.weatherID = weatherID;
		this.timestamp = timestamp;
		this.current_temp_celcius = current_temp_celcius;
		this.pressure = pressure;
		this.humidity = humidity;
		this.windspeed = windspeed;
		this.winddirection = winddirection;
	}

	public int getWeatherdataID() {
		return weatherdataID;
	}

	public void setWeatherdataID(int weatherdataID) {
		this.weatherdataID = weatherdataID;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public int getWeatherID() {
		return weatherID;
	}

	public void setWeatherID(int weatherID) {
		this.weatherID = weatherID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public float getCurrent_temp_celcius() {
		return current_temp_celcius;
	}

	public void setCurrent_temp_celcius(float current_temp_celcius) {
		this.current_temp_celcius = current_temp_celcius;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getWindspeed() {
		return windspeed;
	}

	public void setWindspeed(float windspeed) {
		this.windspeed = windspeed;
	}

	public float getWinddirection() {
		return winddirection;
	}

	public void setWinddirection(float winddirection) {
		this.winddirection = winddirection;
	}

}
