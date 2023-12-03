package com.leoindustries.emergencywarningsystem;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

interface ewsInterfaceAPI {
    @GET("openapi.json")
    Call<ewsAlertDataModel> fetchData();
}

class NetworkManager {
    private final ewsInterfaceAPI ewsInterfaceAPI;

    public NetworkManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.data.go.kr/catalog/3058822/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ewsInterfaceAPI = retrofit.create(ewsInterfaceAPI.class);
    }

    public void fetchData(Callback<ewsAlertDataModel> callback) {
        Call<ewsAlertDataModel> call = ewsInterfaceAPI.fetchData();
        call.enqueue(callback);
    }
}

public class ewsBackgroundService extends Service {
    private Context context;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private ewsAlertsHandler alertsHandler;
    private final NetworkManager networkManager = new NetworkManager();
    private final String channelId = "default_channel_id";

    private final Runnable fetchApiRunnable = new Runnable() {
        @Override
        public void run() {
            fetchDataFromApi();
            handler.postDelayed(this, 20 * 1000); // Fetch every 30 seconds
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        alertsHandler = new ewsAlertsHandler(this);
        createNotificationChannel();
        // Start fetching API data
        handler.post(fetchApiRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the periodic fetching when the service is destroyed
        handler.removeCallbacks(fetchApiRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void fetchDataFromApi() {
        Log.d("ewsLog: ewsBackgroundService", "API called to fetch data");
        networkManager.fetchData(new Callback<ewsAlertDataModel>() {
            @Override
            public void onResponse(Call<ewsAlertDataModel> call, Response<ewsAlertDataModel> response) {
                if (response.isSuccessful()) {
                    Log.d("ewsLog: ewsBackgroundService", "API successfully fetched data");
                    ewsAlertDataModel data = response.body();
                    // Show notification with the fetched data
                    alertsHandler.handleResponse(data);
                    showNotification(data);
                }
            }

            @Override
            public void onFailure(Call<ewsAlertDataModel> call, Throwable t) {
                Log.e("ewsLog: ewsBackgroundService", "Error on API call: "+ t.getMessage());
            }
        });
    }

    private void showNotification(ewsAlertDataModel data) {
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(data.getName())
                .setContentText(data.getDescription())
                .setSmallIcon(R.drawable.notification_icon)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
        );

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}

