package com.leoindustries.emergencywarningsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                // Reschedule your job here if needed
                // You might want to use JobIntentService.enqueueWork or schedule a new job
                // depending on your use case.
            }
        }
    }
}