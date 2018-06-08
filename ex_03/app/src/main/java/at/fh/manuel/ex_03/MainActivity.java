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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            URL url = new URL("http://10.202.234.84:8888/rest/items/vital_data/history");
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
    private class loadWebContentTask extends AsyncTask<URL, Void, List<VitalData>> {
        @Override
        protected List<VitalData> doInBackground(URL... urls) {
            URL url = urls[0];
            List<VitalData> vital_data_list = new ArrayList<>();
            String resultString = null;
            try{
                resultString = getResponseFromHttpURL(url);
                JSONArray jsonRoot  = new JSONArray(resultString);
                DateFormat json_date = new SimpleDateFormat("yyy-MM-dd_HH:mm");



                Set<String> vitalSet = new HashSet<>();
                for(int i =0; i<jsonRoot.length(); i++){
                    JSONObject entry = jsonRoot.getJSONObject(i);
                    VitalData vital_data_temp = new VitalData(json_date.parse(entry.getString("timestamp")),
                            entry.getInt("diastolic_pressure"), entry.getInt("systolic_pressure"),
                            entry.getInt("heart_rate"));
                    vital_data_list.add(vital_data_temp);
                }
                Collections.sort(vital_data_list);
                return vital_data_list;

            }catch (IOException ex){
                Log.e(LOG_TAG, "IOException", ex);
            }catch(JSONException ex){
                Log.e(LOG_TAG, "JSONException", ex);
            } catch (ParseException ex) {
                Log.e(LOG_TAG, "ParseException", ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<VitalData> result) {
            super.onPostExecute(result);
            if(result== null || result.size() ==0) {
                Toast.makeText(MainActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
            }else {
                StringBuilder sb_text = new StringBuilder();

                for (VitalData vital_data_temp : result) {
                    sb_text.append(new SimpleDateFormat("MMM dd, yyyy KK:mm:ss a").format(vital_data_temp.getTimestamp()) +
                            ": BP: " + vital_data_temp.getDiastolicPressure() + "/" + vital_data_temp.getSystolicPressure() +
                            ", HR: " + vital_data_temp.getHeartRate() + "\n");
                }

                mResultTextView.setText(sb_text.toString());
            }
        }
    }
}
