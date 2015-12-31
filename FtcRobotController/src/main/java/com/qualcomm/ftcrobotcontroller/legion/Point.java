package com.qualcomm.ftcrobotcontroller.legion;

/**
 * Created by Seth Chick on 6/1/2015.
 */

/**
 * This is the class that stores the type of point at a point on the map. The position of the point
 //is stored as the position of the object on the matrix array.
 @deprecated
 */
public class Point
{
    private Wall theWall;

    /*This is used you don't want the robot to consider if there is an obstruction at this point or not.
     True: don't consider. False: do consider. This is useful if you don't want the robot to think there
     is a wall when it actually detected the ramp.
     */
    private Boolean ramp;
//    WayPoint wayPoint;

    public Point()
    {
        this(false, false, false);//(wallPresent, isTemperaryObstruction, rampPresent)
    }
    public Point(boolean wallPresent, boolean temperaryObstruction, boolean rampPresent)
    {
        if (wallPresent)
        {
            theWall = new Wall((long)System.currentTimeMillis(),temperaryObstruction);
        }
        else
            theWall = new Wall((long)0, false);
        ramp = rampPresent;
    }

    /*Postcondition: returns if there is an obstruction there
     */
    public boolean isWall()
    {
        return theWall.obstructionPresent();
    }
    /*returns if there is a "ramp" here or not. Used if you don't want the robot to get confused
      due to an incline in sight.
      Postcondition: returns true if a ramp was present.
     */
    public boolean isRamp()
    {
        return ramp;
    }

    /*Precondition: if an obstruction was detected.
      When the ultrasonic sensor picks up an object, it will update this point for the new obstruction.
      Postcondition: only updates if the point is not a ramp or is not a static wall.
     */
    public void seenNewObstruction()
    {
        if (!isRamp()&&!theWall.isStatic()) {
            theWall.newTime(System.currentTimeMillis());
            //update pathing
        }
    }






}
