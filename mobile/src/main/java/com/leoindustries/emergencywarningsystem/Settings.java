package com.leoindustries.emergencywarningsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences preferences = getSharedPreferences("com.leoindustries.emergencywarningsystem.Preferences", Context.MODE_PRIVATE);

        ImageButton button = findViewById(R.id.backToHomeButton);
        SwitchCompat notificationSwitch = findViewById(R.id.notificationToggleSwitch);

        boolean allowNotifications = preferences.getBoolean("allowNotifications", false);
        notificationSwitch.setChecked(allowNotifications);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
        });
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            if (isChecked) {
                editor.putBoolean("allowNotifications", true);
                editor.apply();
                Log.d("ewsLog: Settings", "Toggled to allow notifications: switch is ON");
            } else {
                editor.putBoolean("allowNotifications", false);
                editor.apply();
                Log.d("ewsLog: Settings", "Toggled to NOT allow notifications: switch is OFF");
            }
        });

        Log.d("ewsLog: Settings", "---------- Settings activity started ----------");
    }
}
