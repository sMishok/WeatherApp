package ru.onetome.weatherapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final String KEY_CITY_CHOICE = "key_city_choice";
    private static final String INFO_FRAGMENT_TAG = "weather_details_fragment";
    private static final String FIND_CITY_FRAGMENT_TAG = "find_city_fragment";
    //    private static final String ICONNAME = "icon.png";
    public StorageManager storageManager;
    public DataBaseManager dbManager;
    public SQLiteDatabase database;
    private String cityName;
    private int cityID;
//    private ImageView headerImage;

    public List<WeatherMap> getLastCities() {
        return dbManager.getLastCities();
    }

    public void setCity(String city) {
        cityName = city;
        cityID = dbManager.getCityID(city);
        Log.i(TAG, "city: " + city);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageManager = new StorageManager(MainActivity.this);
        storageManager.getCitiesList();
        dbManager = new DataBaseManager(this);
//        database = dbManager.getWritableDatabase();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setColorFilter(Color.argb(255, 255, 255, 255));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        View headerLayout = navigationView.getHeaderView(0);
        ImageView headerImage = headerLayout.findViewById(R.id.header_imageView);
        Bitmap bitmap;
        if ((bitmap = storageManager.loadIconPublic(this)) != null) {
            headerImage.setImageBitmap(bitmap);
        } else {
            headerImage.setImageResource(R.drawable.homer);
        }
        if (savedInstanceState != null) {
            Log.i(TAG, "Recovery instance state");
        } else {
            WeatherInfoFragment infoFragment = new WeatherInfoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, infoFragment, INFO_FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.find_city_drawer:
                showInputDialog();
                return true;
            case R.id.last_cities_drawer:
                showLastCities();
                return true;
            case R.id.share_drawer:
                shareWeatherInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int optionId;
        DrawerLayout drawer;

        switch (item.getItemId()) {
            case R.id.find_city_drawer:
                showInputDialog();
                drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.last_cities_drawer:
                showLastCities();
                drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.share_drawer:
                shareWeatherInfo();
                drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.about_drawer:
                showAboutFragment();
                drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            default:
                optionId = R.layout.activity_main;
        }

        ViewGroup parent = findViewById(R.id.content);
        parent.removeAllViews();
        View newContent = getLayoutInflater().inflate(optionId, parent, false);
        parent.addView(newContent);

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showInputDialog() {
        FindCityDialogFragment findCityFragment = new FindCityDialogFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        findCityFragment.show(transaction, FIND_CITY_FRAGMENT_TAG);
    }

    public void setWeatherInfoFragment() {
        storageManager.setCity(cityID);
        WeatherInfoFragment infoFragment = (WeatherInfoFragment) getSupportFragmentManager().findFragmentByTag(INFO_FRAGMENT_TAG);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        infoFragment.changeCity(cityID);
        transaction.replace(R.id.fragment_container, infoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showAboutFragment() {
        AboutFragment aboutFragment = new AboutFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, aboutFragment);
        transaction.commit();
    }

    private void showLastCities() {
        CitiesListFragment listFragment = new CitiesListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, listFragment);
        transaction.commit();
    }

    private void shareWeatherInfo() {
        WeatherInfoFragment infoFragment = (WeatherInfoFragment) getSupportFragmentManager().findFragmentByTag(INFO_FRAGMENT_TAG);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, infoFragment.getWeatherInfo());
        String chooserTitle = getString(R.string.chooser_title);
        Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
        startActivity(chosenIntent);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.find_city_drawer:
                        showInputDialog();
                        return true;
                    case R.id.last_cities_drawer:
                        showLastCities();
                        return true;
                    case R.id.share_drawer:
                        shareWeatherInfo();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_main);
        popup.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_CITY_CHOICE, cityName);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart");
        super.onRestart();
    }
}
