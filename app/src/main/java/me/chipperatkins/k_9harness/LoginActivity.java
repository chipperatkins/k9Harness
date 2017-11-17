package me.chipperatkins.k_9harness;

import android.Manifest;
import android.app.Activity;
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

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ArrayAdapter<String> mDogArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);

        this.mDogArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView dogListView = (ListView) findViewById(R.id.devices);
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

        Set<Dogs> dogList = getDogList();

        // If there are paired devices, add each one to the ArrayAdapter
        if (dogList.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (Dog dog : dogList) {
                this.mDogArrayAdapter.add(dog.getName() + "\n" + dog.getLastSession());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            this.mDogArrayAdapter.add(noDevices);
        }
    }

    /**
     * The on-click listener for all dogs in the ListViews
     */
    private AdapterView.OnItemClickListener mDogClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Get the dog name and it's most recent session time

            
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED && device.getName() != null) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
/*
                setProgressBarIndeterminateVisibility(false);
*/
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };
}
