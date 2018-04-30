package at.fh.manuel.homework_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "HILFE";
    public EditText input_zahl1, input_zahl2;
    public TextView output;
    public Button calculateButton;
    public String ergebnis;
    public int summe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        summe=0;
        input_zahl1 =(EditText)findViewById(R.id.number1);
        input_zahl2 = (EditText)findViewById(R.id.number2);
        output = (TextView)findViewById(R.id.ouput);
        calculateButton = (Button)findViewById(R.id.calcbtn);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculate();

                ergebnis = Integer.toString(summe);

                output.setText(ergebnis);
            }
        });



        SeekBar seekBar = (SeekBar)findViewById(R.id.seeker);
        final TextView seekBarValue = (TextView)findViewById(R.id.seekerValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });


    }

    public void calculate(){
        try {
            int zahl1 = Integer.parseInt("0" + input_zahl1.getText().toString());
            int zahl2 = Integer.parseInt("0" + input_zahl2.getText().toString());

            summe = zahl1 + zahl2;
        }catch (Exception e){
            Toast.makeText(this, "invalid Number", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void sendMessage(View view) {
        // Do something in response to button
        calculate();
        ergebnis = Integer.toString(summe);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra(EXTRA_MESSAGE, ergebnis);
        startActivity(intent);
    }
}
