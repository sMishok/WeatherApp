package ru.onetome.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherMap {

    @SerializedName("coord")
    CoordinateElement coordinate;
    @SerializedName("weather")
    List<WeatherElement> weather;
    @SerializedName("base")
    String base;
    @SerializedName("main")
    MainElement main;
    @SerializedName("visibility")
    Integer visibility;
    @SerializedName("wind")
    WindElement wind;
    @SerializedName("clouds")
    CloudsElement clouds;
    @SerializedName("dt")
    Integer dt;
    @SerializedName("sys")
    SysElement sys;
    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String name;
    @SerializedName("cod")
    String cod;

    public WeatherMap(String name, int id, float temp, String description) {
        this.name = name;
        this.id = id;
        main = new MainElement(temp);
        weather = new ArrayList<>();
        weather.add(new WeatherElement(description));
    }

    public Float getLon() {
        return coordinate.lon;
    }

    public Float getLat() {
        return coordinate.lat;
    }

    public Integer getWeatherId() {
        return weather.get(0).id;
    }

    public String getWeatherMain() {
        return weather.get(0).main;
    }

    public String getWeatherDescription() {
        return weather.get(0).description;
    }

    public String getWeatherIcon() {
        return weather.get(0).icon;
    }

    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + weather.get(0).icon + ".png";
    }

    public String getBase() {
        return base;
    }

    public Float getMainTemp() {
        return main.temp;
    }

    public Float getMainPressure() {
        return main.pressure;
    }

    public Integer getMainHumidity() {
        return main.humidity;
    }

    public Float getMainTempMin() {
        return main.temp_min;
    }

    public Float getMainTempMax() {
        return main.temp_max;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public Float getWindSpeed() {
        return wind.speed;
    }

    public Float getWindDeg() {
        return wind.deg;
    }

    public Integer getCloudsAll() {
        return clouds.all;
    }

    public Integer getDt() {
        return dt;
    }

    public Integer getSysType() {
        return sys.type;
    }

    public Float getSysId() {
        return sys.id;
    }

    public Float getSysMessage() {
        return sys.message;
    }

    public String getSysCountry() {
        return sys.country;
    }

    public Integer getSysSunrise() {
        return sys.sunrise;
    }

    public Integer getSysSunset() {
        return sys.sunset;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCod() {
        return cod;
    }

    private class CoordinateElement {
        @SerializedName("lon")
        Float lon;
        @SerializedName("lat")
        Float lat;
    }

    private class WeatherElement {
        @SerializedName("id")
        Integer id;

        @SerializedName("main")
        String main;

        @SerializedName("description")
        String description;

        @SerializedName("icon")
        String icon;

        public WeatherElement(String description) {
            this.description = description;
        }
    }

    private class MainElement {
        @SerializedName("temp")
        Float temp;
        @SerializedName("pressure")
        Float pressure;
        @SerializedName("humidity")
        Integer humidity;
        @SerializedName("temp_min")
        Float temp_min;
        @SerializedName("temp_max")
        Float temp_max;

        public MainElement(Float temp) {
            this.temp = temp;
        }
    }

    private class WindElement {
        @SerializedName("speed")
        Float speed;
        @SerializedName("deg")
        Float deg;
    }

    private class CloudsElement {
        @SerializedName("all")
        Integer all;
    }

    private class SysElement {
        @SerializedName("type")
        Integer type;
        @SerializedName("id")
        Float id;
        @SerializedName("message")
        Float message;
        @SerializedName("country")
        String country;
        @SerializedName("sunrise")
        Integer sunrise;
        @SerializedName("sunset")
        Integer sunset;
    }
}

