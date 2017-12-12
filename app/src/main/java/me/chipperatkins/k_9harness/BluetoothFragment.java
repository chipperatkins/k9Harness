package me.chipperatkins.k_9harness;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothFragment extends Fragment {

    private static final String TAG = "BluetoothFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Flag to make sure only one device list begins
    private boolean deviceListStarted = false;

    // Layout Views
    private ListView mDeviceView;
    private Button mReadLogsButton;

    // BT handler passed to the listener service
    private BtHandler mHandler;

    //Name of the connected device
    private String mConnectedDeviceName = null;

    // Local BT Adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Array adapter for devices thread
    private ArrayAdapter<String> mDeviceListArrayAdapter;

    // Member object for connection service
    private BluetoothListenerService mListenerService = null;

    public BluetoothFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating fragment");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Activity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }

        Log.d(TAG, "Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Starting fragment");
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupListener() will then be called during onActivityResult

        // explicit permissions for android 6.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(super.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the listener session
        } else if (!this.harnessConnected()) {
            Log.d(TAG, "Request connect device");
            // Launch the DeviceListActivity to see devices and do scan
            if (!deviceListStarted) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                deviceListStarted = true;
            }
            if (mListenerService == null) {
                setupListener();
            }
        }
        Log.d(TAG, "Fragment started");
    }

    public boolean harnessConnected() {
        // Checks all previously paired devices to see if they are harnesses
        // NOTE: Does not determine if harness is currently connected
        Set<BluetoothDevice> pairedDevices = this.mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (isHarnessName(device.getName())) {
                Log.d(TAG, "Harness has been bonded: " + device.getName());
                return true;
            }
        }
        return false;
    }

    public boolean isHarnessName(String name) {
        // Checks if device name matches naming convention for harnesses
        if (name.length() >= 7 && Objects.equals(name.substring(0, 6), "HARNESS")) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (mListenerService != null) {
            mListenerService.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resuming Fragment");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mListenerService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mListenerService.getState() == BluetoothListenerService.STATE_NONE) {
                // Start the Bluetooth listener services
                mListenerService.start();
            }
        }
        Log.d(TAG, "Fragment resumed");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDeviceView = (ListView) view.findViewById(R.id.in);
        mReadLogsButton = (Button) view.findViewById(R.id.button_read_logs);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupListener() {
        Log.d(TAG, "setupListener()");

        // Initialize the array adapter for the listening thread
        mDeviceListArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mDeviceView.setAdapter(mDeviceListArrayAdapter);

        Log.d(TAG, "setupListener()- clickListener made");
        // Initialize the BluetoothListenerService to perform bluetooth connections
        mListenerService = new BluetoothListenerService(getActivity(), mHandler);
        Log.d(TAG, "setupListener() - btListenerService started");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK && !deviceListStarted) {
                    Log.d(TAG, "ENABLED BT");
                    // Bluetooth is now enabled, so set up a chat session
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    deviceListStarted = true;
                    if (mListenerService == null) {
                        setupListener();
                    }
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }


    // Establish connection with other device
    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mListenerService.connect(device);
    }
}
