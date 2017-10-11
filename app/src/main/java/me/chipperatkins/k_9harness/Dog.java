package me.chipperatkins.k_9harness;

import java.util.ArrayList;

/**
 * Created by patrickatkins on 10/10/17.
 */

public class Dog {
    public String name;
    private Double heartRateThreshold;
    private Double respiratoryRateThreshold;
    private Double coreTempThreshold;
    private Double ambientTempThreshold;
    private ArrayList<String> sessionIds;

    //Constructor
    public Dog(String n) {
        name = n;
        heartRateThreshold = null;
        respiratoryRateThreshold = null;
        coreTempThreshold = null;
        ambientTempThreshold = null;
        sessionIds = new ArrayList<>();
    }

    public boolean isOverHeartRateThreshold (double cur) {
        return (cur > heartRateThreshold);
    }

    public boolean isOverRespiratoryRateThreshold(double cur) {
        return (cur > respiratoryRateThreshold);
    }

    public boolean isOverCoreTempThreshold (double cur) {
        return (cur > coreTempThreshold);
    }

    public boolean isOverAmbientTempThreshold (double cur) {
        return (cur > ambientTempThreshold);
    }

    public void addSession (String id) {
        sessionIds.add(id);
    }
}
