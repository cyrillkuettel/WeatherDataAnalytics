package ch.hslu.swde.wda.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather")
public class Weather implements Serializable {

	@Id
	@GeneratedValue
	private int weatherID;
	private String summary;
	private String description;

	public Weather() {
	}

	public Weather( String summary, String description) {

		
		this.summary = summary;
		this.description = description;
	}

	public int getWeatherID() {
		return weatherID;
	}

	public void setWeatherID(int weatherID) {
		this.weatherID = weatherID;
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

}
