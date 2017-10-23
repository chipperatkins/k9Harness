package me.chipperatkins.k_9harness;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by patrickatkins on 10/23/17.
 */

public class DbUodateService extends IntentService {
    public DbUodateService(String name) {
        super(name);
    }

    public DbUodateService() {
        super("dbservice");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StorageHandler handler = new StorageHandler();

        //Get the blue tooth update here. Store it in separate date, double combos for values

        // Global logged in dog is saved in settings could be sent in through intent, merge here with scott

        Dog dog = handler.retrieveDog("chipper");
        String sessionId = dog.sessions.get(dog.sessions.size() -1);
        Session session = handler.retrieveSession(sessionId);

        handler.updateHeartRate(session, new Date(), 88.0);
        handler.updateAmbientTemp(session, new Date(), 90.0);
        handler.updateCoreTemp(session, new Date(), 92.0);
        handler.updateRespiratoryRate(session, new Date(), 94.0);


        // wait for next update, or end service and restart from some type of blutetooth
        // service depending on how Scott implements it.
    }
}
