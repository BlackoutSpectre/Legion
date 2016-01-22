package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.ftcrobotcontroller.legion.Waypoint;

import java.util.ArrayList;

public class WaypointConfig extends Activity {

    private EditText xPosBox;
    private EditText yPosBox;
    private EditText radiusBox;
    private EditText titleBox;
    private ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
    private Waypoint current = null;
    private int currentPos = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_config);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadWaypoints()
    {

    }

    public void saveWaypoints()
    {

    }

    private void displaySettings()
    {

    }

    public void goToNext(View view)
    {

    }

    public void goToPrevious(View view)
    {

    }

    public void updateWaypoint(View view)
    {

    }

    public void saveAll(View view)
    {

    }

    public void addWaypoint(View view)
    {

    }

}
