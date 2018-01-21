package ru.onetome.weatherapp;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherInfoFragment extends Fragment {

    private static final String WIF_TAG = "WeatherInfoFragment";
    private final Handler handler = new Handler();
    private MainActivity activity;
    private Typeface weatherFont;
    private TextView weatherIcon;
    private TextView cityText;
    private TextView weatherInfoText;
    private TextView weatherDetailsText;


    private String weatherInfo;
    private String cityName;
    private String weatherDetails;
    private String updateOn;
    private String icon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/forecastfont.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.fragment_weather_info, container, false);
        weatherIcon = infoView.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        cityText = infoView.findViewById(R.id.city);
        weatherInfoText = infoView.findViewById(R.id.weather_info_text);
        weatherDetailsText = infoView.findViewById(R.id.weather_details_text);

        if (savedInstanceState != null) {
            Log.i(WIF_TAG, "Recovery instance state");
        } else {
            changeCity(activity.storageManager.getCity());
        }
        return infoView;
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(activity, city);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, activity.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        Log.d(WIF_TAG, "jspn " + jsonObject.toString());
        try {
            WeatherMap map = new Gson().fromJson(jsonObject.toString(), WeatherMap.class);
            cityName = map.getName().toUpperCase(Locale.US);
            cityText.setText(cityName);
            weatherInfo = map.getWeatherDescription().toUpperCase(Locale.US) + ", " + map.getMainTemp() + " °C";
            weatherInfoText.setText(weatherInfo);
            weatherDetails = "Humidity: " + map.getMainHumidity() + "%" + "\n"
                    + "Pressure: " + map.getMainPressure() + " hPa" + "\n"
                    + "Wind: " + map.getWindSpeed() + " mps";
            weatherDetailsText.setText(weatherDetails);
            DateFormat df = DateFormat.getDateTimeInstance();
            updateOn = df.format(new Date(jsonObject.getLong("dt") * 1000));

            setWeatherIcon(map.getWeatherId(), map.getSysSunrise() * 1000, map.getSysSunset() * 1000);
        } catch (Exception e) {
            Log.d(WIF_TAG, "fields data not found in json");
        }
    }

    private void setWeatherIcon(int code, long sunrise, long sunset) {
        int id = code / 100;
        if (code == 800) {
            long currentTime = new Date().getTime();
            Log.d(WIF_TAG, "Time: " + Long.toString(currentTime) + "\n"
                    + "Sunrise: " + sunrise + "\n"
                    + "Sunset: " + sunrise + "\n");
            if (currentTime >= sunrise && currentTime <= sunset) {
                icon = activity.getString(R.string.weather_sunny);
            } else {
                icon = activity.getString(R.string.weather_clear_night);
            }
        } else {
            Log.d(WIF_TAG, "id " + id);
            switch (id) {
                case 2:
                    icon = activity.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = activity.getString(R.string.weather_drizzle);
                    break;
                case 5:
                    icon = activity.getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = activity.getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = activity.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = activity.getString(R.string.weather_cloudy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }

    public String getWeatherInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(cityName).append("\n").append(weatherInfo);
        return sb.toString();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}
