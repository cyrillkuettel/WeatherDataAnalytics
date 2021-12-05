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

	public WeatherData() {
	}

	public WeatherData(City city, Timestamp datatimestamp, double temp, double pressure, double humidity) {

		this.city = city;
		this.datatimestamp = datatimestamp;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
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

	@Override
	public String toString() {
		return "Weatherdata: [City: ZIP=" + city.getZIPCode() + "City: Name=" + city.getName()
				+ ", WeatherData: Timestamp=" + datatimestamp + ", WeatherData: Temperatur=" + temp
				+ ", WeatherData: Pressure=" + pressure + ", WeatherData: Humidity=" + humidity + "]";
	}

}
