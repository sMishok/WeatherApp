package ru.onetome.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageManager {
    private static final String STORAGE_NAME = "StorageName";
    private static final String KEY = "key";
    private static final String MOSCOW = "Moscow";
    private SharedPreferences sharedPreferences = null;


    public StorageManager(Context context) {
        sharedPreferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public String getCity() {
        return sharedPreferences.getString(KEY, MOSCOW);
    }

    public void setCity(String city) {
        sharedPreferences.edit().putString(KEY, city).apply();
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

