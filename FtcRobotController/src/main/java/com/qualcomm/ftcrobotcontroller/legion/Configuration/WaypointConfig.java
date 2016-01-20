package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.os.Bundle;
import android.app.Activity;

import com.qualcomm.ftcrobotcontroller.R;

public class WaypointConfig extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_config);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
