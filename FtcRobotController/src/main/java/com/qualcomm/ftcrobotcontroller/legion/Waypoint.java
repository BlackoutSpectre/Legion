package com.qualcomm.ftcrobotcontroller.legion;

import java.io.File;

/**
 * Created by Seth Chick on 6/1/2015.
 */

/*Used for when you want the robot to move toward this point for a task objective.
  This will be stored in an arrayList. These waypoints will be read out of waypoint
  configuration text files created externally in a certain directory in the Android's
  memory.
 */
public class Waypoint implements Comparable<String>
{
    /**
     * identifer for waypoint, cannot be changed, named by config waypoint file
     */
    private String name; //
    /**
     * cannot be set more than once, initialized by waypoint config file
     */
    private int xPos; //cannot be set more than once, initialized by waypoint config file
    /**
     * cannot be set more than once, initialized by waypoint config file
     */
    private int yPos; //cannot be set more than once, initialized by waypoint config file
    /**
     * Move directly to this point, not considering collisions.
     * @deprecated this will be handled by procedures instead
     */
    private boolean directNavigate; //Move directly to this point, not considering collisions.
    /**
     * The area in which the robot to consider that it reach its destination.
     */
    private int stoppingRadius; //The area in which the robot to consider that it reach its destination.

    /**
     * default directory of the waypoints file
     */
    public final static File WAYPOINT_LIST_FILE = new File(Helper.getBaseFolder(),"/waypoints.cfg");


    public Waypoint(String name, int xPos, int yPos, boolean direct, int radius)
    {
        setName(name);
        setxPos(xPos);
        setyPos(yPos);
        setDirectNavigate(direct);
        setStoppingRadius(radius);
    }

    /*Do not use setMethods except in constructor*/
    public void setName(String name)
    {
        this.name=name;
    }
    public void setxPos(int pos)
    {
        xPos=pos;
    }
    public void setyPos(int pos)
    {
        yPos=pos;
    }

    /**
     * @deprecated
     * @param direct
     */
    public void setDirectNavigate(boolean direct)
    {
        directNavigate=direct;
    }
    public void setStoppingRadius(int radius)
    {
        stoppingRadius=radius;
    }

    public int getXPos()
    {
        return xPos;
    }
    public int getYPos()
    {
        return yPos;
    }
    public String getName()
    {
        return name;
    }

    /**
     * @deprecated
     * @return
     */
    public boolean getDirect()
    {
        return directNavigate;
    }
    public int getStoppingRadius()
    {
        return stoppingRadius;
    }

    public String toString()
    {
        String output = "Waypoint " +getName() +" located at (" +getXPos() +"," +getYPos() +") will ";

        if (getDirect())
            output+="disregard ";
        else
            output+="consider ";

        output+="collisions and will stop within " +getStoppingRadius() +" of the waypoint";
        return output;

    }

    @Override
    public int compareTo(String another) {
        return another.compareTo(getName());
    }
}
