package ru.onetome.weatherapp;

import android.content.Context;
import android.os.Bundle;
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
    //    private CitiesListListener citiesListListener;
    private List<String> cities;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        citiesListListener = (CitiesListListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.fragment_cities_list, container, false);
        RecyclerView citiesRecyclerView = listView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        citiesRecyclerView.setLayoutManager(layoutManager);
        citiesRecyclerView.setAdapter(new MyAdapter());
        if (savedInstanceState != null) {
        } else {
            cities = activity.getCities();
        }
        return listView;
    }


    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView categoryNameTextView;


        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.category_list_item, parent, false));
            itemView.setOnClickListener(this);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.category_name_text_view);
        }

        void bind(int position) {
            String category = cities.get(position);
            categoryNameTextView.setText(category);
        }

        @Override
        public void onClick(View view) {
            activity.setCity(cities.get(this.getLayoutPosition()));
            activity.setWeatherInfoFragment();
//            showWeatherInfoFragment(this.getLayoutPosition());
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

//    private void showWeatherInfoFragment(int categoryId) {
//        citiesListListener.onListItemClick(categoryId);
//    }


}