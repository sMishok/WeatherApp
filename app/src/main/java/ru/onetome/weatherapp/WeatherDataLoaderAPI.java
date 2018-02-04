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
    private static final String TAG = "WeatherDataLoader";
    private static Retrofit retrofit = null;
    private static Retrofit citiesRetrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(OPEN_WEATHER_MAP_API)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

//    public static Retrofit getCitiesClient() {
//        if (citiesRetrofit== null) {
//            citiesRetrofit = new Retrofit.Builder().baseUrl(OPEN_WEATHER_MAP_CITIES_API)
//                    .addConverterFactory(GsonConverterFactory.create()).build();
//        }
//        return citiesRetrofit;
//    }

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

        @GET("weather")
        Call<WeatherMap> getWeatherByName(@Query("q") String cityName,
                                          @Query("units") String units,
                                          @Query("appid") String id);
    }
}