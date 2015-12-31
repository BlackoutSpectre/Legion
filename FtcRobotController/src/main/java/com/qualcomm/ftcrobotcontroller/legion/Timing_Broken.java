package com.qualcomm.ftcrobotcontroller.legion;
/**
 * Created by Seth Chick on 6/15/2015.
 */

import java.util.Timer;
import java.util.TimerTask;

/**
 * @deprecated
 */
public class Timing_Broken
{
    static Timer navigationTimer;

    public Timing_Broken()
    {
        navigationTimer = new Timer();
    }
    public void startNavTimer(long milliseconds)
    {
        navigationTimer.schedule(new UpdateNav(), milliseconds);
    }
    class UpdateNav extends TimerTask
    {
        public void run()
        {
            //run navigation code
//            Navigation.updateVelocity();
        }
    }
    public void updateNav()
    {
        Crap.BS();

//        Navigation.updateVelocity();
    }
}
