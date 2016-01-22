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

        if (waypointExists())
        {
            loadWaypoints();
        }
        else
        {
            addWaypoint(null);
        }
    }

    public boolean waypointExists()
    {
        return Waypoint.WAYPOINT_LIST_FILE.exists();
    }

    public void loadWaypoints()
    {
        //todo: make load config code
        currentPos=0;
    }

    public void saveWaypoints()
    {

    }

    private void displaySettings()
    {
        current = waypoints.get(currentPos);
        //todo: complete display settings, if values (null and -1), leave text blank

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
        waypoints.add(new Waypoint(null,-1,-1,false,-1));
        currentPos=waypoints.size()-1;
        displaySettings();
    }

}
