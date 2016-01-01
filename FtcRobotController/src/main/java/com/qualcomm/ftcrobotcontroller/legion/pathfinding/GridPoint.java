package com.qualcomm.ftcrobotcontroller.legion.pathfinding;

/**
 * Created by Michael Chick on 7/23/2015.
 * @deprecated
 */
public class GridPoint implements Comparable<GridPoint> {
    private int x;
    private int y;
    private boolean walkable;
    public GridPoint(int x, int y, boolean walkable)
    {
        setX(x);
        setY(y);
        setWalkable(walkable);
    }

    private void setX(int x)
    {
        this.x=x;
    }
    private void setY(int y)
    {
        this.y=y;
    }

    public void setWalkable(boolean walkable)
    {
        this.walkable=walkable;
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public boolean getWalkable()
    {
        return walkable;
    }

    public int compareTo(GridPoint other)
    {
        return other.toString().compareTo(this.toString());
    }

    public String toString()
    {
        return "" +getX() +" " +getY();
    }
}
