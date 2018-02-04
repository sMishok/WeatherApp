package ru.onetome.weatherapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class FillCitiesIntentService extends IntentService {

    private final String TAG = "FillCitiesIntentService";
    private SQLiteDatabase db;

    public FillCitiesIntentService() {
        super("FillCitiesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String cityJson = intent.getStringExtra(StorageManager.INTENT_KEY);
            City city = new Gson().fromJson(cityJson, City.class);
            insertCity(city);

//            cities[index++] = city;
//            if (index == cities.length){
//                insertCities(cities);
//                index = 0;
//                cities = new City[10];
//            }
        }
    }

    private void insertCity(City city) {
        ContentValues cityValues = new ContentValues();
        try {
            String cityName = city.getCityName().toUpperCase();
            cityValues.put("CITY_NAME", cityName);
            cityValues.put("CITY_ID", city.getCityID());
            cityValues.put("CITY_COUNTRY", city.getCityCountry());
            cityValues.put("CITY_LON", city.getCityLon());
            cityValues.put("CITY_LAT", city.getCityLat());
            db.insertOrThrow("CITIES", null, cityValues);
            Log.i(TAG, "City inserted: " + cityName + " " + city.getCityID());
        } catch (SQLiteException e) {
            Log.i(TAG, "InsertCitiesException: " + e.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");
        if (db == null) {
            db = new DataBaseManager(this).getWritableDatabase();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        db.close();
        Log.w(TAG, "onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}
