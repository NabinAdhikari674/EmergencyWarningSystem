package com.leoindustries.emergencywarningsystem;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private Context context;
    RecyclerView alertRecyclerView;

    public ewsAlertsHandler(Context context){
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ewsLog: ewsAlertsHandler", "onCreate for ewsAlertsHandler");

        alertRecyclerView = findViewById(R.id.alertRecyclerView);
        if(alertRecyclerView != null) {
            Log.d("ewsLog: ewsAlertsHandler", "alertRecyclerView is NOT null !");
        }
        else {
            Log.e("ewsLog: ewsAlertsHandler", "alertRecyclerView is null onCreate!");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
        alertRecyclerView.setLayoutManager(layoutManager);
    }

    public void handleResponse(ewsAlertDataModel data){
        Log.d("ewsLog: ewsAlertsHandler", "Data: " + data.getName());
        alertAdapter adapter = new alertAdapter(data);
        if(alertRecyclerView != null){
            alertRecyclerView.setAdapter(adapter);
        }
        else{
            Log.e("ewsLog: ewsAlertsHandler", "alertRecyclerView is null !");
        }
    }
}
