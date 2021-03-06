package com.ermainz.timezor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
//import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, TimerRunningFragment.SaveTimerListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    SharedPreferences mSharedPreferences;
    List<Timer> savedTimerList;
    FragmentManager fm;
    String savedListFragmentTag;
    ActionBar actionBar;
    SavedListFragment f;

    public void saveTimer(long seconds, String label) {
        Timer newTimer = new Timer(seconds, label);
        savedTimerList.add(newTimer);
        if( f != null){
            f.addNewData(newTimer);
            //mViewPager.setCurrentItem(0);
            actionBar.setSelectedNavigationItem(1);
            //Log.e("MESSAGE", "F NOT NULL");
        }
        else {
            //Log.e("MESSAGE", "F NULL");
        }
    }

    public void loadTimerList(){
        //savedTimerList.add(new Timer(Long.parseLong("3"), "Ramen"));
        //savedTimerList.add(new Timer(Long.parseLong("30"), "Laundry"));

        String default_value = "";
        String savedString = mSharedPreferences.getString(getString(R.string.saved_timers_key), default_value);
        if (savedString.equals(default_value)) {
            return;
        }
        String[] timers = savedString.split(";");
        for(String s : timers){
            String[] timer = s.split(",");
            savedTimerList.add(new Timer(Long.valueOf(timer[1]), timer[0]));
        }
    }

    public void saveTimerList(){
        String saveString = "";
        for(Timer t : f.mTimerList){
        //for(Timer t : savedTimerList){
            saveString = saveString + t.label + "," + Long.toString(t.seconds) + ";";
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.saved_timers_key), saveString);
        editor.commit();
    }

    public MainActivity(){
        savedTimerList = new ArrayList<Timer>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        loadTimerList();

        setContentView(R.layout.activity_main);

        // Set up the action bar.
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        fm = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fm, this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveTimerList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        /* http://stackoverflow.com/questions/7723964/replace-fragment-inside-a-viewpager */
        private final class TimerPageListener implements TimerPageFragmentListener {
            public void onSwitchTimerFragment(Long time){
                if (timerPageFragment instanceof TimerCreateFragment){
                    mFragmentManager.beginTransaction().remove(timerPageFragment).commit();
                    timerPageFragment = new TimerRunningFragment(mContext, listener, time, mFragmentManager);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onStartSavedTimer(Long time) {
                actionBar.setSelectedNavigationItem(0);
                onSwitchTimerFragment(time);
            }

            @Override
            public void onStopTimer() {
                mFragmentManager.beginTransaction().remove(timerPageFragment).commit();
                if (timerPageFragment instanceof TimerRunningFragment){
                    timerPageFragment = new TimerCreateFragment(listener);
                }
                notifyDataSetChanged();
            }
        }

        Context mContext;
        TimerPageListener listener = new TimerPageListener();
        //TimerCreateFragment createFragment;
        //TimerRunningFragment runningFragment;
        Fragment timerPageFragment;
        private final FragmentManager mFragmentManager;

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);

            mFragmentManager = fm;
            mContext = context;
            /*change*/
        }




        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
/*            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;*/

            if (position == 0){
                if (timerPageFragment == null){
                    timerPageFragment = new TimerCreateFragment(listener);
                }
                return timerPageFragment;
            }
            else {
                Fragment fr = new SavedListFragment(savedTimerList, listener);
                f = (SavedListFragment) fr;
                savedListFragmentTag = fr.getTag();
                return fr;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public int getItemPosition(Object object){
            if (object instanceof TimerCreateFragment && timerPageFragment instanceof TimerRunningFragment){
                return POSITION_NONE;
            }
            if (object instanceof TimerRunningFragment && timerPageFragment instanceof TimerCreateFragment){
                return POSITION_NONE;
            }
            return POSITION_UNCHANGED;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public interface TimerPageFragmentListener {
        public void onSwitchTimerFragment(Long time);
        public void onStartSavedTimer(Long time);
        public void onStopTimer();
    }

}
