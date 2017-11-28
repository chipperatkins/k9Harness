package me.chipperatkins.k_9harness;

/**
 * Created by patrickatkins on 10/24/17.
 */

class Constants {
    static final int VARIABLES_IN_PACKET = 5;

    // Message types sent from the BluetoothChatService Handler
    static final int MESSAGE_STATE_CHANGE = 1;
    static final int MESSAGE_READ = 2;
    static final int MESSAGE_WRITE = 3;
    static final int MESSAGE_DEVICE_NAME = 4;
    static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    static final String DEVICE_NAME = "device_name";
    static final String TOAST = "toast";

}
