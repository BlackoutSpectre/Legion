package com.qualcomm.ftcrobotcontroller.legion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Map;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.PathingNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Blackout Spectre on 6/2/2015.
 */




//todo: implement sensor functions, modify a* pathfinding to work with program, implement spawn point config file
//todo: implement waypoint files, implement picture maps, implement robot properties file

/**
 * This handles all movement and navigation of the robot, will throw fatal exceptions during
 * autonomous if something goes wrong.
 * @version 0.00.00.01 dev
 * @author Blackout Spectre
 */
public class Navigation implements SensorEventListener
{
    boolean CollisionDetection = true;

    boolean usePathing = true;
    /**
     * Used when the field could be in any position at the start of the match.
     * The best possible version will be set as array "actualField". This will be dereferenced
     * when field is determined to save memory.
     */
    public ArrayList<Map<PathingNode>> possibleFields;

    public Map<PathingNode> actualField;
    private List<PathingNode> path;
    /**
     * Used to store temporary data to compare actual possible fields to the what the robot sees
     * to determine the field configuration the robot is on. Will be deleted when determined.
     */
    public Boolean[][] comparableField;
    /*used to store temperary data to compare to the actual possible fields. Will be dereferenced
    * to save memory when field is determined.*/


    private double YPos = 0;
    private double XPos = 0;
    /*This is the velocities of the robot to the side and forward of the robot, measured by the
    accelerometers.*/
    private double[] robotVelocity = new double[2];
    /*used to get the x,y velocity relative to the robot*/
    private double[] fieldRobotRelativeVelocity = new double[2];
    /*used to get the x,y velocities of the robot relative to the field, so the position does not
    get off due to the robot climbing up or down a ramp. This is relative to the above array and
    tilt*/
    //protected static double[] fieldRelativeRobotVelocity = new double[2];
    private SensorCalc acceleration = new LinearAccel(); //use to get accel to calc speed
    private double heading; //in radians
    private Sensor gyro;
    private SensorManager gyroSensorManager;

    //private SensorCalc gyro = new Gyro(); //use gyroscopic sensor to get heading
    private SensorCalc tilt = new Tilt(); //use orientation sensor to get tilt and not heading
    static final long SPEED_INTERVAL = 1; //in milliseconds
    Context context;
    boolean started;

    Timer navTimer;
    TimerTask timerRunner;

    /**
     * this is so the Navigation class can callback to the AICore class
     * @see AICore
     * @see Navigation
     *
     */
    public AICore brother;


    /**
     * list of all waypoints
     * precondition: must be sorted by ID
     */
    private ArrayList<Waypoint> listOfWaypoints;


    /**
     * current waypoint in use
     */
    private Waypoint currentWayPoint;

    /**
     * Use this method to initialize and load all files and configs into memory
     * and readies the program for start.
     */
    public Navigation()
    {
        navTimer = new Timer();
        timerRunner = new TimerTask() {
            @Override
            public void run() {

            }
        };
        int interval = (int)SPEED_INTERVAL;
        gyroSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyro = gyroSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroSensorManager.registerListener(this, gyro,interval);
        started=false;
    }

    /**
     * when the match starts, activate this method to begin all tracking
     */
    public void start()
    {
        navTimer.schedule(timerRunner, SPEED_INTERVAL);
        started=true;
    }

    /**
     * stops all handlers that have been initialized and started including sensors
     */
    public void shutdown()
    {
        if (started)
            navTimer.cancel();
        gyroSensorManager.unregisterListener(this, gyro);
    }
    /**
     * returns the grid position the robot is on.
     * @return grid position on x axis.
     */
    public int getXPos()
    {
        return (int) Math.round(XPos);
    }



    /**
     *
     * @return grid position on y axis
     */
    public int getYPos()
    {
        return (int) Math.round(YPos);
    }

    /**
     *
     * @return actual position on x axis
     */
    public double getActualXPos()
    {
        return XPos;
    }

