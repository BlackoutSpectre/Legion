package com.qualcomm.ftcrobotcontroller.legion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Grid;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Map;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.PathingNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Blackout Spectre on 6/2/2015.
 */




//todo: implement spawn point config file
//todo: implement waypoint files, implement picture maps,
//todo: change FileNotFoundException to RuntimeException with details of what is wrong
//todo: emprovise collision detection from tile scaling

/**
 * This handles all movement and navigation of the robot, will throw fatal exceptions during
 * autonomous if something goes wrong.
 *
 * Psuodocode (converting from matrix to position and back: When setting path, convert to matrix.
 * Accessing path position for movement, convert to actual.
 * @version 0.00.00.03 dev
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
    protected static final int PATH_NODE_RADIUS = 15;
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
    /**
     * current heading of robot in radians
     */
    private double heading; //in radians
    /**
     * the target heading of the robot towards it's current path or waypoint in radians.
     * @see Waypoint
     */
    private double targetHeading;

    private Sensor gyro;
    private SensorManager gyroSensorManager;

    //private SensorCalc gyro = new Gyro(); //use gyroscopic sensor to get heading
    private SensorCalc tilt = new Tilt(); //use orientation sensor to get tilt and not heading
    static final long SPEED_INTERVAL = 1; //in milliseconds
    Context context;
    boolean started;

    private Grid gridInfo;

    Timer navTimer;
    TimerTask timerRunner;

    double leftWheelRadius;
    double rightWheelRadius;

    public static File WHEEL_CONFIG_FILE_DIR = new File(Helper.getBaseFolder(),"/wheel radius.txt");

    //public static final File COMPILED_MAP = new File(Helper.getBaseFolder(),"/compiledMap.map");
    //public static final File GRID_INFO = new File(Helper.getBaseFolder(),"/gridInfo.bin");

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

    private Ultrasonic[] distanceSensors;


    /**
     * current waypoint in use
     */
    private Waypoint currentWayPoint;

    public static File getCompiledMap()
    {
        return new File(Helper.getBaseFolder(),"/compiledMap.map");
    }
    public static File getGridInfo()
    {
        return new File(Helper.getBaseFolder(),"/gridInfo.bin");
    }

    /**
     * Use this method to initialize and load all files and configs into memory
     * and readies the program for start.
     * @param distanceSensors sensors used to detect obstructions
     * @param robotDimensions longest distance from the edge of the robot to the center of rotation.
     */
    public Navigation(int robotDimensions, Ultrasonic[] distanceSensors) throws IOException, ClassNotFoundException {
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
        this.distanceSensors=distanceSensors;
        started=false;
        try {
            loadWheelRadius();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Wheel Config Not Found");

        }
        loadCompiledMap();
        loadWaypoints();

        //makes the relative to the tile map
        int[] spacing = gridInfo.getMapCoordinateFromGrid(robotDimensions,robotDimensions);
        actualField.setSpacing(spacing[0]);


    }

    private void loadWheelRadius() throws FileNotFoundException {
        Scanner scanner = new Scanner(Navigation.WHEEL_CONFIG_FILE_DIR);
        leftWheelRadius=scanner.nextDouble();
        rightWheelRadius = scanner.nextDouble();
    }

    private void loadCompiledMap() throws IOException, ClassNotFoundException {
        //loads compiled map
        FileInputStream fileInputStream = new FileInputStream(Navigation.getCompiledMap());
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        actualField = (Map<PathingNode>) objectInputStream.readObject();
        objectInputStream.close();

        //loads grid info
        fileInputStream = new FileInputStream(getGridInfo());
        objectInputStream = new ObjectInputStream(fileInputStream);
        gridInfo = (Grid)objectInputStream.readObject();
        objectInputStream.close();
    }

    private void loadWaypoints() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(Waypoint.WAYPOINT_LIST_FILE);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        listOfWaypoints = (ArrayList<Waypoint>) objectInputStream.readObject();
        objectInputStream.close();
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
     * @deprecated is redundant, do not flip over y axis
     */
    public int getMatrixXPos()
    {
        return actualField.getMapSize()[0]-1-gridInfo.getMapCoordinateFromGrid(getXPos(),0)[0];
    }

    /**
     * converts from actual (grid) to scaled (map)
     * @return the electronic Y position of Legion on the map
     */
    public int getMatrixYPos()
    {
        return actualField.getMapSize()[1]- 1 -gridInfo.getMapCoordinateFromGrid(0,getYPos())[1];
    }

    /**
     * get the inverse of the coordinate
     * @param pos position (not scaled) (grid)
     * @param isMirrored is it already inverted?
     * @return the inverted Y Position
     */
    public int convertYtoInverse(int pos,boolean isMirrored)
    {
        return gridInfo.getGridSizeY()-1-pos;
    }

    /**
     * is already scaled down
     * @param pos scaled down position (map)
     * @param isMirrored is it already inverted
     * @return the scaled (map) Y position
     */
    public int convertYMapToInverse(int pos, boolean isMirrored)
    {
        return gridInfo.getMapSizeY()-1-pos;
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
    public void updateLocationByEncoders(int leftWheelDifference, int rightWheelDifference,
                                         long timeInterval)
    {
        double arcLeft = ((double) leftWheelDifference/360)*(2*Math.PI*leftWheelRadius);
        double arcRight = ((double) rightWheelDifference/360)*(2*Math.PI*rightWheelRadius);

        //gets actual distance from the mean of the two wheels
        double distance = arcLeft+arcRight/2;

        //updates the position based on the distance traveled
        XPos = getActualXPos()+distance*Math.sin(heading);
        YPos = getActualYPos()+distance*Math.cos(heading);
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
     * @throws RuntimeException waypoint not found
     */
    private List<PathingNode> pathToWaypoint(String id)
    {
        int[] size = actualField.getMapSize();
        int pos = Collections.binarySearch(listOfWaypoints, id);
        if (pos<0)
            throw new RuntimeException("Specified Waypoint was not found in list");
        else {
            currentWayPoint = listOfWaypoints.get(pos);
            //int currentXPos = size[0]-1-getXPos();
            int currentXPos = gridInfo.getMapCoordinateFromGrid(getXPos(),getYPos())[0];//get the x pos of the matrix
            //int currentYPos = size[1]-1-getYPos();
            int currentYPos = getMatrixYPos();//get the y pos of the matrix
            int waypointX = gridInfo.getMapCoordinateFromGrid(currentWayPoint.getXPos(),0)[0];
            int waypointY = size[1]-1-gridInfo.getMapCoordinateFromGrid(0, currentWayPoint.getYPos())[1];
            return actualField.findPath(currentXPos, currentYPos, waypointX, waypointY);
        }

    }

    private boolean isPathFound(List<PathingNode> path)
    {
        return !(path==null||path.size()<1);
    }

    /**
     * move to waypoint
     * @param id name of waypoint
     * @param makePath should Legion directly move there or take a safe path?
     * @param doCollisionDetection should Legion not smash into a wall if it gets in the way?
     * @return false = cannot reach target, true = direct navigation will occur or path is clear
     */
    public boolean navigateTo(String id, boolean makePath, boolean doCollisionDetection)
    {
        boolean navigationAlright; //can the robot make it to the waypoint?
        /**
         * If Legion should find a path, Legion will check to see if the robot can make it.
         * If Legion should drive straight to the target, path checking will be ignored and Legion
         * will decide that the robot will make it.
         */
        if (makePath) {

            List<PathingNode> tempPath=pathToWaypoint(id);
            if (isPathFound(tempPath))
            {
                navigationAlright=true;
                path=tempPath;
            }
            else
            {
                navigationAlright=false;
            }

        }
        else
            navigationAlright=true;
        CollisionDetection=doCollisionDetection;
        usePathing=makePath;
        return navigationAlright;
    }


    /**
     * procedures use this to see if the Legion has reached the waypoint
     * @return true = reached, false = not yet reached
     */
    public boolean reachedCurrentWaypoint()
    {
        int goalX = currentWayPoint.getXPos();
        int goalY = currentWayPoint.getYPos();
        int spacing = currentWayPoint.getStoppingRadius();
        return (goalX-spacing>getXPos()&&goalX+spacing<getXPos())&&
                (goalY-spacing>getYPos()&&goalY+spacing<getYPos());
    }

    /**
     *
     * @return true = reached path node, false = hasn't
     */
    public boolean reachedPathNode()
    {

        int pathX = path.get(0).getxPos();
        int pathY = path.get(0).getyPos();
        int[] actualNode = gridInfo.getGridCoordinateFromMap(pathX,pathY);

        return (actualNode[0]-PATH_NODE_RADIUS<=getXPos()&&actualNode[0]+PATH_NODE_RADIUS>=getXPos())
                &&(actualNode[1]-PATH_NODE_RADIUS<=getYPos()&&actualNode[1]+PATH_NODE_RADIUS>=getYPos());
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
        heading = correctHeading(heading);
    }

    private double correctHeading(double heading)
    {
        if (heading<0)
        {
            heading+=2*Math.PI;
        }
        else if (heading>=2*Math.PI)
            heading-=2*Math.PI;
        return heading;
    }
//todo: implement a set target heading method


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
