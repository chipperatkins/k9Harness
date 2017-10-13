package me.chipperatkins.k_9harness;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by patrickatkins on 10/10/17.
 */

public class Dog {
    public String name;
    public Double heartRateThreshold;
    public Double respiratoryRateThreshold;
    public Double coreTempThreshold;
    public Double ambientTempThreshold;

    // Constructor
    public Dog(String nname) {
        name = nname;
        heartRateThreshold = null;
        respiratoryRateThreshold = null;
        coreTempThreshold = null;
        ambientTempThreshold = null;
    }

    // Checks for given heartRate cur over threshold
    public boolean isOverHeartRateThreshold (double cur) {
        return (cur > heartRateThreshold);
    }

    // Check for given respiratory rate cur over threshold
    public boolean isOverRespiratoryRateThreshold(double cur) {
        return (cur > respiratoryRateThreshold);
    }

    // Check for given core temp cur over threshold
    public boolean isOverCoreTempThreshold (double cur) {
        return (cur > coreTempThreshold);
    }

    // Check for given ambient temp cur over threshold
    public boolean isOverAmbientTempThreshold (double cur) {
        return (cur > ambientTempThreshold);
    }

    // Converts Map to Dog Object
    public static Dog toDog(Map<String, Object> map) {
        Dog dog = new Dog((String) map.get("name"));
        dog.heartRateThreshold = (Double) map.get("heartRateThreshold");
        dog.respiratoryRateThreshold = (Double) map.get("respiratoryRateThreshold");
        dog.coreTempThreshold = (Double) map.get("coreTempThreshold");
        dog.ambientTempThreshold = (Double) map.get("ambientTempThreshold");

        return dog;
    }
}
