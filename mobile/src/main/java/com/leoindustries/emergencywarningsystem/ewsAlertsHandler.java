package com.leoindustries.emergencywarningsystem;

import android.annotation.SuppressLint;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        try {
            JSONObject jsonObject = data.getJSONObject(position);

            String alertTitle = jsonObject.getString("location_name");
            String locationId = jsonObject.getString("location_id");
            holder.alertTitle.setText(String.format("%s (%s)", alertTitle, locationId));

            String alertDatePublished = jsonObject.getString("create_date");
            String duration = jsonObject.getString("duration");
            holder.alertDatePublished.setText(String.format("%s (%s)", duration, alertDatePublished));

            String alertDescription = jsonObject.getString("msg");
            holder.alertDescription.setText(alertDescription);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Change NotifyDataSetChanged to be specific and not this general
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(JSONArray newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.length();
    }


}

public class ewsAlertsHandler extends AppCompatActivity {
    public static final String ACTION_UPDATE_UI = "com.leoindustries.emergencywarningsystem.UPDATE_UI";
    public static final String EXTRA_DATA_MODEL = "com.leoindustries.emergencywarningsystem.DATA_MODEL";

    public static JSONObject separateAlertData(JSONObject data){
        ZoneId koreaTimeZone = ZoneId.of("Asia/Seoul");
        ZonedDateTime koreanNow = ZonedDateTime.now(koreaTimeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.parse(koreanNow.format(formatter), formatter);

        JSONArray min30 = new JSONArray();
        JSONArray hour1 = new JSONArray();
        JSONArray hour2 = new JSONArray();

        JSONObject finalData = new JSONObject();

        Log.d("ewsLogL alert", "Now: " + now.toString());
        try {
            JSONArray rowArray = data.getJSONArray("DisasterMsg")
                    .getJSONObject(1)
                    .getJSONArray("row");

            for (int i = 0 ; i < rowArray.length(); i++) {
                JSONObject row = rowArray.getJSONObject(i);
                LocalDateTime createDate = LocalDateTime.parse(row.getString("create_date"), formatter);

                Duration duration = Duration.between(createDate, now);
                long seconds = duration.getSeconds();
                float hour = (float) seconds / 3600;

                if(hour <= 0.3) {
                    row.put("duration", (int) (hour * 60) + " min ago");
                    min30.put(row);
                }
                else if (hour <= 1 && hour > 0.3) {
                    row.put("duration", (int) hour + " hour ago");
                    hour1.put(row);
                } else {
                    row.put("duration", (int) hour + " hours ago");
                    hour2.put(row);
//                    Log.d("ewsLog: ewsAlertSeparator", hour + " h2 row: " + row.toString());
                }
            }
            finalData.put("min30", min30);
            finalData.put("hour1", hour1);
            finalData.put("hour2", hour2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return finalData;
    }

    public static void updateUIWithData(String rawData, alertAdapter adapter) {
//        Log.d("ewsLog: ewsAlertsHandler", "Updating UI with data");
        if(!rawData.isEmpty()){
            try {
                JSONArray array = new JSONArray(rawData);

                if(adapter != null) {
                    adapter.updateData(array);
                }
                else {
                    Log.w("ewsLog: ewsAlertsHandler", "alertRecyclerView is NULL on ewsAlertsHandler.updateUIWithData");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
