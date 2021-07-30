package com.xjakubs.nasa.meteorite;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Meteorite implements Serializable, Comparable {
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getNametype() {
		return nametype;
	}

	public String getRecclass() {
		return recclass;
	}

	public int getMass() {
		try {
			return Integer.parseInt(mass);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public String getFall() {
		return fall;
	}

	public int getYear() {
		try {
			return Integer.parseInt(year.substring(0, 4));
		} catch (Exception e) {
			return 0;
		}
	}

	public String getReclat() {
		return reclat;
	}

	public String getReclong() {
		return reclong;
	}

	public Geolocation getGeolocation() {
		return geolocation;
	}

	@SerializedName("name")
	private String name;
	@SerializedName("id")
	private String id;
	@SerializedName("nametype")
	private String nametype;
	@SerializedName("recclass")
	private String recclass;
	@SerializedName("mass")
	private String mass;
	@SerializedName("fall")
	private String fall;
	@SerializedName("year")
	private String year;
	@SerializedName("reclat")
	private String reclat;
	@SerializedName("reclong")
	private String reclong;
	@SerializedName("geolocation")
	private Geolocation geolocation;

	@Override
	public int compareTo(Object o) {
		int comparemass = ((Meteorite) o).getMass();
		return comparemass - this.getMass();
	}


	public class Geolocation implements Serializable {
		@SerializedName("type")
		private String type;

		public String getType() {
			return type;
		}
//		@SerializedName("coordinates")
//		private String coordinates;
	}
}


