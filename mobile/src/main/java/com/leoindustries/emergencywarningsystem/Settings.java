package com.leoindustries.emergencywarningsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ImageButton button = findViewById(R.id.backToHomeButton);
        // Set a click listener for the button
        button.setOnClickListener(view -> {
            // Create an Intent to open the target activity
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
