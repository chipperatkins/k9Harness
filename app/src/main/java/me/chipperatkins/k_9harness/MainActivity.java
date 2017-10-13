package me.chipperatkins.k_9harness;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // create a dog
        Dog dog = new Dog("chipper");
        dog.ambientTempThreshold = 88.0;
        dog.coreTempThreshold = 102.0;
        dog.respiratoryRateThreshold = 7.0;
        dog.heartRateThreshold = 98.0;

        Session session = new Session("chipper");

        HashMap<String, Object> sessionDoc = new HashMap<>();
        sessionDoc.put("session", session);

        HashMap<String, Object> dogDoc = new HashMap<>();
        dogDoc.put(dog.name, dog);

        HashMap<String, Object> dogSessions = new HashMap<>();

        CbDatabase db = null;
        String sessionId = null;
        String dogId = null;
        String dogSessionsId = null;

        try {
            db = new CbDatabase("testdb", getApplicationContext());

            sessionId = db.create(sessionDoc);
            dogId = db.create(dogDoc);

            ArrayList<String> sessionList = new ArrayList<>();
            sessionList.add(sessionId);
            dogSessions.put("chipper", sessionList);
            dogSessionsId = db.create(dogSessions);
        }
        catch (Exception e) {
            // handle here...
        }

        Map<String, Object> chipper = db.retrieve(dogId);
        Dog testDog = Dog.toDog((Map<String, Object>) chipper.get("chipper"));
        Object sessionIds = db.retrieve(dogSessionsId);
        Object sessionObj = db.retrieve(sessionId);
        String name = testDog.name;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
