package ru.onetome.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String STORAGE_NAME = "StorageName";
    private static final String ICONNAME = "icon.png";
    private static final String KEY = "key";
    //    private static final String MOSCOW = "PRVFRZ";
    private static final String MOSCOW = "null";
    private static final String TAG = "StorageManager";
    private static final int OFFSET = 3;
    private SharedPreferences sharedPreferences = null;
    private MainActivity activity;
    private City[] cities;


    public StorageManager(Context context) {
        sharedPreferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        saveIconPublic(context);
        activity = (MainActivity) context;
    }

    public int getCity() {
        String tmp = sharedPreferences.getString(KEY, MOSCOW);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tmp.length(); i++) {
            sb.append((char) (tmp.charAt(i) - OFFSET));
            Log.i(TAG, sb.toString());
        }
        int cityID = activity.dbManager.getCityID(sb.toString());
        return cityID;
    }

    public void setCity(String city) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < city.length(); i++) {
            sb.append((char) (city.charAt(i) + OFFSET));
            Log.i(TAG, sb.toString());
        }
        sharedPreferences.edit().putString(KEY, sb.toString()).apply();
    }

    public void getCitiesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = activity.getAssets().open("cities_list/city.list.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder(1024);
                    String tempData;
                    while ((tempData = reader.readLine()) != null) {
                        sb.append(tempData).append("\n");
                    }
                    reader.close();
                    is.close();
                    JSONArray jsonArray = new JSONArray(sb.toString());
                    List<City> citiesList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        City city = new Gson().fromJson(json.toString(), City.class);
                        citiesList.add(city);
                    }
                    City[] cities = citiesList.toArray(new City[citiesList.size()]);
                    activity.dbManager.insertCities(cities);
                    Log.i(TAG, "Cities" + cities.length);
                } catch (Exception e) {
                    Log.i(TAG, "City.list.json read problem" + e.toString());
                }
            }
        }).start();
    }

    public void saveIconPrivate(Context context) {
        File internalDir = context.getFilesDir();
        File dir;
        if (internalDir != null) {
            dir = new File(internalDir.getPath());
        } else {
            return;
        }
        boolean isFileExist = dir.exists() || dir.mkdirs();

        if (isFileExist) {
            File file = new File(dir, ICONNAME);
            Log.i(TAG, "file path is created: " + file.getAbsolutePath());
            onSaveIcon(context, file);
        }
    }

    public void saveIconPublic(Context context) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ICONNAME);
        Log.i(TAG, "file path is created: " + file.getAbsolutePath());
        if (!file.exists()) {
            Log.i(TAG, "file isn`t exist");
            onSaveIcon(context, file);
        }
    }

    public Bitmap loadIconPrivate(Context context) {
        File file = new File(context.getFilesDir(), ICONNAME);
        Log.i(TAG, "file path is loaded: " + file.getAbsolutePath());
        return onLoadIcon(context, file);
    }

    public Bitmap loadIconPublic(Context context) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ICONNAME);
        Log.i(TAG, "file path is loaded: " + file.getAbsolutePath());
        return onLoadIcon(context, file);
    }

    private void onSaveIcon(final Context context, final File file) {

        if (!isExternalStorageWritable(context)) {
            Log.i(TAG, "External storage not found (save)");
            return;
        }
        if (context.getResources().getDrawable(R.drawable.homer) != null) {
            try {
                FileOutputStream outputStream = new FileOutputStream(file, false);
                Bitmap bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.homer)).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
                Log.i(TAG, "file is saved: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap onLoadIcon(Context context, File file) {
        Bitmap icon = null;
        if (!isExternalStorageReadable(context)) {
            Log.i(TAG, "External storage not found (load)");
            return null;
        }
        if (!file.exists()) {
            Log.i(TAG, "Icon not found");
            return null;
        }
        try {

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            icon = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            Log.i(TAG, "file is loaded: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return icon;
    }

    private boolean isExternalStorageWritable(Context context) {
        String state = Environment.getExternalStorageState();
        Log.i(TAG, "ExternalStorageWritable: " + state);
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable(Context context) {
        String state = Environment.getExternalStorageState();
        Log.i(TAG, "ExternalStorageReadable: " + state);
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


}


//    ____________________________________________________

//    private static final String STORAGE_NAME = "StorageName";
//
//    private static SharedPreferences settings = null;
//    private static SharedPreferences.Editor editor = null;
//    private static Context context = null;
//
//    public static void init(Context context){
//        StorageManager.context = context;
//    }
//
//    private static void init(){
//        settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
//        editor = settings.edit();
//    }
//
//    public static void addStringProperty(String name, String value){
//        if( settings == null ){
//            init();
//        }
//        editor.putString(name, value);
//        editor.apply();
//    }
//
//    public static void addStringArrayProperty(String name, String value){
//        if( settings == null ){
//            init();
//        }
//        Set<String> values = getStringArrayProperty(name);
//        values.add(value);
//        editor.putStringSet(name, values);
//        editor.apply();
//    }
//
//    public static void addIntProperty(String name, int value){
//        if( settings == null ){
//            init();
//        }
//        editor.putInt(name, value);
//        editor.apply();
//    }
//
//    public static void addBooleanProperty(String name, boolean value){
//        if( settings == null ){
//            init();
//        }
//        editor.putBoolean(name, value);
//        editor.apply();
//    }
//
//    public static String getStringProperty(String name){
//        if( settings == null ){
//            init();
//        }
//        return settings.getString(name, null);
//    }
//
//    public static Set<String> getStringArrayProperty(String name){
//        if( settings == null ){
//            init();
//        }
//        return settings.getStringSet(name, null);
//    }
//
//    public static int getIntProperty(String name){
//        if( settings == null ){
//            init();
//        }
//        return settings.getInt(name, 0);
//    }
//
//    public static boolean getBooleanProperty(String name){
//        if( settings == null ){
//            init();
//        }
//        return settings.getBoolean(name, false);
//    }
//}

