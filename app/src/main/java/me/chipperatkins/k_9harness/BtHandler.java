package me.chipperatkins.k_9harness;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by user on 12/8/2017.
 */

public class BtHandler extends Handler {
    Context mContext;
    DogApplication mDogApplication;

    public BtHandler(Context context, DogApplication dogApplication) {
        mContext = context;
        mDogApplication = dogApplication;
    }

    public void handleMessage(byte[] readBuf) {
        Intent intent = new Intent(mContext , DbUpdateService.class);
        intent.putExtra("byteArr", readBuf);
        intent.putExtra("dataUpdateReceiver", mDogApplication.getUpdateReceiver());
        mContext.startService(intent);
    }
}
