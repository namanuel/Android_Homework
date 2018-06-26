package at.namanuel.bsaweatherapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import static at.namanuel.bsaweatherapp.MainActivity.EXTRA_MESSAGE;

public class DetailActivity  extends AppCompatActivity {

    public TextView tv_date, tv_weatherdes, tv_temp, tv_airpres, tv_hum, tv_cloud, tv_windV, tv_windD, tv_rain, tv_snow;
    public ImageView iv_icon;
    Context mContext;
    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");
    String $lang = Locale.getDefault().getLanguage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        Weather weatherdata = (Weather) getIntent().getSerializableExtra(EXTRA_MESSAGE);

        tv_date = findViewById(R.id.textView_Date);
        iv_icon = findViewById(R.id.iv_iconDetail);
        tv_weatherdes = findViewById(R.id.textView_Description);
        tv_temp = findViewById(R.id.textView_Temp);
        tv_airpres = findViewById(R.id.textView_AirP);
        tv_hum = findViewById(R.id.textView_Humidity);
        tv_cloud= findViewById(R.id.textView_Cloud);
        tv_windV= findViewById(R.id.textView_WindV);
        tv_windD= findViewById(R.id.textView_WindD);
        tv_rain= findViewById(R.id.textView_Rain);
        tv_snow= findViewById(R.id.textView_Snow);
        mContext = getApplicationContext();

        String speed = "km/h";
        //SPRACHE
        if($lang != "de"){
            speed = "kph";
            tv_weatherdes.setText(weatherdata.getDescription());
        }else{
            String wetterbeschreibund_de = getDescirptionForConditionCode(mContext, weatherdata.getConditions());
            tv_weatherdes.setText(wetterbeschreibund_de);
        }


        // Get the Intent that started this activity and extract the string


        String uri = "@drawable/a"+weatherdata.getIcon();

        int resourceId = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        Drawable imageRes = mContext.getResources().getDrawable(resourceId);



        iv_icon.setImageDrawable(imageRes);
        tv_date.setText(weatherdata.getDate());


        tv_temp.setText(" "+weatherdata.getTemp());
        tv_airpres.setText(" "+REAL_FORMATTER.format(weatherdata.getPressure())+" hPa");
        tv_hum.setText(" "+REAL_FORMATTER.format(weatherdata.getHumidity())+" %");
        tv_cloud.setText(" "+REAL_FORMATTER.format(weatherdata.getClouds())+" %");
        tv_windV.setText(" "+REAL_FORMATTER.format(weatherdata.getWindSpeed())+speed);
        tv_windD.setText(" "+REAL_FORMATTER.format(weatherdata.getWindDirection())+"°");
        tv_rain.setText(" "+REAL_FORMATTER.format(weatherdata.getRain())+" mm");
        tv_snow.setText(" "+REAL_FORMATTER.format(weatherdata.getSnow())+" mm");




        // Capture the layout's TextView and set the string as its text

    }
    public static String getDescirptionForConditionCode(Context context, int conditionCode){

        return context.getString(context.getResources().getIdentifier("condition" + conditionCode, "string", context.getPackageName()));

    }
}