    /**
     *
     * @return actual position on y axis
     */
    public double getActualYPos()
    {
        return YPos;
    }

    /**
     *
     * @return the electronic X position of Legion on the map
     */
    public int getMatrixXPos()
    {
        return actualField.getMapSize()[0]-1-getXPos();
    }

    /**
     * @return the electronic Y position of Legion on the map
     */
    public int getMatrixYPos()
    {
        return actualField.getMapSize()[1]-1-getYPos();
    }


    /**
     * Uses the encoders to calculate the forward velocity of the robot.
     * Takes the average of the rotation of the left and right wheels to deprive the velocity.
     * Uses configuration file for wheel encoder calibration.
     * WARNING!
     * cannot account for slippage or rightward velocity
     * @param leftWheelDifference the degree difference of the left wheel
     * @param rightWheelDifference the degree difference of the right wheel
     * @param timeInterval the time in milliseconds between samples
     */
    public void updateVelocityByEncoders(int leftWheelDifference, int rightWheelDifference,
                long timeInterval)
    {

    }
    /**
     * uses accelerometer to determine robot's speed.
     * @deprecated
     */
    public void updateVelocityByAccelerometer()
    {
        double[] a = acceleration.readSensor();
        robotVelocity[0] = robotVelocity[0] + a[0]*((double)SPEED_INTERVAL/1000);
        robotVelocity[1] = robotVelocity[1] + a[1]*((double)SPEED_INTERVAL/1000);
    }

    /**
     * updates the robot's velocity relative to the field.
     */
    public void updateGridVelocity()
    {
        fieldRobotRelativeVelocity[0]=robotVelocity[0]*Math.cos(heading);
        fieldRobotRelativeVelocity[1]=robotVelocity[1]*Math.sin(heading);
    }

    /**
     * updates the position based on the robot's velocity on the field.
     */
    public void updateGridPos()
    {
        XPos+=fieldRobotRelativeVelocity[0]*((double)SPEED_INTERVAL/1000);
        YPos+=fieldRobotRelativeVelocity[1]*((double)SPEED_INTERVAL/1000);
    }

    /**
     * assumes the Waypoint list is sorted
     * @param id the waypoint name to be searched for.
     * @see Waypoint
     * @throws IllegalArgumentException waypoint not found
     */
    private void pathToWaypoint(String id)
    {
        int[] size = actualField.getMapSize();
        int pos = Collections.binarySearch(listOfWaypoints, id);
        if (pos<0)
            throw new IllegalArgumentException();
        else {
            currentWayPoint = listOfWaypoints.get(pos);
            int currentXPos = size[0]-1-getXPos();//get the x pos of the matrix
            int currentYPos = size[1]-1-getYPos();//get the y pos of the matrix
            int waypointX = size[0]-1-currentWayPoint.getXPos();
            int waypointY = size[1]-1-currentWayPoint.getYPos();
            path = actualField.findPath(currentXPos, currentYPos, waypointX, waypointY);
        }

    }

    /**
     * move to waypoint
     * @param id name of waypoint
     * @param makePath should Legion directly move there or take a safe path?
     * @param doCollisionDetection should Legion not smash into a wall if it gets in the way?
     */
    public void navigateTo(String id, boolean makePath, boolean doCollisionDetection)
    {
        if (makePath)
            pathToWaypoint(id);
        CollisionDetection=doCollisionDetection;
        usePathing=makePath;
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GYROSCOPE:
                    updateHeading(event.values[0],(int)SPEED_INTERVAL);
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * Updates heading based on the turn rate given by sensor
     * @param turnRate the x-axis sensor value
     * @param timeInterval the time it takes between updates in milliseconds
     */
    private void updateHeading(float turnRate, int timeInterval)
    {
        heading += turnRate * (float) timeInterval/1000;
    }

    /**
     * Called when the accuracy of a sensor has changed.
     * <p>See {@link SensorManager SensorManager}
     * for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * All of the extra, non-core, methods are entered here
     * @author no one ever
     */
    public class CustomMethods
    {

    }
}