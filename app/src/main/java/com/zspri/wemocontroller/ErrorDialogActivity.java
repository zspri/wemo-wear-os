package com.zspri.wemocontroller;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class ErrorDialogActivity extends WearableActivity {

    private TextView mErrorBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_dialog);

        Bundle extras = getIntent().getExtras();

        mErrorBody = (TextView) findViewById(R.id.error_body);
        mErrorBody.setText(extras.getString("error_desc"));
    }
}
