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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
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
                loadWebResult();
            }
        });
    }
    private void loadWebResult(){
        try{
            URL url = new URL("https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:OEFFHALTESTOGD&srsName=EPSG:4326&outputFormat=json");
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
                resultString = getResponseFromHttpURL(url);
                JSONObject jsonRoot  = new JSONObject(resultString);
                JSONArray features = jsonRoot.getJSONArray("features");
                Set<String> stationSet = new HashSet<>();
                for(int i =0; i<features.length(); i++){
                    JSONObject entry = features.getJSONObject(i);
                    JSONObject properties = entry.getJSONObject("properties");
                    String name = properties.getString("HTXT");
                    stationSet.add(name);
                }
                List<String> stationList = new LinkedList<>(stationSet);
                Collections.sort(stationList);
                return stationList;

            }catch (IOException ex){
                Log.e(LOG_TAG, "IOException", ex);
            }catch(JSONException ex){
                Log.e(LOG_TAG, "JSONException", ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            if(result== null || result.size() ==0)
                Toast.makeText(MainActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
            else
                mResultTextView.setText(TextUtils.join("\n", result));
        }
    }
}
