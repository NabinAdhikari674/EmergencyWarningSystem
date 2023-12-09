package com.leoindustries.emergencywarningsystem;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button = findViewById(R.id.settingsButton);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

        Intent ewsAlertsHandlerIntent = new Intent(this, ewsAlertsHandler.class);
        ewsAlertsHandlerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ewsAlertsHandlerIntent);

        // Start the API background service
        startService(new Intent(this, ewsBackgroundService.class));

        Log.d("ewsLog: MainActivity", "---------- Main activity started ----------");
    }
}

