package at.namanuel.bsaweatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMap {

    private final String api_key = "e9b3e40b1174e2215b4d761f665ef1b5";
    private String city;
    private String unit;

    private static final String LOG_TAG =
            MainActivity.class.getCanonicalName();

    //public List<String> data = new LinkedList<>();
    private List<Weather> weather;


    public OpenWeatherMap(String unit, String city) {
        this.city = city;
        this.unit = unit;
        loadWebResult();
    }

    private void loadWebResult() {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&units="+unit+"&APPID=" + api_key);
            new loadWebContentTask().execute(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException.", e);
        }
    }

    private String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("GET");
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext())
                return scanner.next();
            else
                return null;

        } finally {
            urlConnection.disconnect();
        }
    }

    private class loadWebContentTask extends AsyncTask<URL, Void, List<Weather>> {
        @Override
        protected List<Weather> doInBackground(URL... urls) {
            URL url = urls[0];
            String resultString = null;
            String icon = "";
            String description = "";
            int condition_id = 0;
            weather = new ArrayList<>();
            try {
                resultString = getResponseFromHttpURL(url);
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd,yyyy KK:mm:ss a");

                JSONObject jsonRoot = new JSONObject(resultString);
                JSONArray list_weather = jsonRoot.getJSONArray("list");
                for (int i = 0; i < list_weather.length(); i++) {
                    JSONObject entry = list_weather.getJSONObject(i); //DATUM; ICON KÜRZEL; TEMPERATUR IN °C
                    //TEMPERATUR mit 2 Dezimalstellen
                    JSONObject main_weather = entry.getJSONObject("main");
                    double temperatur = main_weather.getDouble("temp");
                    double pressure = main_weather.getDouble("pressure");
                    double humidity = main_weather.getDouble("humidity");

                    JSONObject cl = entry.getJSONObject("clouds");
                    double clouds = cl.getDouble("all");

                    JSONObject w = entry.getJSONObject("wind");
                    double windSpeed = w.getDouble("speed");
                    double windDir = w.getDouble("deg");

                    String temp_text = "";
                    if (unit.equals("metric")) {
                        temp_text = String.format("%.2f", temperatur) + " °C";
                    }else if(unit.equals("imperial")){
                        temp_text = String.format("%.2f", temperatur) + " °F";
                    }

                    //ICON aus dem Wetter_Array bekommen
                    JSONArray weather_data = entry.getJSONArray("weather");
                    for(int j=0; j < weather_data.length(); j++) {
                        JSONObject icon_data = weather_data.getJSONObject(j);
                        icon = icon_data.getString("icon");
                        description = icon_data.getString("description");
                        condition_id = icon_data.getInt("id");
                    }
                    //REGEN
                    double rain = 0;
                    if(entry.has("rain")) {
                        JSONObject rain_data = entry.getJSONObject("rain");


                        if (rain_data.has("3h")) {
                            rain = rain_data.getDouble("3h")*100;
                        }
                    }

                    //SCHNEE
                    double snow = 0;
                    if(entry.has("snow")){
                        JSONObject snow_data = entry.getJSONObject("snow");

                        if(snow_data.has("3h")){
                            snow = snow_data.getDouble("3h")*100;
                        }
                    }

                    //DATUM

                    Date date = dateformat.parse(entry.getString("dt_txt"));

                    String date_new = (new SimpleDateFormat("EEEE,dd. MMMM yyyy KK:mm a").format(date));

                    // date, icon, description, temp, pressure, humidity, clouds,  windSpeed, windDirection, rain
                    //AUSGABE

                    Weather weatherObject = new Weather(date_new, icon, description, temp_text, pressure, humidity, clouds, windSpeed, windDir, rain, snow, condition_id);
                    //weather_list.add(datum_new + "\n" + icon + "\n" + temp_text);
                    weather.add(weatherObject);
                }
                //
                return weather; //weather_list;
            } catch (IOException ex) {
                Log.e(LOG_TAG, "IOException", ex);
            } catch (JSONException ex) {
                Log.e(LOG_TAG, "JSONException", ex);
            }
            catch (ParseException ex) {
                Log.e(LOG_TAG, "ParseException", ex);
            }

            return weather;}

        @Override
        protected void onPostExecute(List<Weather> result) {
            super.onPostExecute(result);
            MainActivity.ListData(result);
            //mResultTextView.setText(TextUtils.join("\n", result));

            //recyclerView.setAdapter(mAdapter);

        }
    }
}
