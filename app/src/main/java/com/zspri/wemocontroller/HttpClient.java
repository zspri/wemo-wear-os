package com.zspri.wemocontroller;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

enum BinaryState {
    ON,
    OFF
}

public class HttpClient {
    private RequestQueue queue;
    private Response.ErrorListener errorListener;
    private String baseUrl = "https://your-wemo-server-here";

    public HttpClient(final Context ctx) {
        queue = Volley.newRequestQueue(ctx);

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Intent i = new Intent(ctx, ErrorDialogActivity.class);
                i.putExtra("error_desc", error.getMessage());
                ctx.startActivity(i);
            }
        };
    }

    public void getDevices(Response.Listener<JSONObject> listener) {
        JsonRequest request = new JsonObjectRequest(
                Request.Method.GET,
                baseUrl + "/devices",
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "your-password-here");

                return params;
            }
        };
        queue.add(request);
    }

    public void setDeviceState(WemoDeviceItem device, BinaryState state, Response.Listener<JSONObject> listener) {
        String binState = state == BinaryState.ON ? "1" : "0";

        JsonRequest request = new JsonObjectRequest(
                Request.Method.PATCH,
                baseUrl + "/devices/" + device.getHash() + "?state=" + binState,
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "your-password-here");

                return params;
            }
        };
        queue.add(request);
    }
}
