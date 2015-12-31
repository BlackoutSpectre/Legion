/**
 * Created by Seth Chick on 7/8/2015.
 */

package com.qualcomm.ftcrobotcontroller.legion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//todo: make procedure config file to read
//todo: make priority sorter
public class AICore {

    //private ArrayList<CoreProcedure> procedures = new ArrayList<CoreProcedure>();
    /**
     * sister class for interaction between AICore and Navigation for the use of procedures.
     *
     * @see AICore
     * @see Navigation
     */
    public Navigation sister;
    /**
     * This arraylist is the different time segments for the procedures.
     * when the next time index is reached, the current procedure will be aborted
     * and the AI will move to the next time segment and start those procedures.
     *
     * Will be loaded from configuration file
     */
    private ArrayList<LinkedList<CoreProcedure>> queuedProcedures;
    /**
     * different time indexes for the queued procedures
     *
     * loaded from file
     */
    private ArrayList<Long> timeIndexes;

    private List<CoreProcedure> finishedProcedures = new LinkedList<CoreProcedure>();
    private List<CoreProcedure> failedProcedures = new LinkedList<CoreProcedure>();

    static final String procedureListFileName = "queuedProcedures.list";
    static final String timeSegmentFileName = "timeSegments.list";





    /**
     * loads the AI core and the procedures along with the config from file
     */
    public AICore() throws IOException, ClassNotFoundException {
        loadAll();
    }

    /**
     * called by constructor, will load the following files
     */
    private void loadAll() throws IOException, ClassNotFoundException {
        File root = Helper.getBaseFolder();
        File procedureListFile = new File(root, procedureListFileName);
        FileInputStream procedureFileInputStream = new FileInputStream(procedureListFile);
        ObjectInputStream procedureObjectLoader = new ObjectInputStream(procedureFileInputStream);
        queuedProcedures = (ArrayList<LinkedList<CoreProcedure>>) procedureObjectLoader.readObject();
        procedureObjectLoader.close();

        File segmentListFile = new File(root, timeSegmentFileName);
        FileInputStream segmentInputStream = new FileInputStream(segmentListFile);
        ObjectInputStream segmentObjectLoader = new ObjectInputStream(segmentInputStream);
        timeIndexes = (ArrayList<Long>) segmentObjectLoader.readObject();
        segmentObjectLoader.close();

    }

    public void start()
    {

    }

}
