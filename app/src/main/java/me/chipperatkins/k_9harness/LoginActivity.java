package me.chipperatkins.k_9harness;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT=1;
    private static final String TAG = "LoginActivity";
    TextView btnOnOff;
    private ListView lvBtDevices;
    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<String> mBtDevices = new ArrayList<>();
    public ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private Handler hand = new Handler();
    private BroadcastReceiver mReceiver = new MyReceiver(hand);

    // Create a BroadcastReceiver for ACTION_FOUND.
    public class MyReceiver extends BroadcastReceiver {
        private final Handler handler;

        public MyReceiver(Handler handler){
            this.handler = handler;
        }

        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
//            Log.v(TAG, "onReceive is called" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName()!= null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                            discoveredDevices.add(device);
                            mBtDevices.add(device.getName());
                            updateList();
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initiatialize BT adapter, done button, view data button, refresh
        // BT list button, bt listview, and dog listview
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnOnOff=(TextView)findViewById(R.id.btOnOff);
        lvBtDevices = (ListView) findViewById(R.id.lvBtDevices);

        // Create a list of BT devices to connect to
        // Chcek if device is BT enabled, then enable if possible
        if(mBluetoothAdapter==null) {
            Toast.makeText(getApplicationContext(), "This device doesn't support Bluetooth.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            if(!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Turning on Bluetooth.", Toast.LENGTH_SHORT).show();
                Intent turnOnBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOnBt, REQUEST_ENABLE_BT);
            }
            else {
                Log.v(TAG, "BT already enabled");
                beginListing();
            }
        }

        // Create a list of dog profiles to login to
        // Connect to DB, find all dog profiles
        // Adapt to listview by Name and Last Used fields
        // Allow selectable

    }

    private void updateList() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1 , mBtDevices);
        Log.v(TAG, "BT DEVICES:");
        for(int i=0;i<arrayAdapter.getCount();i++) {
            Log.v(TAG, arrayAdapter.getItem(i) + "\n");
        }
        lvBtDevices.setAdapter(arrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "BT ActivityResult Returned");
        if (requestCode == REQUEST_ENABLE_BT) {
            Log.v(TAG, "BT Turn On Request Made");
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is now on.", Toast.LENGTH_SHORT).show();
                beginListing();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth failed to enable.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void addBoundDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mBtDevices.add(device.getName());
            }
        }
    }

//    public void OnClick(View v){
//
//    }

    private void beginListing(){
        /* once BT has been enabled, populate the list of bluetooth devices*/

        // list all devices that the device has previously connected to
        this.addBoundDevices();
        updateList();

        // In case device hasn't previously been connected, beging the discovery process
        // Register broadcast receiver to detect when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);

        // explicit permissions for android 6.0+
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        // by convention, always cancel discovery before beginning it
        mBluetoothAdapter.cancelDiscovery();
        // start discovery and output text to let the user know
        if (mBluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(), "Discovering other devices", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Could not search for other devices", Toast.LENGTH_SHORT).show();
        }
    }

}
