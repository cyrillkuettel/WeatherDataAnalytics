package ch.hslu.swde.wda.domain;

public class Weather {
	
	private int WeatherID;
	private String Summary;
	private String Description;
	
	
	public int getWeatherID() {
		return WeatherID;
	}
	public void setWeatherID(int weatherID) {
		WeatherID = weatherID;
	}
	public String getSummary() {
		return Summary;
	}
	public void setSummary(String summary) {
		Summary = summary;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

}
