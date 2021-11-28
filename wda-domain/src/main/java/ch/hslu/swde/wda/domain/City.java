package ch.hslu.swde.wda.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name= "city")
public class City implements Serializable{

	
	private static final long serialVersionUID = 2514335275204941450L;
	
	@Id
	private int zipCode;
	
	private String name;
	private float longitude;
	private float latitude;
	
	public City() {
		
	}
	
	public City(int zipCode,String name, float longitude, float latitude ) {
		
		this.zipCode=zipCode;
		this.name=name;
		this.longitude=longitude;
		this.latitude=latitude;
			
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return zipCode;
	}
	
	
	@Override
	public String toString() {
		return "City [ZIP Code=" + zipCode + ", Name=" + name + ", Latitude=" + latitude + ", Longitude=" + longitude + "]";
	}
	
	
}
