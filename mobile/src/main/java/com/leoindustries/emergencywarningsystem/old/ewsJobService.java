package com.leoindustries.emergencywarningsystem;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ewsJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("ewsLog: ewsJobService", "Job started");

        // Your logic to perform background task
        String url = "https://www.data.go.kr/catalog/3058822/openapi.json";
        String result = makeGetRequest(url);

        Log.d("ewsLog: ewsJobService", "Result: " + result);

        // Let the system know that the job has finished
        jobFinished(params, false);

        // Return true if there's more work to be done, false otherwise
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("ewsLog: ewsJobService", "Job stopped");

        // Return true to reschedule the job if necessary
        return false;
    }

    private String makeGetRequest(String url) {
        // Implement your GET request logic here
        // Return the result or handle it as needed
        return "RESULT FROM API SIMULATED";
    }
}

