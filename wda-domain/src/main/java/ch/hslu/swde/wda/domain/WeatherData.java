package ch.hslu.swde.wda.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "weatherdata")
public class WeatherData implements Serializable {


	private static final long serialVersionUID = 5740070809225824469L;

	@Id
	@GeneratedValue
	private int weatherdataID;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "zipCode")
	private City city;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "weatherID")
	private Weather weather;

	Timestamp timestamp;

	private float current_temp_celcius;
	private int pressure;
	private float humidity;
	private float windspeed;
	private float winddirection;

	public WeatherData() {
	}

	public WeatherData( City city, Weather weather, Timestamp timestamp, float current_temp_celcius,
			int pressure, float humidity, float windspeed, float winddirection) {

		
		this.city = city;
		this.weather = weather;
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


	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeatherID(Weather weather) {
		this.weather = weather;
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
