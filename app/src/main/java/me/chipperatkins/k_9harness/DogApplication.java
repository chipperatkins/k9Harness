package me.chipperatkins.k_9harness;

import android.app.Application;

/**
 * Created by user on 12/8/2017.
 */

public class DogApplication extends Application {

    static private Dog activeDog = null;
    static private boolean bluetoothToggle = false;
    static private DataUpdateReciever mReceiver;

    static public Dog getActiveDog() {
        return activeDog;
    }

    static public void setActiveDog(Dog active){
        activeDog = active;
    }

    static public boolean isBluetoothConnected() {
        return bluetoothToggle;
    }

    static public void bluetoothToggle(){
        if (bluetoothToggle) {
            bluetoothToggle = false;
        }
        else {
            bluetoothToggle = true;
        }
    }

    static public DataUpdateReciever getUpdateReceiver() { return mReceiver; }
    static public void setUpdateReceiver(DataUpdateReciever mDataReceiver) { mReceiver = mDataReceiver; }
}
