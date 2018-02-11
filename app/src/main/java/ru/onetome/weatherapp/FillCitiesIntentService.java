package ru.onetome.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class FillCitiesIntentService extends IntentService {
    public static final String INTENT_KEY = "intent_key";
    private final String TAG = "FillCitiesIntentService";
    private SQLiteDatabase db;
    private DataBaseManager dbManager;

    public FillCitiesIntentService() {
        super("FillCitiesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            City city;
            ArrayList<String> cityJson = intent.getStringArrayListExtra(INTENT_KEY);
            StringBuilder builder = new StringBuilder();
            Long aLong = System.currentTimeMillis();
            builder.append("INSERT INTO CITIES (");
            builder.append("CITY_NAME").append(",");
            builder.append("CITY_ID").append(",");
            builder.append("CITY_COUNTRY").append(",");
            builder.append("CITY_LON").append(",");
            builder.append("CITY_LAT");
            builder.append(") VALUES ");
            for (String s : cityJson) {
                city = new Gson().fromJson(s, City.class);
                builder.append('(');
                builder.append('"').append(city.getCityName().toUpperCase()).append("\",");
                builder.append('"').append(city.getCityID()).append("\",");
                builder.append('"').append(city.getCityCountry()).append("\",");
                builder.append('"').append(city.getCityLon()).append("\",");
                builder.append('"').append(city.getCityLat()).append("\"");
                builder.append(')').append(',');
            }
            builder.deleteCharAt(builder.length() - 1);
            Log.i(TAG, builder.toString());
            db.execSQL(builder.toString());
            dbManager.citiesTableFilledTest();
            aLong = System.currentTimeMillis() - aLong;
            Log.e(TAG, "onHandleIntent: " + aLong);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");
        if (db == null) {
            dbManager = new DataBaseManager(this);
            db = dbManager.getWritableDatabase();
            db.execSQL("DELETE FROM CITIES");
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
