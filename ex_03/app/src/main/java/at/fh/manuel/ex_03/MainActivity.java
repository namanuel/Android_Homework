package at.fh.manuel.ex_03;

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
                mResultTextView.setText("");
                loadWebResult();
            }
        });
    }
    private void loadWebResult(){
        try{//IPADRESSE ÄNDERN!!!!!!!!!!!
            URL url = new URL("http://192.168.43.183:8888/rest/items/vital_data/history");
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
    private String getResponseFromHttpURL (URL url) throws IOException {
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
    private class loadWebContentTask extends AsyncTask<URL, Void, List<String>> {
        @Override
        protected List<String> doInBackground(URL... urls) {
            URL url = urls[0];
            String resultString = null;
            try{
                resultString = getResponseFromHttpURL(url);
                JSONArray jsonRoot  = new JSONArray(resultString);
                Set<String> vitalSet = new HashSet<>();
                for(int i =0; i<jsonRoot.length(); i++){
                    JSONObject entry = jsonRoot.getJSONObject(i);
                    String timestamp = entry.getString("timestamp");
                    String heart_rate = entry.getString("heart_rate");
                    String systolic_pressure = entry.getString("systolic_pressure");
                    String diastolic_pressure = entry.getString("diastolic_pressure");
                    vitalSet.add(timestamp +"; "+"BP: "+systolic_pressure +"/"+diastolic_pressure+ " HR: " + heart_rate);
                }
                List<String> vitalList = new LinkedList<>(vitalSet);
                Collections.sort(vitalList);
                Collections.reverse(vitalList);
                return vitalList;

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
