package com.qualcomm.ftcrobotcontroller.legion.pathfinding;

import com.qualcomm.ftcrobotcontroller.legion.Navigation;
import com.qualcomm.ftcrobotcontroller.legion.Wall;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Michael Chick on 7/15/2015.
 */
public class PathingNode extends AbstractNode {

    private int xPos;
    private int yPos;
    public Navigation nav;

    public PathingNode(int xPosition, int yPosition) {
        super(xPosition, yPosition);
        // do other init stuff

        xPos=xPosition;
        yPos=yPosition;
        //this(false, false, false);//(wallPresent, isTemperaryObstruction, rampPresent)
    }

    public void sethCosts(AbstractNode endNode) {
        this.sethCosts((absolute(this.getxPosition() - endNode.getxPosition())
                + absolute(this.getyPosition() - endNode.getyPosition()))
                * BASICMOVEMENTCOST);
    }

    private int absolute(int a) {
        return a > 0 ? a : -a;
    }

    public int getxPos()
    {
        return xPos;
    }
    public int getyPos()
    {
        return yPos;
    }

    public int[] getCoordinates()
    {
        int[] point = {getxPos(),getyPos()};
        return point;
    }
    //private Wall theWall;

    /**
     * This is used you don't want the robot to consider if there is an obstruction at this point or not.
     True: don't consider. False: do consider. This is useful if you don't want the robot to think there
     is a wall when it actually detected the ramp.
     */
    private Boolean ramp;
//    WayPoint wayPoint;

    /*public Point()
    {
        this(false, false, false);//(wallPresent, isTemperaryObstruction, rampPresent)
    }*/
    /*public PathingNode(boolean wallPresent, boolean temperaryObstruction, boolean rampPresent, )
    {
        super(xPosition, yPosition);
        if (wallPresent)
        {
            theWall = new Wall((long)System.currentTimeMillis(),temperaryObstruction);
        }
        else
            theWall = new Wall((long)0, false);
        ramp = rampPresent;
    }*/

    /**
     * Postcondition: returns if there is an obstruction there
     */
    public boolean isWall()
    {
        return obstructionPresent();
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
    /*public void seenNewObstruction()
    {
        if (!isRamp()&&!theWall.isStatic()) {
            theWall.newTime(System.currentTimeMillis());
            //update pathing
        }
    }*/


    /**
     * Sets up a new obstruction and set the map as unwalkable, for a duration of EXPIRATION milliseconds.
     *
     */
    private void seenNewObstruction()
    {
        {
            setWall(true);
            timer.schedule(countdown, EXPIRATION);
           //update pathing
        }
    }


    //private long timeSeen; //-1 if the wall is static, useful for walls on the field that don't move... EVER!!!
    /**
     * detected obstructions will be removed after this many milliseconds.
     */
    static final long EXPIRATION = 5000; //detected obstructions will be removed after this many milliseconds.
    private Timer timer = new Timer();
    private TimerTask countdown = new TempWall();
    class TempWall extends TimerTask
    {

        @Override
        public void run() {
            timer.cancel();
            timer = new Timer();
            setWall(false);

        }
    }

    private boolean isStatic;
    /*public Wall(long timeSeen, boolean isStatic)
    {
        if (isStatic)
            timeSeen = -1;
        else
            this.timeSeen=timeSeen;
    }*/


    /**updates the time in which another obstruction at this point was seen
     as long it is not a static wall.
     */
    /*public void newTime(long timeSeen)
    {
        if (!isStatic())
            this.timeSeen=timeSeen;
    }*/

    public void newTime()
    {
        if (!isStatic())
            seenNewObstruction();
    }

    /*tells you if the wall will always be there or not (to be static or not to be static).
     */
    public boolean isStatic()
    {
        return isStatic;
    }

    /*returns if the is an obstruction present at this point.*/
    private boolean obstructionPresent()
    {
        return !isWalkable();
    }
    /*public boolean obstructionPresent()
    {
        if (isStatic())
            return true;
        else if (timeSeen+EXPIRATION<=System.currentTimeMillis())
            return true;
        return false;
    }*/

    private void setWall(boolean present)
    {
        setWalkable(!present);

    }

}
