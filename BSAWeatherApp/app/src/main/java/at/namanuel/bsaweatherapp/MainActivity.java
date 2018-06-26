package at.namanuel.bsaweatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {




    private static ListAdapter mAdapter;
    private static final String LOG_TAG =
            MainActivity.class.getCanonicalName();

    private static final String PREFERENCE_KEY = "weather";

    public static final String EXTRA_MESSAGE = "DetailsData";

    private static List<Weather> weather;
    private String unit = "metric";
    private String city = "vienna";
    public  static RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean visible = sharedPreferences.getBoolean(
                getString(R.string.settings_key),
                getResources().getBoolean(R.bool.settings_metrisch)
        );
        String city_new = sharedPreferences.getString(getString(R.string.pref_key),"");



        if (visible){
            unit = "metric";
            city = city_new;
        }else{
            unit="imperial";
        }

        OpenWeatherMap openWeatherMap = new OpenWeatherMap(unit, city);


        mAdapter = new ListAdapter(null);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ListAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Weather item) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_MESSAGE, item);
                startActivity(intent);
            }
        });

    }

    public static void ListData(List<Weather> w) {
        if (w != null) {
            //weatherData = w;
            //weather = w.getList();
            weather = w;
            mAdapter.swapData(weather);
        } else {
            //hier gechachte Daten laden
            Log.d(LOG_TAG, "no List loaded");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(itemId == R.id.action_neuladen){
            OpenWeatherMap openWeatherMap = new OpenWeatherMap(unit, city);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_key))) {
            boolean visible = sharedPreferences.getBoolean(
                    getString(R.string.settings_key),
                    getResources().getBoolean(R.bool.settings_metrisch)
            );
        }
    }
}
