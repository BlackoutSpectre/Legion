package com.qualcomm.ftcrobotcontroller.legion;

/**
 * Created by Seth Chick on 7/8/2015.
 */

/*this class is used as a playmarker of what to implement in each procedure, this is required
  when the procedures are sorted in an arrayList. Subclasses can have any name as its identifier,
  but must always extend to this class.
 */
public abstract class CoreProcedure implements Comparable<CoreProcedure>
{

    AICore parent;
    //must have step numbers
    protected int step;
    private String procedureName; //must be predefined in this task.
    /**
     * The max score from completing this task.
     */
    private int score;

    public byte statusCode = 0;

    private int priority;

    /**
     * Constructs the object. These variables are immediately set from when the configuration
     * sets them. This object will be reconstructed with each change applied to it.
     * @param procedureName (constant) Set this within the constructor of the base class.
     * @param priority Set this when the object is changed within the config.
     * @param step (constant) The starting step value. Depends from how you set up the script.
     * @param scoreValue (constant) Set this within the constructor. The max score achieved when the procedure is completed.
     */
    public CoreProcedure(String procedureName, int priority, int step, int scoreValue)
    {
        setPriority(priority);
        this.procedureName = procedureName;
        this.step=step;
        score = scoreValue;
    }

    /**
     * This tells the program what to do when the robot is performing a task.
     * A core timer is continuously iterating this method until the task is finished or interupted.
     *
     * @return 0 for still in progress, 1 for this has finished and move on to next task, <0 for interupt and resume later
     */
    public abstract int procedure();

    /**
     * use this to reset the step number so that the task can be repeated without error when the
      robot has to interupt this task to do something else or this task can't be completed.
     */
    public abstract void resetStep();

    /**set the priority of the task (1 for first, higher nums to do later)
    * (unused) -2 = do not attempt
    * (unused) -1 = drop current and do this last few seconds*/
    public void setPriority(int priority)
    {
        this.priority=priority;
    }

    public int getPriority()
    {
        return priority;
    }

    /**must be implemented so that the file determining the priority can search for this task*/
    public int compareTo(CoreProcedure procedure)
    {
        return procedure.getPriority()-getPriority();
    }
    public String getProcedureName()
    {
        return procedureName;
    }

}
