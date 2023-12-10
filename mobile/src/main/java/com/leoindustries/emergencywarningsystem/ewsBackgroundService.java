package com.leoindustries.emergencywarningsystem;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

interface ewsInterfaceAPI {
//    @GET("openapi.json")
//    Call<ewsAlertDataModel> fetchData();
    @GET("getDisasterMsg1List")
    Call<Object> fetchData(
        @Query("ServiceKey") String serviceKey,
        @Query("pageNo") int pageNo,
        @Query("numOfRows") int numOfRows,
        @Query("type") String type
    );
}

class NetworkManager {
    private final ewsInterfaceAPI ewsInterfaceAPI;

    public NetworkManager() {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
//        httpClient.addInterceptor(loggingInterceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Header1", "Value1")  // Add your headers here
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.data.go.kr/1741000/DisasterMsg3/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        ewsInterfaceAPI = retrofit.create(ewsInterfaceAPI.class);
    }

    public void fetchData(Callback<Object> callback) {
        Call<Object> call = ewsInterfaceAPI.fetchData(
            "rIG73AhrzXbBx/dL61YI31djJZxsurkpljXhi10wlNrC4orEy9BOgk6Umqr89Iv581r1tNbZl3sktP+kUerwDA==",
            1,
            10,
            "json");
        call.enqueue(callback);
    }
}

public class ewsBackgroundService extends Service {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final NetworkManager networkManager = new NetworkManager();
    private final String channelId = "com.leoindustries.emergencywarningsystem.notification_channel_id";

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
        createNotificationChannel();
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
        networkManager.fetchData(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    Object dataMap = response.body();
                    JSONObject dataJSON = new JSONObject((Map) dataMap);
                    Log.d("ewsLog: ewsBackgroundService", "API successfully fetched data");
                    handleResponse(dataJSON);
                    handleNotifications(dataJSON);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("ewsLog: ewsBackgroundService", "Error on API call: "+ t.getMessage());
            }
        });
    }

    public void handleResponse(JSONObject data){
        // Create an intent with the action and data
        Intent intent = new Intent(ewsAlertsHandler.ACTION_UPDATE_UI);
        intent.putExtra(ewsAlertsHandler.EXTRA_DATA_MODEL, data.toString());

        // Send the broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleNotifications(JSONObject data){
        SharedPreferences preferences = getSharedPreferences("com.leoindustries.emergencywarningsystem.Preferences", Context.MODE_PRIVATE);
        boolean allowNotifications = preferences.getBoolean("allowNotifications", false);
        if(allowNotifications){
            showNotification(data);
        }
    }

    private void showNotification(@NonNull JSONObject data) {
        try {
            JSONArray rowArray = data.getJSONArray("DisasterMsg")
                    .getJSONObject(1)
                    .getJSONArray("row");
            JSONObject firstElement = rowArray.getJSONObject(0);


            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(firstElement.getString("location_name"))
                    .setContentText(firstElement.getString("msg"))
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


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

