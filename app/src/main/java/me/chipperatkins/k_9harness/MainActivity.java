package me.chipperatkins.k_9harness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.preference.Preference;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(prefs.getString("current_dog", "Dog Name"));
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Marked.", Snackbar.LENGTH_LONG)
                        .setAction("Add a note?", new AddMemoListener()).show();
            }
        });

        LinearLayout heartRateButton = (LinearLayout)findViewById(R.id.heart_rate);
        heartRateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToGraph(0);
            }
        });

        LinearLayout respiratoryRateButton = (LinearLayout)findViewById(R.id.respiratory_rate);
        respiratoryRateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToGraph(1);
            }
        });

        LinearLayout coreTemperatureButton = (LinearLayout)findViewById(R.id.core_temperature);
        coreTemperatureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToGraph(2);
            }
        });

        LinearLayout ambientTemperatureButton = (LinearLayout)findViewById(R.id.ambient_temperature);
        ambientTemperatureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToGraph(3);
            }
        });



        // create a dog
        Dog dog = new Dog("chipper");
        dog.abdominalTempThreshold = 88.0;
        dog.coreTempThreshold = 102.0;
        dog.respiratoryRateThreshold = 7.0;
        dog.heartRateThreshold = 98.0;

        // store a dog
        StorageHandler storageHandler = new StorageHandler(getApplicationContext());
        storageHandler.storeDog(dog);

        //Dog d = storageHandler.retrieveDog("chipper");
        // create a session
        Session session = new Session("chipper");

        // store a session
        storageHandler.storeSessionAndUpdateDog(session);

        updateUI("chipper");
        Intent intent = new Intent(getApplicationContext(), DbUpdateService.class);

        this.startService(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(prefs.getString("current_dog", "Dog Name"));

        }
        updateUI(prefs.getString("current_dog", ""));

    }

    public void goToGraph(int startPage){
        Log.d("GO_TO_GRAPH", "button pressed");
        Intent intent = new Intent(MainActivity.this, MainGraphActivity.class);
        intent.putExtra("startPage", startPage);
        startActivity(intent);

    }

    public void updateUI(String curDog) {
        StorageHandler storageHandler = new StorageHandler();
        Dog dog = storageHandler.retrieveDog(curDog);
        double heartRate = 100;
        double respiratoryRate = 30;
        double coreTemperature = 50;
        double ambientTemperature = 60;

        TextView heartRateValue = (TextView) findViewById(R.id.heart_rate_value);
        heartRateValue.setText(Double.toString(heartRate));
        if(dog != null && dog.isOverHeartRateThreshold(heartRate)){
            heartRateValue.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else{
            heartRateValue.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }


        TextView respiratoryRateValue = (TextView) findViewById(R.id.respiratory_rate_value);
        respiratoryRateValue.setText(Double.toString(respiratoryRate));
        if(dog != null && dog.isOverRespiratoryRateThreshold(respiratoryRate)){
            respiratoryRateValue.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else{
            respiratoryRateValue.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        TextView coreTemperatureValue = (TextView) findViewById(R.id.core_temperature_value);
        coreTemperatureValue.setText(Double.toString(coreTemperature));
        if(dog != null && dog.isOverCoreTempThreshold(coreTemperature)){
            coreTemperatureValue.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else{
            coreTemperatureValue.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        TextView ambientTemperatureValue = (TextView) findViewById(R.id.ambient_temperature_value);
        ambientTemperatureValue.setText(Double.toString(ambientTemperature));
        if(dog != null && dog.isOverAbdominalTempThreshold(ambientTemperature))
        {
            ambientTemperatureValue.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else{
            ambientTemperatureValue.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }


    }
}
