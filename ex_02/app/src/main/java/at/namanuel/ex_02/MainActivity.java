package at.namanuel.ex_02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "HILFE";
    public Button clicker, navigate;
    public TextView textis;
    public int summe;
    public String ergebnis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        summe = 0;
        clicker = findViewById(R.id.counter);
        navigate = findViewById(R.id.navi);
        textis = findViewById(R.id.textView);

        clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summe ++;

                ergebnis = "The click count is: "+Integer.toString(summe);
                textis.setText(ergebnis);

            }
        });
    }

    public void sendMessage(View view) {
        // Do something in response to button
        ergebnis = "The click count was: " + Integer.toString(summe);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra(EXTRA_MESSAGE, ergebnis);
        startActivity(intent);
    }
}
