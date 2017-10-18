package me.chipperatkins.k_9harness;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by patrickatkins on 10/10/17.
 */

class Session {
    Date startDate;
    private Date endTime;
    private String dog;

    private Map<String, Double> heartRate;
    private Map<String, Double> respiratoryRate;
    private Map<String, Double> coreTemp;
    private Map<String, Double> ambientTemp;

    Session(String dogg) {
        startDate = new Date();
        endTime = null;
        dog = dogg;

        heartRate = new LinkedHashMap<>();
        respiratoryRate = new LinkedHashMap<>();
        coreTemp = new LinkedHashMap<>();
        ambientTemp = new LinkedHashMap<>();
    }

    boolean isActive() {
        return (endTime == null);
    }

    // Adds value to heart rate map
    void addHeartRate(String time, Double value) {
        heartRate.put(time, value);
    }

    // Adds value to respiratory rate map
    void addRespiratoryRate(String time, Double value) {
        respiratoryRate.put(time, value);
    }

    // Adds value to coreTemp map
    void addCoreTemp(String time, Double value) {
        coreTemp.put(time, value);
    }

    // Adds value to ambientTemp map
    void addAmbientTemp(String time, Double value) {
        ambientTemp.put(time, value);
    }

    // ends session
    static void endSession(Session session) {
        session.endTime = new Date();
    }

    // Converts Java Type Session class to Map
    static LinkedHashMap<String, Object> fromSession (Session session) {
        LinkedHashMap<String, Object> sessionMap = new LinkedHashMap<>();
        sessionMap.put("startDate", session.startDate);
        sessionMap.put("endTime", session.endTime);
        sessionMap.put("dogName", session.dog);
        sessionMap.put("heartRate", session.heartRate);
        sessionMap.put("respiratoryRate", session.respiratoryRate);
        sessionMap.put("coreTemp", session.coreTemp);
        sessionMap.put("ambientTemp", session.ambientTemp);

        return sessionMap;
    }

    // Converts map to Java Class Type Session
    static Session toSession(Map<String, Object> map) {
        Session session = new Session((String) map.get("dogName"));
        session.startDate = (Date) map.get("startDate");
        session.endTime = (Date) map.get("endTime");
        session.heartRate = (Map<String, Double>) map.get("heartRate");
        session.respiratoryRate = (Map<String, Double>) map.get("respiratoryRate");
        session.coreTemp = (Map<String, Double>) map.get("coreTemp");
        session.ambientTemp = (Map<String, Double>) map.get("ambientTemp");

        return session;
    }
}
