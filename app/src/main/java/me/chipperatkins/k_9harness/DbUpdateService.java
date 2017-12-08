package me.chipperatkins.k_9harness;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Date;

/**
 * Created by patrickatkins on 10/23/17.
 */

//TODO: add threshold checks here
//TODO: raise event

public class DbUpdateService extends IntentService {
    public DbUpdateService(String name) {
        super(name);
    }

    public DbUpdateService() {
        super("dbservice");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StorageHandler handler = new StorageHandler();

        // Global logged in dog is saved in settings could be sent in through intent, merge here with scott

        Dog dog = handler.retrieveDog("chipper");
        String sessionId = dog.sessions.get(dog.sessions.size() -1);
        Session session = handler.retrieveSession(sessionId);


        // Things for Scott to Sync
        Object byteArrayFromBlueTooth = null;
        int numBytesFromBlueTooth = 8;
        // ---------------------------------


        String testData = "20:30:40:50:60";
        byte[] testBytes = testData.getBytes();
        int numBytes = testBytes.length;

        byte[] readBuf = (byte[]) testBytes;
        String readMessage = new String(readBuf, 0, numBytes);
        String[] parsedMessage = readMessage.split(":");

        if (parsedMessage.length == Constants.VARIABLES_IN_PACKET) {

            // remove leading or trailing whitespace
            for (int i = 0; i < Constants.VARIABLES_IN_PACKET; i++){
                parsedMessage[i] = parsedMessage[i].trim();
            }

            try {
                double hr = (double) Integer.parseInt(parsedMessage[0]);
                double rr = (double) Integer.parseInt(parsedMessage[1]);
                double abdominalTemp = (double) Integer.parseInt(parsedMessage[2]);
                double ambientTemp = (double) Integer.parseInt(parsedMessage[3]);
                double chestTemp = (double) Integer.parseInt(parsedMessage[4]);

                Date now = new Date();

                // convert temperature values from ADC values to degrees Fahrenheit
                abdominalTemp = DataHandler.convertTemp(abdominalTemp);
                ambientTemp = DataHandler.convertTemp(ambientTemp);
                chestTemp = DataHandler.convertTemp(chestTemp);

                // get average ambient temp so far
                handler.updateAmbientTemp(session, now, ambientTemp);
                Session updatedSession = handler.retrieveSession(sessionId);
                Collection<Double> ambientMap = updatedSession.ambientTemp.values();

                double avgAmbient = 0.0;
                for (double temp: ambientMap) {
                    avgAmbient += temp;
                }
                avgAmbient = avgAmbient / ambientMap.size();

                double coreTemp = DataHandler.calculateCoreTemp(abdominalTemp, ambientTemp, chestTemp, avgAmbient);

                handler.updateCoreTemp(session, now, coreTemp);
                handler.updateAbdominalTemp(session, now, abdominalTemp);
                handler.updateHeartRate(session, now, hr);
                handler.updateRespiratoryRate(session, now, rr);

            }
            catch (NumberFormatException nfe) {
                // Handle data cannot be parsed here
            }
        }
        // Data length is not correct number of values
        else {
            /*Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            String text = "Corrupted data received.";
            Toast.makeText(context, text, duration).show();

            This should not be done here, ui should not be updated from background thread */
        }
    }
}
