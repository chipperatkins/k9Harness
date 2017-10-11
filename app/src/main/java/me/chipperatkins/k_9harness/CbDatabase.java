package me.chipperatkins.k_9harness;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.Map;

/**
 * Created by patrickatkins on 10/10/17.
 */

/** Database Wrapper **/
public class CbDatabase {
    private Database database;
    private Manager manager;

    /** Ctor Setup **/
    public CbDatabase(String dname, Context ctx) throws IOException, CouchbaseLiteException {

        // 1. use fefault settings for now (read/write access)
        manager = new Manager(new AndroidContext(ctx), Manager.DEFAULT_OPTIONS);

        // 2. Check database name given by user
        // No upper case allowed in CBL!
        if (! Manager.isValidDatabaseName(dname)) {
            // report ..
            return;
        }

        // 3. Get existing db with that name
        // create one if it doesn't exist
        database = manager.getDatabase(dname);
    }

    public void close() {
        if (manager != null) {
            manager.close();
        }
    }

    /** C-rud */
    public String create( Map<String, Object> docContent) throws CouchbaseLiteException {

        // create an empty document
        Document doc = database.createDocument();

        // add content to document and save it
        doc.putProperties(docContent);
        return doc.getId();
    }

    /** c-R-ud */
    public Map<String, Object> retrieve(String docId) {
        return database.getDocument(docId).getProperties();
    }

    /** cr-U-d (auto-retry) */
    public void update(final String key, final Object value, String docId)
            throws CouchbaseLiteException {

        //retrieve document from the database
        Document doc = database.getDocument(docId);
        doc.update(new Document.DocumentUpdater() {

            /** This may be called more than once */
            @Override
            public boolean update (UnsavedRevision newRevision) {
                Map<String, Object> properties = newRevision.getUserProperties();
                properties.put(key, value);
                newRevision.setUserProperties(properties);
                return true;
            }
        });
    }

    /** cru-D */
    public boolean delete(String docId) throws CouchbaseLiteException {
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        // delete the doc
        doc.delete();
        return doc.isDeleted();
    }
}