package me.chipperatkins.k_9harness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.preference.Preference;

public class MainActivity extends AppCompatActivity implements DataUpdateReciever.Receiver {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataUpdateReciever mReceiver = new DataUpdateReciever(new Handler());
        mReceiver.setReceiver(this);
        DogApplication.setUpdateReceiver(mReceiver);

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
        
        // create a dog
        Dog dog = new Dog("chipper");
        dog.abdominalTempThreshold = 88.0;
        dog.coreTempThreshold = 102.0;
        dog.respiratoryRateThreshold = 7.0;
        dog.heartRateThreshold = 98.0;

        // store a dog
        StorageHandler storageHandler = new StorageHandler(getApplicationContext());
        storageHandler.storeDog(dog);

        // create a session
        Session session = new Session("chipper");

        // store a session
        storageHandler.storeSessionAndUpdateDog(session);

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
    }

    public void goToGraph(View view){
        Log.d("GO_TO_GRAPH", "button pressed");
        Intent intent = new Intent(MainActivity.this, MainGraphActivity.class);
        String buttonTag = view.getTag().toString();
        int startPage = 0;
        if(buttonTag.equals("heart_rate"))
            startPage = 0;
        else if(buttonTag.equals("respiratory_rate"))
            startPage = 1;
        else if(buttonTag.equals("core_temperature"))
            startPage = 2;
        else if(buttonTag.equals("ambient_temperature"))
            startPage = 3;
        intent.putExtra("startPage", startPage);
        startActivity(intent);
    }

    public void updateUI(Bundle resultData) {

        Button heartRateButton = (Button) findViewById(R.id.heart_rate);
        heartRateButton.setText(Double.toString(resultData.getDouble("hr")));

        Button respiratoryRateButton = (Button) findViewById(R.id.respiratory_rate);
        heartRateButton.setText(Double.toString(resultData.getDouble("rr")));

        Button coreTemperatureButton = (Button) findViewById(R.id.core_temperature);
        heartRateButton.setText(Double.toString(resultData.getDouble("coreTemp")));

        Button ambientTemperatureButton = (Button) findViewById(R.id.ambient_temperature);
        heartRateButton.setText(Double.toString(resultData.getDouble("ambientTemp")));
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode==1) {
            updateUI(resultData);
        }
        else if(resultCode==2) {
            // Handle thresholds however you desire
        }
    }
}
