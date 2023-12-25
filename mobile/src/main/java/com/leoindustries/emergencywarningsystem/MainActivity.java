package com.leoindustries.emergencywarningsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
//    static RecyclerView alertRecyclerView;
    ViewPager viewPager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    JSONObject currentData;

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TabFragment1(), "30 Minutes");
        viewPagerAdapter.addFragment(new TabFragment2(), "1 Hour");
        viewPagerAdapter.addFragment(new TabFragment3(), "2 Hours");
        viewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        preferences = getSharedPreferences("com.leoindustries.emergencywarningsystem.UI", Context.MODE_PRIVATE);
        editor = preferences.edit();

        ImageButton button = findViewById(R.id.settingsButton);
        button.setOnClickListener(view -> {
            Log.d("ewsLog: MainActivity", "Settings Button clicked");
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

        // Start the API background service
        startService(new Intent(this, ewsBackgroundService.class));

        Log.d("ewsLog: MainActivity", "---------- Main activity started ----------");
    }

    private final BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), ewsAlertsHandler.ACTION_UPDATE_UI)) {
                try {
                    currentData = new JSONObject(Objects.requireNonNull(intent.getStringExtra(ewsAlertsHandler.EXTRA_DATA_MODEL)));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                int currentItem = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + currentItem);

                if (activeFragment instanceof UpdatableFragment) {
                    if(currentItem == 0) {
                        try {
                            ((UpdatableFragment) activeFragment).updateTabData(String.valueOf(currentData.getJSONArray("min30")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(currentItem == 1) {
                        try {
                            ((UpdatableFragment) activeFragment).updateTabData(String.valueOf(currentData.getJSONArray("hour1")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(currentItem == 2) {
                        try {
                            ((UpdatableFragment) activeFragment).updateTabData(String.valueOf(currentData.getJSONArray("hour2")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateUIReceiver, new IntentFilter(ewsAlertsHandler.ACTION_UPDATE_UI));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateUIReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ewsLog: MainActivity", "Main Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}


