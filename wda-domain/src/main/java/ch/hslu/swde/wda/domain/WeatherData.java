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
@Table(name = "weatherdata")
public class WeatherData implements Serializable {

	private static final long serialVersionUID = 5740070809225824469L;

	@Id
	@GeneratedValue
	private int weatherdataid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city")
	private City city;

	Timestamp datatimestamp;

	private double temp;
	private double pressure;
	private double humidity;
	private double windspeed;
	private double winddirection;
	private String summary;
	private String description;

	public WeatherData() {
	}

	
	public WeatherData(City city, Timestamp datatimestamp, double temp, double pressure, double humidity,
			double windspeed, double winddirection, String summary, String description) {

		this.city = city;
		this.datatimestamp = datatimestamp;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.windspeed = windspeed;
		this.winddirection = winddirection;
		this.summary = summary;
		this.description = description;
	}

	public int getWeatherdataID() {
		return weatherdataid;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Timestamp getDataTimestamp() {
		return datatimestamp;
	}

	public void setTimestamp(Timestamp datatimestamp) {
		this.datatimestamp = datatimestamp;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getWindspeed() {
		return windspeed;
	}

	public void setWindspeed(double windspeed) {
		this.windspeed = windspeed;
	}

	public double getWinddirection() {
		return winddirection;
	}

	public void setWinddirection(double winddirection) {
		this.winddirection = winddirection;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Weatherdata: [City: ZIP=" + city.getZIPCode() + "City: Name=" + city.getName() + ", WeatherData: Summary="
				+ summary + ", WeatherData: Description=" + description + ", WeatherData: Timestamp="
				+ datatimestamp + ", WeatherData: Temperatur=" + temp + ", WeatherData: Pressure=" + pressure + ", WeatherData: Humidity=" + humidity
				+ ", WeatherData: Windspeed=" + windspeed + ", WeatherData: Winddirection=" + winddirection + "]";
	}

}
