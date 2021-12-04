package ch.hslu.swde.wda.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "city")
public class City implements Serializable {

	private static final long serialVersionUID = 2514335275204941450L;

	@Id
	private int zipCode;
	private String name;

	public City() {

	}

	public City(int zipCode, String name) {

		this.zipCode = zipCode;
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getZIPCode() {
		return zipCode;
	}

	public void setZIP(int zipcode) {
		this.zipCode = zipcode;
	}

	@Override
	public String toString() {
		return "City [ZIP Code=" + zipCode + ", Name=" + name +"]";
	}

}
