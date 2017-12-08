package me.chipperatkins.k_9harness;

import android.os.ResultReceiver;
import android.os.Handler;
import android.os.Bundle;

/**
 * Created by patrickatkins on 12/7/17.
 */

public class DataUpdateReciever extends ResultReceiver {

    private Receiver mReceiver;

    public DataUpdateReciever(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor sub
    }

    public interface Receiver {
        public void onRecceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onRecceiveResult(resultCode, resultData);
        }
    }
}
