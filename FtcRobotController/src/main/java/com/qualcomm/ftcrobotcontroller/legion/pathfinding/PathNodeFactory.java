package com.qualcomm.ftcrobotcontroller.legion.pathfinding;

import java.io.Serializable;

/**
 * Created by Michael Chick on 7/15/2015.
 */
public class PathNodeFactory implements NodeFactory, Serializable {
    @Override
    public AbstractNode createNode(int x, int y) {
        return new PathingNode(x, y);
    }
}
