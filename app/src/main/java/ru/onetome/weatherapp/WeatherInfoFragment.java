package ru.onetome.weatherapp;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherInfoFragment extends Fragment {

    private static final String WIF_TAG = "WeatherInfoFragment";
    //    @BindView(R.id.weather_icon) TextView weatherIcon;
    @BindView(R.id.weather_image_icon)
    ImageView weatherImageIcon;
    @BindView(R.id.city)
    TextView cityText;
    @BindView(R.id.weather_temp)
    TextView tempText;
    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;
    @BindView(R.id.weather_details_text)
    TextView weatherDetailsText;
    private MainActivity activity;
    //    private final Handler handler = new Handler();
    private WeatherDataLoaderAPI.WeatherAPI api;
    private Typeface weatherFont;
    private String weatherInfo;
    private String weatherTemp;
    private String cityName;
    private String weatherDetails;
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
        ButterKnife.bind(this, infoView);
//        weatherIcon.setTypeface(weatherFont);
        if (savedInstanceState == null) {
            changeCity(activity.storageManager.getCity());
        }
        return infoView;
    }

    private void updateWeatherData(final int cityID) {
        api = WeatherDataLoaderAPI.getClient().create(WeatherDataLoaderAPI.WeatherAPI.class);
        Call<WeatherMap> callWeather = api.getWeather(cityID, WeatherDataLoaderAPI.getUNITS(), WeatherDataLoaderAPI.getOpenWeatherMapApiId());
        callWeather.enqueue(new Callback<WeatherMap>() {
            @Override
            public void onResponse(Call<WeatherMap> call, Response<WeatherMap> response) {
                if (response.isSuccessful()) {
                    WeatherMap weatherMap = response.body();
                    Log.d(WIF_TAG, "onResponse: " + response.toString());
                    renderWeather(weatherMap);
                    activity.dbManager.insertLastCity(weatherMap);
                } else
                    Log.e(WIF_TAG, "Response is not receive");
            }

            @Override
            public void onFailure(Call<WeatherMap> call, Throwable t) {
                Log.e(WIF_TAG, "onFailure: " + t.toString());
            }
        });
    }

    public void renderWeather(WeatherMap map) {
        try {
            cityName = map.getName().toUpperCase(Locale.US);
            cityText.setText(cityName);
            weatherTemp = map.getMainTemp() + " °C";
            tempText.setText(weatherTemp);
            weatherInfo = map.getWeatherDescription().toUpperCase(Locale.US);
            weatherInfoText.setText(weatherInfo);
            weatherDetails = "Humidity: " + map.getMainHumidity() + "%" + "\n"
                    + "Pressure: " + map.getMainPressure() + " hPa" + "\n"
                    + "Wind: " + map.getWindSpeed() + " mps";
            weatherDetailsText.setText(weatherDetails);
            Glide.with(activity).load(map.getIconUrl()).into(weatherImageIcon);


//            DateFormat df = DateFormat.getDateTimeInstance();
//            setWeatherIcon( map.getWeatherId(), map.getSysSunrise() * 1000, map.getSysSunset() * 1000);
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
            Log.d(WIF_TAG, "id: " + id);
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
//        weatherIcon.setText(icon);
    }

    public void changeCity(int cityID) {
        Log.i(WIF_TAG, "changeCity: " + cityID);
        updateWeatherData(cityID);
    }

    public String getWeatherInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(cityName).append("\n").append(weatherInfo);
        return sb.toString();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}
