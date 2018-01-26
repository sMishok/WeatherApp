package ru.onetome.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "weatherDB";
    private static final int DB_VERSION = 1;
    private static final String DBMANAGER_TAG = "logDB";
    private final Context context;
    private List<String> cities;

    public DataBaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        Log.i(DBMANAGER_TAG, "DBManager created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DBMANAGER_TAG, "DBManager OnCreate started");
        db.execSQL("CREATE TABLE LAST_CITIES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CITY TEXT UNIQUE);");

        Log.i(DBMANAGER_TAG, "База создана");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCity(SQLiteDatabase db, String city) {

        try {
            ContentValues cityValues = new ContentValues();
            cityValues.put("CITY", city);
            db.insertOrThrow("LAST_CITIES", null, cityValues);
        } catch (SQLiteException e) {
            Log.i(DBMANAGER_TAG, city.toString() + " registered in the database");
        }
    }

    public List<String> getCities() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query("LAST_CITIES", new String[]{"_id", "CITY"},
                    null, null, null, null, null);
            cities = new ArrayList<>();
            while (cursor.moveToNext()) {
                String city = cursor.getString(1);
                cities.add(0, city);
            }
            cursor.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailuble", Toast.LENGTH_SHORT);
            toast.show();
        }
        return cities;
    }
}
