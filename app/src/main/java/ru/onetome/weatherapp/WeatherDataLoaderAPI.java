package ru.onetome.weatherapp;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherDataLoaderAPI {

    private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/data/2.5/";
    private static final String OPEN_WEATHER_MAP_API_ID = "192d773d7b8e762a18b8960246199be5";
    private static final String UNITS = "metric";
    private static final String WDLTag = "WeatherDataLoader";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(OPEN_WEATHER_MAP_API)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static String getOpenWeatherMapApiId() {
        return OPEN_WEATHER_MAP_API_ID;
    }

    public static String getUNITS() {
        return UNITS;
    }

    interface WeatherAPI {
        @GET("weather")
        Call<WeatherMap> getWeather(@Query("id") int cityID,
                                    @Query("units") String units,
                                    @Query("appid") String id);
    }
}