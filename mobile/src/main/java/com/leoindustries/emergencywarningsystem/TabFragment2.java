package com.leoindustries.emergencywarningsystem;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class TabFragment2 extends Fragment implements UpdatableFragment {
    RecyclerView alertRecyclerView;
    alertAdapter adapter;
    SharedPreferences preferences;
    String tabName = "Tab2";
    public TabFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab, container, false);

        preferences = requireActivity().getSharedPreferences("com.leoindustries.emergencywarningsystem." + tabName, Context.MODE_PRIVATE);

        alertRecyclerView = view.findViewById(R.id.alertRecyclerView);

        adapter = new alertAdapter(new JSONArray());
        alertRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        alertRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void updateTabData(String data){
        ewsAlertsHandler.updateUIWithData(data, adapter);
        Log.d("ewsLog: " + tabName, tabName + " Updated with data: " + data );
    }

    @Override
    public void onResume() {
        super.onResume();
        String savedData = preferences.getString("alertsData", "");
        if(!savedData.isEmpty()){
            updateTabData(savedData);
        }
        Log.d("ewsLog: "+ tabName, tabName + " resumed");
    }
}
