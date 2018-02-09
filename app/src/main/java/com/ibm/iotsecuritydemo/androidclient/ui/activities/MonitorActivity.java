package com.ibm.iotsecuritydemo.androidclient.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ibm.iotsecuritydemo.androidclient.R;
import com.ibm.iotsecuritydemo.androidclient.core.DeviceIoTDemoApplication;
import com.ibm.iotsecuritydemo.androidclient.core.MonitoredDevicesInformation;
import com.ibm.iotsecuritydemo.androidclient.ui.fragments.MonitorFragment;

public class MonitorActivity extends AppCompatActivity {

    private static final String TAG = MonitorActivity.class.getSimpleName();

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
        setContentView(R.layout.activity_monitor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(),
                DeviceIoTDemoApplication.get().getMonitoredDevicesInformation());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        for(int position = 0; position < mSectionsPagerAdapter.getCount(); position++) {
            tabLayout.addTab(
                    tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(position))
            );
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /**
        NoticeHelper noticeHelper;

        NoticeHelper.AuthState authState =
                (NoticeHelper.AuthState) getIntent().getSerializableExtra("auth-state");

        if(NoticeHelper.AuthState.new_guest == authState || NoticeHelper.AuthState.returning_guest == authState) {

            DeviceIoTDemoApplication.get().setMonitoredDevicesInformation(
                    new Gson().fromJson(DeviceIoTDemoApplication.DUMMY_DEVICE_LIST, MonitoredDevicesInformation.class)
            );

            Log.i(TAG, "Starting MonitorActivity: Response=" + DeviceIoTDemoApplication.DUMMY_DEVICE_LIST);
        }
         **/

        /**
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        **/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monitor, menu);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private MonitoredDevicesInformation devicesInformation;

        public SectionsPagerAdapter(FragmentManager fm, MonitoredDevicesInformation devicesInformation) {
            super(fm);
            this.devicesInformation = devicesInformation;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MonitorFragment.newInstance(devicesInformation.devices[position]);
        }

        @Override
        public int getCount() {
            return devicesInformation.devices.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("SectionsPagerAdapter", "Setting title for tab at position "+position);
            if ((position >= 0) && (position < devicesInformation.devices.length)) {
                Log.d("SectionsPagerAdapter", "Setting title for tab as "+devicesInformation.devices[position]);
                return devicesInformation.devices[position];
            }

            return null;
        }
    }
}
