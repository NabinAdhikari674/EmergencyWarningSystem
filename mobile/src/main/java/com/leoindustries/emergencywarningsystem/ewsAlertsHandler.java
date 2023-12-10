package com.leoindustries.emergencywarningsystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private JSONArray data;

    public alertAdapter(JSONArray responseData) {
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
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject jsonObject = data.getJSONObject(i);
                String alertTitle = jsonObject.getString("location_name");
                holder.alertTitle.setText(alertTitle);

                String alertDescription = jsonObject.getString("msg");;
                holder.alertDescription.setText(alertDescription);

                String alertDatePublished = jsonObject.getString("create_date");
                holder.alertDatePublished.setText(alertDatePublished);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public int getItemCount() {
        return data.length();
    }

}

public class ewsAlertsHandler extends AppCompatActivity {
    public static final String ACTION_UPDATE_UI = "com.leoindustries.emergencywarningsystem.UPDATE_UI";
    public static final String EXTRA_DATA_MODEL = "com.leoindustries.emergencywarningsystem.DATA_MODEL";

    public static void updateUIWithData(String rawData, RecyclerView alertRecyclerView) {
        Log.d("ewsLog: ewsAlertsHandler", "Updating UI with new data");
        try {
            JSONObject data = new JSONObject(rawData);
            JSONArray rowArray = data.getJSONArray("DisasterMsg")
                    .getJSONObject(1)
                    .getJSONArray("row");

            alertAdapter adapter = new alertAdapter(rowArray);
            if(alertRecyclerView != null) {
                alertRecyclerView.setAdapter(adapter);
            }
            else {
                Log.w("ewsLog: ewsAlertsHandler", "alertRecyclerView is NULL on ewsAlertsHandler.updateUIWithData");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
