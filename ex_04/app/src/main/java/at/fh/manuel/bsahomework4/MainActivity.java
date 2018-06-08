package at.fh.manuel.bsahomework4;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class    MainActivity extends AppCompatActivity {
    private ListAdapter mAdapter;
    private static final String LOG_TAG =
            MainActivity.class.getCanonicalName();
    private Toast mToast;

    private static final String RESULT_KEY = "result";

    public Button ok_btn;
    public SeekBar seeker;
    public EditText edittext;
    public RecyclerView recyclerView;
    public int seek_value;
    public List<String> data = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readFile();
        ok_btn = findViewById(R.id.button_todo);
        seeker = findViewById(R.id.seekbar_todo);
        edittext = findViewById(R.id.todo_text);
        recyclerView = findViewById(R.id.rv_todo);


        mAdapter = new ListAdapter(generateContent());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnItemClickListener(new ListAdapter.ListItemClicklistener() {
            @Override
            public void onListItemClick(String item) {
                data.remove(item);
                mAdapter.swapData(generateContent());
                Log.d(LOG_TAG, "Eintrag "+ item +" gelöscht.");
                if(mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(MainActivity.this,"Eintrag "+ item+" gelöscht.", Toast.LENGTH_SHORT);
                mToast.show();

            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int porgress = seeker.getProgress();
                String text_edit = edittext.getText().toString();
                if(!edittext.getText().toString().equals("")){
                    writeFile();
                    mAdapter.swapData(generateContent());
                    edittext.setText("");
                }else {
                    Toast.makeText(MainActivity.this,"Write Todo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (savedInstanceState != null) {
            String resultString = savedInstanceState.getString(RESULT_KEY);
        }

    }
    private List<String> generateContent(){

        Collections.sort(data);
        return data;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mAdapter.swapData(generateContent());
    }
    public void writeFile(){
        String text_todo = edittext.getText().toString();
        String file_name = "todo_file";
        data.add(text_todo);
        try{
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(),"Todo gespeichert",Toast.LENGTH_LONG).show();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void readFile(){
        try {
            FileInputStream fileInputStream  = openFileInput("todo_file");

            //InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            //BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            data.clear();
            data = (List<String>) objectInputStream.readObject();
            objectInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}