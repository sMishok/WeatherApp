package ru.onetome.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CitiesListFragment extends Fragment {

    private MainActivity activity;
    private List<WeatherMap> cities;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.fragment_cities_list, container, false);
        RecyclerView citiesRecyclerView = listView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        citiesRecyclerView.setLayoutManager(layoutManager);
        citiesRecyclerView.setAdapter(new MyAdapter());
        if (savedInstanceState == null) {
            cities = activity.getLastCities();
        }
        return listView;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cityNameTextView;
        private TextView countryNameTextView;
        private TextView weatherTempTextView;
        private TextView weatherDescriptionTextView;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.city_list_item, parent, false));
            itemView.setOnClickListener(this);
            cityNameTextView = itemView.findViewById(R.id.city_name_text_view);
            countryNameTextView = itemView.findViewById(R.id.country_name_text_view);
            weatherTempTextView = itemView.findViewById(R.id.temp_temp_text_view);
            weatherDescriptionTextView = itemView.findViewById(R.id.weather_description_text_view);
        }

        void bind(int position) {
            String cityName = cities.get(position).getName() + ", ";
            String countryName = cities.get(position).getSysCountry();
            String temp = cities.get(position).getMainTemp().toString() + " °C, ";
            String description = cities.get(position).getWeatherDescription();
            cityNameTextView.setText(cityName);
            countryNameTextView.setText(countryName);
            weatherTempTextView.setText(temp);
            weatherDescriptionTextView.setText(description);
        }

        @Override
        public void onClick(View view) {
            activity.setCityID(cities.get(this.getLayoutPosition()).getId());
            activity.setWeatherInfoFragment();
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }
    }
}
