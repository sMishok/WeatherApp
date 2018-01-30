package ru.onetome.weatherapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;

public class SelectCountryDialogFragment extends DialogFragment {
    private static final String TAG = "logSelectCountry";
    private MainActivity activity;
    private int cityID;
    private CharSequence[] countries;
    private List<City> cities;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(getString(R.string.select_country));
        builder.setIcon(R.drawable.ic_city);
        builder.setItems(countries, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cityID = cities.get(which).getCityID();
                Log.i(TAG, "CityID: " + cityID);
                activity.setCityID(cityID);
                activity.setWeatherInfoFragment();
            }
        });
        return builder.create();
    }

    public void setCountries(List<City> cities) {
        this.cities = cities;
        countries = new CharSequence[cities.size()];
        for (int i = 0; i < countries.length; i++) {
            countries[i] = "Country: " + cities.get(i).cityCountry + " City coordinates: ("
                    + cities.get(i).getCityLat().toString() + "°, " + cities.get(i).getCityLon().toString() + "°)";
        }
    }
}

