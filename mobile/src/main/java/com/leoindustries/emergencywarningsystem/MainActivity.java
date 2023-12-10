package com.leoindustries.emergencywarningsystem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION_UPDATE_UI = "com.leoindustries.emergencywarningsystem.UPDATE_UI";
    public static final String EXTRA_DATA_MODEL = "com.leoindustries.emergencywarningsystem.DATA_MODEL";

    static RecyclerView alertRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertRecyclerView = findViewById(R.id.alertRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        alertRecyclerView.setLayoutManager(layoutManager);

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

    private BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ewsAlertsHandler.ACTION_UPDATE_UI)) {
                String data = intent.getStringExtra(ewsAlertsHandler.EXTRA_DATA_MODEL);
                ewsAlertsHandler.updateUIWithData(data, alertRecyclerView);
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

    private boolean isSettingsActivityRunning() {
//        return SettingsActivity.isRunning();
        return false;
    }

    private void bringSettingsActivityToFront() {
//        SettingsActivity.bringToFront();
    }
}


