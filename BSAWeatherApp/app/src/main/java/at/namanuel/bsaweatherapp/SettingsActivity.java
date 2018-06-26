package at.namanuel.bsaweatherapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_settings);
    getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new at.namanuel.bsaweatherapp.SettingsFragment()).commit();

  }

}
