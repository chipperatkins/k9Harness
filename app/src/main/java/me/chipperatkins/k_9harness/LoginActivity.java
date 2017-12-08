package me.chipperatkins.k_9harness;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ArrayAdapter<String> mDogArrayAdapter;
    private ListView dogListView;
    StorageHandler storageHandler;
    BtHandler mHandler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        storageHandler = new StorageHandler(getApplicationContext());

        // Create a bluetooth fragment to connect to devices
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BluetoothFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        Log.d(TAG, "Created BT fragment");
        // Create a list of dog profiles to login to
        // Connect to DB, find all dog profiles
        // Adapt to listview by Name and Last Used fields
        // Allow selectable

        // Initialize the button to allow skipping the dog selection process
        Button skipButton = (Button) findViewById(R.id.button_continue);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Section creating a dog to test dog listing
        // create a dog
        Dog dog = new Dog("chipper");
        dog.abdominalTempThreshold = 88.0;
        dog.coreTempThreshold = 102.0;
        dog.respiratoryRateThreshold = 7.0;
        dog.heartRateThreshold = 98.0;


        // store 2 dogs
        storageHandler.storeDog(dog);

        Dog dog2 = new Dog("dog2");
        dog2.abdominalTempThreshold = 88.0;
        dog2.coreTempThreshold = 102.0;
        dog2.respiratoryRateThreshold = 7.0;
        dog2.heartRateThreshold = 98.0;
        storageHandler.storeDog(dog);

        // create a session
        Session session = new Session("chipper");

        // store a session
        storageHandler.storeSessionAndUpdateDog(session);

        //Intent intent = new Intent(getApplicationContext(), DbUpdateService.class);

        //this.startService(intent);
        // End test section


        // Initialize array adapters for listing all dog names
        this.mDogArrayAdapter = new ArrayAdapter<String>(this, R.layout.dog_name);
        Map<String, Object> allDogs = storageHandler.retrieveAllDogs();
        Iterator it = allDogs.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getKey().toString().charAt(0) != '_') {
                mDogArrayAdapter.add(pair.getKey().toString());
            }
        }

        // Find and set up the ListView for dog names. also attach the clicklistener
        ListView dogListView = (ListView) findViewById(R.id.dog_list);
        dogListView.setAdapter(this.mDogArrayAdapter);
        dogListView.setOnItemClickListener(mDogClickListener);


    }

 /*   public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dogListView = (ListView) view.findViewById(R.id.in);
    }*/

    /*    *
     * The on-click listener for all dogs in the ListViews*/
    private AdapterView.OnItemClickListener mDogClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {

            // Get the selected dog
            String dogName = (String) av.getItemAtPosition(position);
            Dog selectedDog = storageHandler.retrieveDog(dogName);

            // Set the dog as the current active in global settings
            ((DogApplication) LoginActivity.super.getApplication()).setActiveDog(selectedDog);
            Log.d(TAG, "Global dog set: " + ((DogApplication) LoginActivity.super.getApplication()).getActiveDog().name);

            // Initialize the btHandler with the active dog if BT is active
            // Check if active BT set
                // begin btHandler
               //Intent intent = new Intent();
                //intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            // BT not set, so don't start a handler

            finish();
        }
    };
}
