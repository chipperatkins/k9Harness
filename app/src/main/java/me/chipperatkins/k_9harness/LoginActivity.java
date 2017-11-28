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
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ArrayAdapter<String> mDogArrayAdapter;
    private ListView dogListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        // Initialize the button to perform device discovery
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


        // store a dog
        StorageHandler storageHandler = new StorageHandler(getApplicationContext());
        storageHandler.storeDog(dog);

        // create a session
        Session session = new Session("chipper");

        // store a session
        storageHandler.storeSessionAndUpdateDog(session);

        Intent intent = new Intent(getApplicationContext(), DbUpdateService.class);

        this.startService(intent);
        // End test section


        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        this.mDogArrayAdapter = new ArrayAdapter<String>(this, R.layout.dog_name);
        mDogArrayAdapter.add(dog.name);

        // Find and set up the ListView for paired devices
        ListView dogListView = (ListView) findViewById(R.id.dog_list);
        dogListView.setAdapter(this.mDogArrayAdapter);
        dogListView.setOnItemClickListener(mDogClickListener);

        // Find and set up the ListView for newly discovered devices
        //ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        //deviceListView.setAdapter(mNewDevicesArrayAdapter);
        //deviceListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a dog is returned
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //Set<Dogs> dogList = getDogList();

        /*if (mDogArrayAdapter. > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (Dog dog : dogList) {
                this.mDogArrayAdapter.add(dog.getName() + "\n" + dog.getLastSession());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            this.mDogArrayAdapter.add(noDevices);
        }*/
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dogListView = (ListView) view.findViewById(R.id.in);
    }

    /*    *
     * The on-click listener for all dogs in the ListViews*/
    private AdapterView.OnItemClickListener mDogClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Get the dog name and it's most recent session time


            // Create the result Intent and include the MAC address
            //Intent intent = new Intent();
            //intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            //setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

/*    *
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished*/

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a dog
            if (true) {

            }
        }
    };
}
