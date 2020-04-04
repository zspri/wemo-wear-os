package com.zspri.wemocontroller;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONObject;

public class DeviceDetailsActivity extends WearableActivity {

    private Bundle extras;
    private HttpClient httpClient;
    private WemoDeviceItem device;

    private TextView deviceAddrTextView;
    private TextView deviceHashTextView;
    private TextView deviceNameTextView;
    private TextView deviceStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        extras = getIntent().getExtras();
        httpClient = new HttpClient(this);

        device = new WemoDeviceItem(
                extras.getString("device_name"),
                extras.getString("device_addr"),
                extras.getString("device_hash"),
                extras.getInt("device_state"));

        deviceAddrTextView = (TextView) findViewById(R.id.device_addr);
        deviceHashTextView = (TextView) findViewById(R.id.device_hash);
        deviceNameTextView = (TextView) findViewById(R.id.device_name);
        deviceStateTextView = (TextView) findViewById(R.id.device_state);

        deviceAddrTextView.setText("Address: " + extras.getString("device_addr"));
        deviceHashTextView.setText("Hash: " + extras.getString("device_hash"));
        deviceNameTextView.setText(extras.getString("device_name"));
        deviceStateTextView.setText("State: " + extras.getInt("device_state"));
    }

    public void turnDeviceOn(View view) {
        httpClient.setDeviceState(device, BinaryState.ON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                device.setState(1);
                deviceStateTextView.setText("State: 1");
            }
        });
    }

    public void turnDeviceOff(View view) {
        httpClient.setDeviceState(device, BinaryState.OFF, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                device.setState(0);
                deviceStateTextView.setText("State: 0");
            }
        });
    }

}
