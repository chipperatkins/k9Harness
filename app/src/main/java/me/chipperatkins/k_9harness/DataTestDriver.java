package me.chipperatkins.k_9harness;

import android.content.Intent;
import android.app.IntentService;
import android.support.annotation.Nullable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by patrickatkins on 12/12/17.
 */

public class DataTestDriver extends IntentService {
    public DataTestDriver(String name) {super(name);}
    public DataTestDriver() {super("testDriver");}

    protected void onHandleIntent(@Nullable Intent intent) {
        Intent dbUpdater = new Intent(getApplicationContext(), DbUpdateService.class);
        Random r = new Random();
        final long NANOSEC_PER_SEC = 1000l*1000*1000;
        byte[] dummyBytes;

        long startTime = System.nanoTime();
        while((System.nanoTime()-startTime) < 5 *60*NANOSEC_PER_SEC) {
            //create dummy data
            int hr = (int) (100 * r.nextDouble()) + 50;
            int rr = (int) (50 * r.nextDouble()) + 50;
            int abdominalTemp = (int) (80 * r.nextDouble()) + 60;
            int ambientTemp = (int) (75 * r.nextDouble()) + 30;
            int chestTemp = (int) (80  * r.nextDouble()) + 40;

            String testString = hr + ":" + rr + ":" + abdominalTemp + ":" + ambientTemp + ":" + chestTemp;
            dummyBytes = testString.getBytes();


            dbUpdater.putExtra("byteArr", dummyBytes);
            intent.putExtra("dataUpdateReceiver", DogApplication.getUpdateReceiver());
            this.startService(dbUpdater);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
