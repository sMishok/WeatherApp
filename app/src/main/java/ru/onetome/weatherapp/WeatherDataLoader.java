package ru.onetome.weatherapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataLoader {

    private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API_ID = "192d773d7b8e762a18b8960246199be5";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int SERVER_MESSAGE = 200;
    private static final String WDLTag = "WeatherDataLoader";

    static JSONObject getJSONData(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, OPEN_WEATHER_MAP_API_ID);

            BufferedReader reader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
            StringBuilder tempData = new StringBuilder(1024);
            String tempString = "";
            while ((tempString = reader.readLine()) != null) {
                tempData.append(tempString).append(NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(tempData.toString());
            if (jsonObject.getInt(RESPONSE) != SERVER_MESSAGE) {
                Log.e(WDLTag, "Json is not receive");
                return null;

            }
            return jsonObject;
        } catch (Exception e) {
            Log.e(WDLTag, "Exception in the getJSONData()" + e.toString());
            return null;
        }
    }
}

