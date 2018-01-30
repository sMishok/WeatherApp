package ru.onetome.weatherapp;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    Integer cityID;
    @SerializedName("name")
    String cityName;
    @SerializedName("country")
    String cityCountry;
    @SerializedName("coord")
    CityCoordinate coordinate;


    public City(String cityName, Integer cityID, String cityCountry, Float lon, Float lat) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.cityCountry = cityCountry;
        coordinate = new CityCoordinate(lon, lat);
    }

    public Integer getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public Float getCityLon() {
        return coordinate.lon;
    }

    public Float getCityLat() {
        return coordinate.lat;
    }

    private class CityCoordinate {
        @SerializedName("lon")
        Float lon;
        @SerializedName("lat")
        Float lat;

        public CityCoordinate(Float lon, Float lat) {
            this.lon = lon;
            this.lat = lat;
        }
    }
}
