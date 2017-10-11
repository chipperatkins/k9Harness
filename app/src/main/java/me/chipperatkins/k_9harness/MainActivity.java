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
        Map<String, Object> dog = new HashMap<String, Object>();
        ArrayList<String> sessions = new ArrayList<String>();
        dog.put("name", "Chester");
        dog.put("hrThreshhold", 80);
        dog.put("rrThreshhold", 99);
        dog.put("ctThreshhold", 103);
        dog.put("atThreshhold", null);
        dog.put("sessions", sessions);

        // create a session
        Date now = new Date();
        String nowString = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now);

        Map<String, Double> heartRate = new HashMap<String, Double>();
        heartRate.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 88.0);
        heartRate.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 89.0);
        heartRate.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 88.5);
        heartRate.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 84.0);

        Map<String, Double> coreTemp = new HashMap<String, Double>();
        coreTemp.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 88.0);
        coreTemp.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 89.0);
        coreTemp.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 88.5);
        coreTemp.put(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now), 84.0);

        // etc. do this for RR and AT as well

        Map<String, Object> docContent = new HashMap<String, Object>();
        docContent.put("date", DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now));
        docContent.put("endTime", null);
        docContent.put("heartRate", heartRate);
        docContent.put("coreTemp", coreTemp);

        CbDatabase db = null;
        String docId = null;

        try {
            db = new CbDatabase("testdb", getApplicationContext());

            docId = db.create(docContent);
            sessions.add(docId);
            String dogId = db.create(dog);
        }
        catch (Exception e) {
            // handle here...
        }

        Map<String, Object> hm = db.retrieve(docId);
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
