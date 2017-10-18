package me.chipperatkins.k_9harness;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by patrickatkins on 10/17/17.
 */

// Handles Storage Functions needed by the app to interface with the Db wrapper
public class StorageHandler {
    // list of dogs keyed by name
    private String dogDocumentId;

    // list of sessions
    private String sessionDocumentId;

    // format for session date keys
    private DateFormat dateFormat;

    // db object
    private CbDatabase database;

    public StorageHandler (android.content.Context ctx) {
        LinkedHashMap<String, Object> dogDocument = new LinkedHashMap<>();
        LinkedHashMap<String, Object> sessionDocument = new LinkedHashMap<>();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        try {
            database = new CbDatabase("maindb", ctx);
            dogDocumentId = database.create(dogDocument);
            sessionDocumentId = database.create(sessionDocument);
        }
        catch (Exception e) {
            // handle here
        }
    }

    // stores dog as a sub-map into the dogDocument
    public void storeDog (Dog dog) {
        LinkedHashMap<String, Object> dogMap = Dog.fromDog(dog);
        try {
            database.update(dog.name, dogMap, dogDocumentId);
        }
        catch (Exception e) {
            String x = "got here";
        }
    }

    // retrieves a dog by name
    public Dog retrieveDog (String name) {
        Map<String, Object> dogDocument = database.retrieve(dogDocumentId);
        Map<String, Object> dogMap = (Map) dogDocument.get(name);
        Dog dog = Dog.toDog(dogMap);

        return dog;
    }

    // retrieve all dogs
    public Map<String, Object> retrieveAllDogs() {
        return database.retrieve(dogDocumentId);
    }

    // store a session and update the corresponding dog's list of session id's
    public void storeSessionAndUpdateDog (Session session) {
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
            Map<String, Object> dogMap = (Map) dogDocument.get(dogName);

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

    // retrieves by sessionId, formatted by dateformat
    public Session retrieveSession (String sessionId) {
        Map<String, Object> sessionDocument = database.retrieve(sessionDocumentId);
        Map<String, Object> sessionMap = (Map) sessionDocument.get(sessionId);
        Session session = Session.toSession(sessionMap);

        return session;
    }

    // retrieves all sessions
    public Map<String, Object> retrieveAllSessions() {
        return database.retrieve(sessionDocumentId);
    }

    // update session
}
