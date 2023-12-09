package com.leoindustries.emergencywarningsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

class alertViewHolder extends RecyclerView.ViewHolder {
    public TextView alertTitle;
    public TextView alertDescription;
    public TextView alertDatePublished;

    public alertViewHolder(@NonNull View itemView) {
        super(itemView);

        alertTitle = itemView.findViewById(R.id.alertTitle);
        alertDescription = itemView.findViewById(R.id.alertDescription);
        alertDatePublished = itemView.findViewById(R.id.alertDatePublished);
    }
}

class alertAdapter extends RecyclerView.Adapter<alertViewHolder> {
    private ewsAlertDataModel data;

    public alertAdapter(ewsAlertDataModel responseData) {
        this.data = responseData;
    }

    @NonNull
    @Override
    public alertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert, parent, false);
        return new alertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull alertViewHolder holder, int position) {
        String alertTitle = data.getName();
        holder.alertTitle.setText(alertTitle);

        String alertDescription = data.getDescription();
        holder.alertDescription.setText(alertDescription);

        String alertDatePublished = data.getDatePublished();
        holder.alertDatePublished.setText(alertDatePublished);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}

public class ewsAlertsHandler extends AppCompatActivity {
    public static final String ACTION_UPDATE_UI = "com.leoindustries.emergencywarningsystem.UPDATE_UI";
    public static final String EXTRA_DATA_MODEL = "com.leoindustries.emergencywarningsystem.DATA_MODEL";
    RecyclerView alertRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(updateUIReceiver, new IntentFilter(ewsAlertsHandler.ACTION_UPDATE_UI));

        alertRecyclerView = findViewById(R.id.alertRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        alertRecyclerView.setLayoutManager(layoutManager);
    }

    private BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ewsAlertsHandler.ACTION_UPDATE_UI)) {
                // Handle the broadcast and update UI
                ewsAlertDataModel data = intent.getParcelableExtra(ewsAlertsHandler.EXTRA_DATA_MODEL);
                updateUIWithData(data);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver in onDestroy to avoid memory leaks
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateUIReceiver);
    }

    private void updateUIWithData(ewsAlertDataModel data) {
        Log.d("ewsLog: ewsAlertsHandler", "Data: " + data.getName());
        alertAdapter adapter = new alertAdapter(data);
        if(alertRecyclerView != null) {
            alertRecyclerView.setAdapter(adapter);
            Log.d("ewsLog: ewsAlertsHandler", "alertRecyclerView is NOT null on updateUIWithData !");
        }
        else {
            Log.e("ewsLog: ewsAlertsHandler", "alertRecyclerView is null onCreate on updateUIWithData!");
        }
    }
}
