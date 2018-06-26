package at.fh.manuel.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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

public class MainActivity extends AppCompatActivity {

    String api_key = "e9b3e40b1174e2215b4d761f665ef1b5";
    public String url_weather = "http://api.openweathermap.org/data/2.5/forecast?q=Vienna,at&APPID=" + api_key;
    private static final String LOG_TAG = MainActivity.class.getCanonicalName();
    private TextView mResultTextView;
    private Button mReloadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = findViewById(R.id.tv_result);
        mReloadButton = findViewById(R.id.btn_reload);

        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Beim BUTTON click das Textview leer setzen!!!!°
                loadWebResult();
            }
        });
    }
    private void loadWebResult(){
        try{
            //URL VON DEINEM VITALSERVER MIT PORT reinschreiben (z.b. 10.0.0.25:8080)
            URL url = new URL(url_weather);
//            String content = getResponseFromHttpURL(url);
//            mResultTextView.setText(content);
            new loadWebContentTask().execute(url);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "MalformedURLException.", e);
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }
// catch(IOException e){
//            Log.e(LOG_TAG, "IOException.", e);
//            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
//        }
    }
    private String getResponseFromHttpURL (URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        try{
            urlConnection.setRequestMethod("GET");
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext())
                return scanner.next();
            else
                return null;

        }finally {
            urlConnection.disconnect();
        }
    }
    private class loadWebContentTask extends AsyncTask<URL, Void, List<String>>{
        @Override
        protected List<String> doInBackground(URL... urls) {
            URL url = urls[0];
            String resultString = null;
            try{

                //wir brauchen kein JsonObject sondern nur ein Array weil wir nur arrays bekommen, somit nur mit dem Array arbeiten und das dann in jsonobjects zerstückeln
                resultString = getResponseFromHttpURL(url);
                DateFormat json_date = new SimpleDateFormat("yyy-MM-dd_HH:mm");




                JSONObject jsonRoot = new JSONObject(resultString);
                JSONArray weather_array = jsonRoot.getJSONArray("list");
                List<String> weather_list = new LinkedList<>();
                for (int i = 0; i < weather_array.length(); i++) {
                    JSONObject entry = weather_array.getJSONObject(i); //DATUM; ICON KÜRZEL; TEMPERATUR IN °C
                    JSONObject main = entry.getJSONObject("main");
                    double weather_temp = main.getDouble("temp") - 273.15;
                    JSONObject weather = entry.getJSONObject("weather");
                    String icon = weather.getString("icon");
                    Date datum = json_date.parse(entry.getString("dt_txt"));
                    String date = entry.getString("dt_txt");
                    //weather_list.add(String.valueOf(datum));
                    //weather_list.add(String.valueOf(weather_temp));
                    //weather_list.add(icon);
                    weather_list.add(date);
                }
                return weather_list;

            }catch (IOException ex){
                Log.e(LOG_TAG, "IOException", ex);
            }catch(JSONException ex){
                Log.e(LOG_TAG, "JSONException", ex);
            } catch (ParseException ex) {
                Log.e(LOG_TAG, "JSONException", ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            if(result== null || result.size() ==0)
                Toast.makeText(MainActivity.this, "LEER.", Toast.LENGTH_SHORT).show();
            else
                mResultTextView.setText(TextUtils.join("\n", result));
        }
    }
}
