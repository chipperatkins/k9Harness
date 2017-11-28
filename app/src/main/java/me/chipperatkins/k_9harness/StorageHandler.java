package me.chipperatkins.k_9harness;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by patrickatkins on 10/17/17.
 */

// Handles Storage Functions needed by the app to interface with the Db wrapper
class StorageHandler {
    // list of dogs keyed by name
    private static String dogDocumentId;

    // list of sessions
    private static String sessionDocumentId;

    // format for session date keys
    private static DateFormat dateFormat;

    // db object
    private static CbDatabase database;

    StorageHandler(android.content.Context ctx) {
        LinkedHashMap<String, Object> dogDocument = new LinkedHashMap<>();
        LinkedHashMap<String, Object> sessionDocument = new LinkedHashMap<>();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);

        try {
            database = new CbDatabase("maindb", ctx);
            dogDocumentId = database.create(dogDocument);
            sessionDocumentId = database.create(sessionDocument);
        }
        catch (Exception e) {
            // handle here
        }
    }

    StorageHandler() {}

    // stores dog as a sub-map into the dogDocument
    void storeDog(Dog dog) {
        LinkedHashMap<String, Object> dogMap = Dog.fromDog(dog);
        try {
            database.update(dog.name, dogMap, dogDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // retrieves a dog by name
    Dog retrieveDog(String name) {
        Map<String, Object> dogDocument = database.retrieve(dogDocumentId);
        Map dogMap = (Map) dogDocument.get(name);

        return Dog.toDog(dogMap);
    }

    // retrieve all dogs
    Map<String, Object> retrieveAllDogs() {
        return database.retrieve(dogDocumentId);
    }

    // update dog heart rate threshold
    void updateHeartRateThreshold (Dog dog, Double value) {
        // get dog
        Map<String, Object> dogDocument = database.retrieve(dogDocumentId);
        Map dogMap = (Map) dogDocument.get(dog.name);

        // update dog
        Dog d = Dog.toDog(dogMap);
        d.heartRateThreshold = value;

        // update session in db
        try {
            database.update(dog.name, Dog.fromDog(d), dogDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update dog respiratory rate threshold
    void updateRespiratoryRateThreshold(Dog dog, Double value) {
        // get dog
        Map<String, Object> dogDocument = database.retrieve(dogDocumentId);
        Map dogMap = (Map) dogDocument.get(dog.name);

        // update dog
        Dog d = Dog.toDog(dogMap);
        d.respiratoryRateThreshold = value;

        // update session in db
        try {
            database.update(dog.name, Dog.fromDog(d), dogDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update dog abdominal temp threshold
    void updateAmbientTempThreshold (Dog dog, Double value) {
        // get dog
        Map<String, Object> dogDocument = database.retrieve(dogDocumentId);
        Map dogMap = (Map) dogDocument.get(dog.name);

        // update dog
        Dog d = Dog.toDog(dogMap);
        d.abdominalTempThreshold = value;

        // update session in db
        try {
            database.update(dog.name, Dog.fromDog(d), dogDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update dog core temp threshold
    void updateCoreTempThreshold (Dog dog, Double value) {
        // get dog
        Map<String, Object> dogDocument = database.retrieve(dogDocumentId);
        Map dogMap = (Map) dogDocument.get(dog.name);

        // update dog
        Dog d = Dog.toDog(dogMap);
        d.coreTempThreshold = value;

        // update session in db
        try {
            database.update(dog.name, Dog.fromDog(d), dogDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // store a session and update the corresponding dog's list of session id's
    void storeSessionAndUpdateDog(Session session) {
        LinkedHashMap<String, Object> sessionMap = Session.fromSession(session);
        String sessionId = dateFormat.format((Date) sessionMap.get("startDate"));
        String dogName = (String) sessionMap.get("dogName");

        // update session document & appropriate dog in dog document
        try {
            // store session
            database.update(sessionId, sessionMap, sessionDocumentId);

            //get dog document
            Map<String, Object> dogDocument = database.retrieve(dogDocumentId);

            // get dog from dog document
            Map dogMap = (Map) dogDocument.get(dogName);

            // convert to dog and add session id
            Dog dog = Dog.toDog(dogMap);
            dog.sessions.add(sessionId);

            // convert to map and update Database
            dogMap = Dog.fromDog(dog);
            database.update(dog.name, dogMap, dogDocumentId);
        }
        catch (Exception e)
        {
            // handle here
        }
    }

    // retrieves by sessionId, formatted by dateFormat
    Session retrieveSession(String sessionId) {
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map sessionMap = (Map) sessionDocument.get(sessionId);

        return Session.toSession(sessionMap);
    }

    // retrieves all sessions
    Map<String, Object> retrieveAllSessions() {
        return database.retrieve(sessionDocumentId);
    }

    // update session heart rate
    void updateHeartRate (Session session, Date date, Double value) {
        // get session
        String sessionId = dateFormat.format(session.startDate);
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map sessionMap = (Map) sessionDocument.get(sessionId);

        // update session
        Session ses = Session.toSession(sessionMap);
        ses.addHeartRate(dateFormat.format(date), value);

        // update session in db
        try {
            database.update(sessionId, Session.fromSession(ses), sessionDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update session respiratory rate
    void updateRespiratoryRate (Session session, Date date, Double value) {
        // get session
        String sessionId = dateFormat.format(session.startDate);
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map sessionMap = (Map) sessionDocument.get(sessionId);

        // update session
        Session ses = Session.toSession(sessionMap);
        ses.addRespiratoryRate(dateFormat.format(date), value);

        // update session in db
        try {
            database.update(sessionId, Session.fromSession(ses), sessionDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update session core temp
    void updateCoreTemp (Session session, Date date, Double value) {
        // get session
        String sessionId = dateFormat.format(session.startDate);
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map sessionMap = (Map) sessionDocument.get(sessionId);

        // update session
        Session ses = Session.toSession(sessionMap);
        ses.addCoreTemp(dateFormat.format(date), value);

        // update session in db
        try {
            database.update(sessionId, Session.fromSession(ses), sessionDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update session ambient temp
    void updateAmbientTemp (Session session, Date date, Double value) {
        // get session
        String sessionId = dateFormat.format(session.startDate);
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map sessionMap = (Map) sessionDocument.get(sessionId);

        // update session
        Session ses = Session.toSession(sessionMap);
        ses.addAmbientTemp(dateFormat.format(date), value);

        // update session in db
        try {
            database.update(sessionId, Session.fromSession(ses), sessionDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // update session abdominal temp
    void updateAbdominalTemp (Session session, Date date, Double value) {
        // get session
        String sessionId = dateFormat.format(session.startDate);
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map sessionMap = (Map) sessionDocument.get(sessionId);

        // update session
        Session ses = Session.toSession(sessionMap);
        ses.addAbdominalTemp(dateFormat.format(date), value);

        // update session in db
        try {
            database.update(sessionId, Session.fromSession(ses), sessionDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // ends a session
    void endSession (Session session) {
        Session.endSession(session);
        try {
            database.update(dateFormat.format(session.startDate), Session.fromSession(session), sessionDocumentId);
        }
        catch (Exception e) {
            // handle here
        }
    }
}
