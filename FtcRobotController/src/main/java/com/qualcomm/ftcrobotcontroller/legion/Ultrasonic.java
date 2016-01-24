package com.qualcomm.ftcrobotcontroller.legion;

/**
 * Created by Seth Chick on 1/24/2016.
 *
 * The class is used to detect objects by using distance sensors (like the ultrasonic sensor)
 * to detect objects that are in the way of the robot. Update this class as quickly as possible.
 * This not a file config, set this up when constructing the Navigation class.
 * @see com.qualcomm.ftcrobotcontroller.legion.Navigation
 */
public class Ultrasonic
{
    public static final int INCHES = 0;
    public static final int CENTIMETERS = 1;
    public static final double INCHES_TO_CENTIMETERS = 2.54;

    private int degreeOffset;
    private int distanceFromCenter;
    private int maxRange;

    /**
     * 0=inches, 1=centimeters
     */
    private int unitsUsed;

    /**
     * The distance that the ultrasonic or distance sensor has picked up. If sensor hasn't
     * detected anything or the distance is out of range, this variable will be set to -1.
     */
    private int detectedDistance = -1;

    /**
     *
     * @param degreeOffset the heading of the sensor based off from the front of the robot and the
     *                     center of rotation.
     * @param distanceFromCenter distance in cm from the center of rotation (cm).
     * @param maxRange the furthest you want the AI to consider of the object that's in the way (cm).
     * @param units enter whether the sensor is returning in inches or centimeters.
     */
    public Ultrasonic(int degreeOffset, int distanceFromCenter, int maxRange, int units)
    {
        this.degreeOffset=degreeOffset;
        this.distanceFromCenter=distanceFromCenter;
        this.maxRange=maxRange;
        unitsUsed=units;
    }

    /**
     *
     * @param distance The distance that the ultrasonic or distance sensor has picked up.
     *                 If sensor hasn't
     * detected anything or the distance is out of range, this variable will be set to -1.
     */
    public void setDetectedDistance(int distance)
    {
        if (distance<maxRange)
            detectedDistance=distance;
        else
            detectedDistance=-1;
    }

    /**
     *
     * @return detected distance in cm. -1 if nothing's detected.
     */
    public int getDetectedDistance()
    {
        if (unitsUsed==Ultrasonic.INCHES)
        {
            return (int)Math.round((double)detectedDistance*Ultrasonic.INCHES_TO_CENTIMETERS);
        }
        else
            return detectedDistance;
    }
}
