package me.chipperatkins.k_9harness;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public ArrayList<String> sessions;

    // Constructor
    public Dog(String nname) {
        name = nname;
        heartRateThreshold = null;
        respiratoryRateThreshold = null;
        coreTempThreshold = null;
        ambientTempThreshold = null;
        sessions = new ArrayList<>();
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
        dog.sessions = (ArrayList<String>) map.get("sessions");

        return dog;
    }

    // Converts to Map from Dog
    public static LinkedHashMap<String, Object> fromDog(Dog dog) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("name", dog.name);
        map.put("heartRateThreshold", dog.heartRateThreshold);
        map.put("respiratoryRateThreshold", dog.respiratoryRateThreshold);
        map.put("coreTempThreshold", dog.coreTempThreshold);
        map.put("ambientTempThreshold", dog.ambientTempThreshold);
        map.put("sessions", dog.sessions);

        return map;
    }
}
