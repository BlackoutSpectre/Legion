package com.qualcomm.ftcrobotcontroller.legion;
import java.util.Timer;

/**
 * Created by Seth Chick on 6/1/2015.
 * @deprecated
 */
public class Wall
{
    private long timeSeen; //-1 if the wall is static, useful for walls on the field that don't move... EVER!!!
    static final long EXPIRATION = 5000; //detected obstructions will be removed after this many milliseconds.
    Timer timer = new Timer();
    public Wall(long timeSeen, boolean isStatic)
    {
        if (isStatic)
            timeSeen = -1;
        else
            this.timeSeen=timeSeen;
    }


    /*updates the time in which another obstruction at this point was seen
    as long it is not a static wall.
     */
    public void newTime(long timeSeen)
    {
        if (!isStatic())
            this.timeSeen=timeSeen;
    }

    /*tells you if the wall will always be there or not (to be static or not to be static).
     */
    public boolean isStatic()
    {
        return timeSeen==-1;
    }

    /*returns if the is an obstruction present at this point.*/
    public boolean obstructionPresent()
    {
        if (isStatic())
            return true;
        else if (timeSeen+EXPIRATION<=System.currentTimeMillis())
            return true;
        return false;
    }
}
