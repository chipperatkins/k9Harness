package me.chipperatkins.k_9harness;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewParent;
import android.widget.TextView;

import android.content.Intent;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainGraphActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_graph);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            getSupportActionBar().setTitle("Dog Name");


            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle(prefs.getString("current_dog", "Dog Name"));
            }
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Intent intent = getIntent();
        int startPage = intent.getIntExtra("startPage", 0);
        mViewPager.setCurrentItem(startPage);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Marked.", Snackbar.LENGTH_LONG)
                        .setAction("Add a note?", new AddMemoListener()).show();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(prefs.getString("current_dog", "Dog Name"));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_graph, menu);
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
            Intent intent = new Intent(MainGraphActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NAME = "section_name";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NAME, getPageTitle(sectionNumber));
            fragment.setArguments(args);
            return fragment;
        }

        public static String getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "Heart Rate";
                case 2:
                    return "Respiratory Rate";
                case 3:
                    return "Core Temperature";
                case 4:
                    return "Ambient Temperature";
            }
            return null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_main_graph, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getArguments().getString(ARG_SECTION_NAME));

            GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

            graph.getViewport().setScrollable(true);
            graph.getViewport().setScrollableY(true);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);

            graph.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    ViewParent parent = v.getParent();
                    parent.requestDisallowInterceptTouchEvent(true);
                    return v.onTouchEvent(event);
                }
            });

            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);
            updateGraph();
        }

        private void updateGraph(){
            String sectionName = getArguments().getString(ARG_SECTION_NAME);
            GraphView graph = (GraphView) getView().findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            if(sectionName.equals("Heart Rate")) {
                series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });
            }
            else if(sectionName.equals("Respiratory Rate")){
                series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 1),
                        new DataPoint(1, 2),
                        new DataPoint(2, 3),
                        new DataPoint(3, 4),
                        new DataPoint(4, 5)
                });
            }
            else if(sectionName.equals("Core Temperature")){
                series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 5),
                        new DataPoint(1, 4),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 1)
                });
            }
            else if(sectionName.equals("Ambient Temperature")){
                series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 1),
                        new DataPoint(1, 1),
                        new DataPoint(2, 5),
                        new DataPoint(3, 1),
                        new DataPoint(4, 1)
                });
            }

            graph.addSeries(series);


        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Heart Rate";
                case 1:
                    return "Respiratory Rate";
                case 2:
                    return "Core Temperature";
                case 3:
                    return "Ambient Temperature";
            }
            return null;
        }



    }
}
