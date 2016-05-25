package com.ismeralda_fabio.worldcityguide.model;

public class Place {

	String icon;
	String id;
	String name;
	String vicinity;
	String place_id;
	String reference;
	String photo_ref;
	Geometry geometry;




	public Place(String icon, String id, String name, String vicinity,
			String reference,String place_id, Geometry geometry) {
		
		this.icon = icon;
		this.id = id;
		this.name = name;
		this.vicinity = vicinity;
		this.reference = reference;
		this.place_id = place_id;
		this.geometry = geometry;

	}

	public Place(String icon, String name, String vicinity, String reference, Geometry geometry) {
		this.icon = icon;
		this.name = name;
		this.vicinity = vicinity;
		this.reference = reference;
		this.geometry = geometry;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto_ref() {
		return photo_ref;
	}

	public void setPhoto_ref(String photo_ref) {
		this.photo_ref = photo_ref;
	}


	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}




	public static class Geometry {

		public Location location;

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

	}

	public static class Location {

		public double lat;

		public double lng;

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

	}

}
