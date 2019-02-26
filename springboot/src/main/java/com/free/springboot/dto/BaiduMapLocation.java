package com.free.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaiduMapLocation {
	
	@JsonProperty("lon")
	private double longitude;
	
	@JsonProperty("lat")
	private double latitude;

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
