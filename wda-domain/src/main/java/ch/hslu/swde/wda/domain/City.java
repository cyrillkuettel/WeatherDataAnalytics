package ch.hslu.swde.wda.domain;

public class City {

	
	private int ZIPCode;
	private String Name;
	private float longitude;
	private float latitude;
	
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public int getZIPCode() {
		return ZIPCode;
	}
	
	
}
