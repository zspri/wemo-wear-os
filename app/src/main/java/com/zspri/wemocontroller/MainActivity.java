package com.zspri.wemocontroller;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.wear.widget.WearableRecyclerView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WearableActivity {
    private SwipeRefreshLayout swipeContainer;
    public HttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpClient = new HttpClient(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                httpClient.getDevices(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        addRecyclerItems(response);
                        swipeContainer.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void addRecyclerItems(JSONObject response) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final WearableRecyclerView recyclerView = (WearableRecyclerView) findViewById(R.id.itemList);

        List<WemoDeviceItem> items = new ArrayList<>();
        try {
            JSONArray devices = response.getJSONArray("devices");
            for (int i = 0; i < devices.length(); i++) {
                JSONObject device = devices.getJSONObject(i);
                System.out.println(device.toString());
                WemoDeviceItem item = new WemoDeviceItem(
                        device.getString("name"),
                        device.getString("device"), // device ip address
                        device.getString("hash"), // unique device identifier
                        device.getInt("state"));
                items.add(item);
            }

            RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, httpClient);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        // called on activity create AND resume

        super.onResume();

        httpClient.getDevices(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addRecyclerItems(response);
            }
        });
    }
}
