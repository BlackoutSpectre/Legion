package com.qualcomm.ftcrobotcontroller.legion;

import java.io.Serializable;

/**
 * Created by Blackout Spectre on 12/18/2015.
 */
public class SpawnPoint extends Waypoint implements Serializable{

    /**
     * Heading in degrees, not changed to radians yet
     */
    private int headingInDegrees;

    /**
     * Use this to tell the robot to move to an open space
     * after the match starts
     */
    private String startingWaypoint;


    public SpawnPoint(String name, String startingWaypoint, int x, int y, int headingInDegrees)
    {
        super(name, x, y, true, 0);
        this.startingWaypoint =startingWaypoint;
        this.headingInDegrees=headingInDegrees;
    }

    /**
     * Call this method as soon as the robot initializes this program so that the procedure
     * can call back to Navigation and AICore.
     * @param parent AICore
     * @see AICore
     * @see Navigation
     * @see CoreProcedure
     * @deprecated
     */
    public void setAICore(AICore parent)
    {
        //startingWaypoint.parent=parent;
    }

    public void setHeadingInDegrees(int degrees)
    {
        headingInDegrees=degrees;
    }

    public double getRadians()
    {
        return Math.toRadians((double)getHeadingInDegrees());
    }
    public int getHeadingInDegrees()
    {
        return headingInDegrees;
    }

}
