package com.leoindustries.emergencywarningsystem;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class ewsJobScheduler {
    public static void scheduleJob(Context context) {
        Log.d("ewsLog: ewsJobScheduler", "Job scheduler called");
        ComponentName serviceComponent = new ComponentName(context, ewsJobService.class);

        JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
//        builder.setPeriodic(30 * 1000); // 30 seconds interval
        builder.setPeriodic(15 * 60 * 1000); // 15 minutes interval

        builder.setPersisted(true);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Log.d("ewsLog: ewsJobScheduler", "Job scheduler finished");
    }
}
