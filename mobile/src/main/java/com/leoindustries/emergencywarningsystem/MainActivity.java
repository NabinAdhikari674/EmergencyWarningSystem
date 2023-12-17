package com.leoindustries.emergencywarningsystem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    static RecyclerView alertRecyclerView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("com.leoindustries.emergencywarningsystem.UI", Context.MODE_PRIVATE);
        editor = preferences.edit();

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

    private final BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), ewsAlertsHandler.ACTION_UPDATE_UI)) {
                currentData = intent.getStringExtra(ewsAlertsHandler.EXTRA_DATA_MODEL);
                ewsAlertsHandler.updateUIWithData(currentData, alertRecyclerView);
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
        String savedData = preferences.getString("alertsData", "");
        if(!savedData.equals("")) {
            ewsAlertsHandler.updateUIWithData(savedData, alertRecyclerView);
        }
//        Log.d("ewsLog: MainActivity", "---------- resumed with data ----------: " + savedData);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        editor.putString("alertsData", currentData);
//        editor.apply();
//        Log.d("ewsLog: MainActivity", "---------- pause ----------:" + currentData);
    }


}


