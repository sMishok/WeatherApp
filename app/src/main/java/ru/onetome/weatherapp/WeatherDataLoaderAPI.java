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
        Call<WeatherMap> getWeather(@Query("q") String city,
                                    @Query("units") String units,
                                    @Query("appid") String id);
    }

    //       if (api == null){
//           api = retrofit.create(WeatherAPI.class);
//       }
//       Call<WeatherMap> callWeather = api.getWeather(city, UNITS, OPEN_WEATHER_MAP_API_ID);
//       try {
//           callWeather.execute();
//           weatherRenderFragment.renderWeather()
//       } catch (IOException e) {
//           Log.d(WDLTag, "Responce exception: " + e.toString());
//       }
//       callWeather.enqueue(new Callback<WeatherMap>() {
//           @Override
//           public void onResponse(Call<WeatherMap> call, Response<WeatherMap> response) {
//
//               if (response.isSuccessful()){
//                   Log.d(WDLTag, "onResponse: " + response.toString());
//                   weatherMap = response.body();
//               } else
//                   Log.e(WDLTag, "Response is not receive");
//           }
//
//           @Override
//           public void onFailure(Call<WeatherMap> call, Throwable t) {
//               Log.e(WDLTag, "onFailure: " + t.toString());
//           }
//       });
//       Log.d(WDLTag, "retrofit finished");
//       return weatherMap;
//   }


//    static JSONObject getJSONData(Context context, String city) {
//        try {
//            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.addRequestProperty(KEY, OPEN_WEATHER_MAP_API_ID);
//
//            BufferedReader reader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
//            StringBuilder tempData = new StringBuilder(1024);
//            String tempString = "";
//            while ((tempString = reader.readLine()) != null) {
//                tempData.append(tempString).append(NEW_LINE);
//            }
//            reader.close();
//
//            JSONObject jsonObject = new JSONObject(tempData.toString());
//            if (jsonObject.getInt(RESPONSE) != SERVER_MESSAGE) {
//                Log.e(WDLTag, "Json is not receive");
//                return null;
//
//            }
//            return jsonObject;
//        } catch (Exception e) {
//            Log.e(WDLTag, "Exception in the getJSONData()" + e.toString());
//            return null;
//        }
//    }
}

