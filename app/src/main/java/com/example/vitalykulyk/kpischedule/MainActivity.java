package com.example.vitalykulyk.kpischedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {//implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer mDrawerFragment;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    String day;
    EditText search_query;
    android.app.FragmentTransaction mFragmentTransaction;
    android.app.FragmentManager mFragmentManager;
    boolean isPressed = false;
    boolean isPressedSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        search_query = (EditText) findViewById(R.id.editText);
        search_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPressedSearch == false) {
                    search_query.setText("");
                }
                isPressedSearch = true;
            }
        });
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

//        if (id == R.id.action_search){
//            ScheduleFragment.ScheduleTask scheduleTask = new ScheduleFragment.ScheduleTask();
//            String query = String.valueOf(search_query.getText());
//            scheduleTask.execute(query, day);
//            if (!isPressed) {
//                mFragmentTransaction = mFragmentManager.beginTransaction();
//                if (id == R.id.action_search) {
////                    ScheduleFragment schFragment = new ScheduleFragment();
////                    mFragmentTransaction.add(R.id.schedule_fragment, schFragment);
////                    mFragmentTransaction.commit();
//                }
//                isPressed = true;
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onDrawerItemSelected(View view, int position) {
//        displayView(position);
//    }

//    private void displayView(int position) {
//        ScheduleFragment fragment = new ScheduleFragment();
//        String title = getString(R.string.app_name);
//        switch (position) {
//            case 0:
//                fragment = new ScheduleFragment();
//                title = getString(R.string.title_home);
//                break;
//            case 1:
//                fragment = new ScheduleFragment();
//                title = getString(R.string.title_friends);
//                break;
//            case 2:
//                fragment = new ScheduleFragment();
//                title = getString(R.string.title_messages);
//                break;
//            default:
//                break;
//        }

//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            //fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//            // set the toolbar title
//            getSupportActionBar().setTitle(title);
//        }


    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return ScheduleFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.mon);
                case 1:
                    return getString(R.string.tue);
                case 2:
                    return getString(R.string.wed);
                case 3:
                    return getString(R.string.thu);
                case 4:
                    return getString(R.string.fri);
                case 5:
                    return getString(R.string.sat);
                case 6:
                    return getString(R.string.sun);
            }
            return null;
        }
    }
}
