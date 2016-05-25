package com.ismeralda_fabio.worldcityguide.model;

public class UserLocation {

	double lat;
	double lng;

	public UserLocation(double lat, double lng) {

		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public String getUserLoc()
	{
		return String.valueOf(getLat()) + "," + String.valueOf(getLng());
	}

}
