package me.chipperatkins.k_9harness;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by patrickatkins on 10/10/17.
 */

public class Session {
    private Date startDate;
    private Date endTime;
    private String dog;

    private LinkedHashMap<String, Double> heartRate;
    private LinkedHashMap<String, Double> respiratoryRate;
    private LinkedHashMap<String, Double> coreTemp;
    private LinkedHashMap<String, Double> ambientTemp;

    public Session(String dogg) {
        startDate = new Date();
        endTime = null;
        dog = dogg;

        heartRate = new LinkedHashMap<>();
        respiratoryRate = new LinkedHashMap<>();
        coreTemp = new LinkedHashMap<>();
        ambientTemp = new LinkedHashMap<>();
    }

    public boolean isActive() {
        return (endTime == null);
    }

    // Adds value to heart rate map
    public void addHeartRate(String time, Double value) {
        heartRate.put(time, value);
    }

    // Adds value to respiratory rate map
    public void addRespiratoryRate(String time, Double value) {
        respiratoryRate.put(time, value);
    }

    // Adds value to coreTemp map
    public void addCoreTemp(String time, Double value) {
        coreTemp.put(time, value);
    }

    // Adds value to ambientTemp map
    public void addAmbientTemp(String time, Double value) {
        ambientTemp.put(time, value);
    }
}
