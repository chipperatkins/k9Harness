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
import android.os.Handler;
import android.os.Message;
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
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT=3;

    // Flag to make sure only one device list begins
    private boolean deviceListStarted=false;

    // Layout Views
    private ListView mConversationView;
    private Button mConnectButton;
    private Button mReadLogsButton;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Array adapter for the devices thread
     */
    private ArrayAdapter<String> mDeviceListArrayAdapter;

    /**
     * Member object for the connection services
     */
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
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(super.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the listener session
        } else if(!this.harnessConnected()){
            Log.d(TAG, "Request connect device insecure");
            // Launch the DeviceListActivity to see devices and do scan
            if(this.deviceListStarted == false ) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                this.deviceListStarted = true;
            }
            if (mListenerService == null) {
                setupListener();
            }
        }
        Log.d(TAG, "Fragment started");
    }

    public boolean harnessConnected(){
        // Checks all previously paired devices to see if they are harnesses
        // NOTE: Does not determine if harness is currently connected
        Set<BluetoothDevice> pairedDevices = this.mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device: pairedDevices) {
            if(this.harnessName(device.getName())){
                Log.d(TAG, "Device is bonded: " + device.getName());
                return true;
            }
        }
        return false;
    }

    public boolean harnessName(String name) {
        // Checks if device name matches naming convention for harnesses
        if(name.length() >= 7 && Objects.equals(name.substring(0,6),"HARNESS") ){
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerService != null) {
            mListenerService.stop();
        }
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
                // Start the Bluetooth chat services
                mListenerService.start();
            }
        }
        Log.d(TAG, "Fragment resumed");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mConnectButton = (Button) view.findViewById(R.id.button_connect);
        mReadLogsButton = (Button) view.findViewById(R.id.button_read_logs);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupListener() {
        Log.d(TAG, "setupListener()");

        // Initialize the array adapter for the listening thread
        mDeviceListArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mDeviceListArrayAdapter);

        // Initialize the send button with a listener that for click events
/*        mConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                   *//* TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);*//*
                }
            }
        });*/

        Log.d(TAG, "setupListener()- clickListener made");
        // Initialize the BluetoothListenerService to perform bluetooth connections
        mListenerService = new BluetoothListenerService(getActivity(), mHandler);
        Log.d(TAG, "setupListener() - btListenerService started");
    }

    /**
     * The Handler that gets information back from the BluetoothListenerService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Message handler beginning");
            FragmentActivity activity = (FragmentActivity) getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothListenerService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to) + mConnectedDeviceName);
                            mDeviceListArrayAdapter.clear();
                            break;
                        case BluetoothListenerService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothListenerService.STATE_LISTEN:
                        case BluetoothListenerService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mDeviceListArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mDeviceListArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK && this.deviceListStarted == false) {
                    Log.d(TAG, "ENABLED BT");
                    // Bluetooth is now enabled, so set up a chat session
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
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


    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mListenerService.connect(device);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = (FragmentActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = (FragmentActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }
}