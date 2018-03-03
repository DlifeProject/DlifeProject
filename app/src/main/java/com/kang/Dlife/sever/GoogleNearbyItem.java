package com.kang.Dlife.sever;

import java.io.Serializable;

public class GoogleNearbyItem implements Serializable {

	public String name;
	public String placeID ;
	public double latitude;
	public double longitude;
	public GoogleNearbyItem(String name, String placeID, double latitude, double longitude) {
		super();
		this.name = name;
		this.placeID = placeID;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaceID() {
		return placeID;
	}

	public void setPlaceID(String placeID) {
		this.placeID = placeID;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


}
