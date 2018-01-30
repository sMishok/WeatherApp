package ru.onetome.weatherapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "weatherDB";
    private static final int DB_VERSION = 1;
    private static final String DBMANAGER_TAG = "logDB";
    private final MainActivity activity;

    public DataBaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        activity = (MainActivity) context;
        Log.i(DBMANAGER_TAG, "DBManager created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DBMANAGER_TAG, "DBManager OnCreate started");
        db.execSQL("CREATE TABLE CITIES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CITY_NAME TEXT, "
                + "CITY_ID INTEGER  UNIQUE, "
                + "CITY_COUNTRY TEXT, "
                + "CITY_LON REAL, "
                + "CITY_LAT REAL);");

        Log.i(DBMANAGER_TAG, "Cities table created");

        db.execSQL("CREATE TABLE LAST_CITIES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CITY TEXT, "
                + "CITY_ID INTEGER  UNIQUE, "
                + "CITY_ICON TEXT, "
                + "CITY_TEMP REAL, "
                + "CITY_WEATHER TEXT, "
                + "CITY_HUMIDITY INTEGER , "
                + "CITY_PRESSURE REAL, "
                + "CITY_WIND REAL);");

        Log.i(DBMANAGER_TAG, "Last Cities table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertLastCity(WeatherMap map) {
        new InsertLastCity().execute(map);
    }

    public void insertCities(City[] cities) {
        new InsertCities().execute(cities);
    }

    public List<WeatherMap> getLastCities() {
        return new LoadLastCities(activity).loadInBackground();
    }


    public List<City> getCityID(String city) {
        return new LoadCityID(activity, city).loadInBackground();
    }

    public boolean citiesTableFilled() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.query("CITIES", null, null, null, null, null, null);
            int citiesCount = cursor.getCount();
            cursor.close();
            return citiesCount > 209000;
        } catch (SQLiteException e) {
            Log.i(DBMANAGER_TAG, "GetRowCountException: " + e.toString());
            return false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertCities extends AsyncTask<City, Void, Boolean> {
        ContentValues cityValues;

        @Override
        protected void onPreExecute() {
            cityValues = new ContentValues();
        }

        @Override
        protected Boolean doInBackground(City... cities) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                for (int i = 0; i < cities.length; i++) {
                    String cityName = cities[i].getCityName().toUpperCase();
                    cityValues.put("CITY_NAME", cityName);
                    cityValues.put("CITY_ID", cities[i].getCityID());
                    cityValues.put("CITY_COUNTRY", cities[i].getCityCountry());
                    cityValues.put("CITY_LON", cities[i].getCityLon());
                    cityValues.put("CITY_LAT", cities[i].getCityLat());
                    db.insertOrThrow("CITIES", null, cityValues);
                    Log.i(DBMANAGER_TAG, "City inserted: " + cityName + " " + cities[i].getCityID());
                }
                db.close();
                return true;
            } catch (SQLiteException e) {
                Log.i(DBMANAGER_TAG, "InsertCitiesException: " + e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                Log.i(DBMANAGER_TAG, "Cities inserted");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertLastCity extends AsyncTask<WeatherMap, Void, Boolean> {
        ContentValues cityValues;

        @Override
        protected void onPreExecute() {
            cityValues = new ContentValues();
        }

        @Override
        protected Boolean doInBackground(WeatherMap... map) {
            SQLiteDatabase db = getWritableDatabase();
            WeatherMap city = map[0];
            try {
                cityValues.put("CITY", city.getName());
                cityValues.put("CITY_ID", city.getId());
                cityValues.put("CITY_ICON", city.getWeatherIcon());
                cityValues.put("CITY_TEMP", city.getMainTemp());
                cityValues.put("CITY_WEATHER", city.getWeatherDescription());
                cityValues.put("CITY_HUMIDITY", city.getMainHumidity());
                cityValues.put("CITY_PRESSURE", city.getMainPressure());
                cityValues.put("CITY_WIND", city.getWindSpeed());
                db.insertOrThrow("LAST_CITIES", null, cityValues);
                db.close();
                return true;
            } catch (SQLiteException e) {
                Log.i(DBMANAGER_TAG, "InsertLastCityException: " + e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                Log.i(DBMANAGER_TAG, "Last_City inserted");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadLastCities extends AsyncTaskLoader {

        SQLiteDatabase db;
        List<WeatherMap> maps;


        public LoadLastCities(@NonNull Context context) {
            super(context);
            db = getReadableDatabase();
            maps = new ArrayList<>();
        }

        @Override
        public List<WeatherMap> loadInBackground() {
            Cursor cursor = db.query("LAST_CITIES", new String[]{"_id", "CITY", "CITY_ID", "CITY_TEMP", "CITY_WEATHER"},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                String cityName = cursor.getString(1);
                Integer cityID = cursor.getInt(2);
                Float cityTemp = cursor.getFloat(3);
                String cityWeather = cursor.getString(4);
                WeatherMap map = new WeatherMap(cityName, cityID, cityTemp, cityWeather);
                maps.add(0, map);
            }
            db.close();
            cursor.close();
            return maps;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadCityID extends AsyncTaskLoader {

        SQLiteDatabase db;
        List<City> cities;
        String cityName;

        public LoadCityID(@NonNull Context context, String cityName) {
            super(context);
            db = getReadableDatabase();
            cities = new ArrayList<>();
            this.cityName = cityName;
        }

        @Override
        public List<City> loadInBackground() {
            Cursor cursor = db.query("CITIES", new String[]{"_id", "CITY_NAME", "CITY_ID", "CITY_COUNTRY"},
                    "CITY_NAME = ?", new String[]{cityName}, null, null, null);
            while (cursor.moveToNext()) {
                String cityName = cursor.getString(1);
                Integer cityID = cursor.getInt(2);
                String cityCountry = cursor.getString(3);
                City city = new City(cityName, cityID, cityCountry);
                cities.add(0, city);
            }
            db.close();
            cursor.close();
            return cities;
        }
    }
}
