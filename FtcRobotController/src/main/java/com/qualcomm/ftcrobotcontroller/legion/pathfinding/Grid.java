package com.qualcomm.ftcrobotcontroller.legion.pathfinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Chick on 7/17/2015.
 */

/*to help with support for large grids, the size of the pathing map will be divided by how much
      space is needed from objects on the grid when generating a grid, so that the Map class is small
      but the grid is large. Essentially, a grid inside a grid.
     */
public class Grid implements Serializable
{
    private boolean[][] walkable;
    private int sizeX;
    private int sizeY;
    private int spacing;

    public Grid(int sizeX, int sizeY, int spacing)
    {
        setSizeX(sizeX);
        setSizeY(sizeY);
        if (spacing>0)
            this.spacing=spacing;
        else
            this.spacing = 1;
        walkable= new boolean[sizeX][sizeY];
        initializeGrid();
    }

    private void setSizeX(int x)
    {
        sizeX=x;
    }
    private void setSizeY(int y)
    {
        sizeY=y;
    }

    private void initializeGrid()
    {
        for (int y = 0; y<walkable.length; y++)
            for (int x = 0; x<walkable[y].length; x++)
                walkable[y][x] = true;
    }

/*    public int[] getMapCoordinate(int x, int y)
    {
        int[] coor = new int[2];
        coor[0] = x/spacing;
        coor[1] = y/spacing;
        return coor;
    }*/

    public int getGridSizeX()
    {
        return sizeX;
    }
    public int getGridSizeY()
    {
        return sizeY;
    }

    public int getMapSizeX()
    {
        return (int)Math.ceil((double)sizeX/spacing);
    }
    public int getMapSizeY()
    {
        return (int)Math.ceil((double)sizeY/spacing);
    }

    public int[] getMapCoordinateFromGrid(int x, int y)
    {
        int[] coor = new int[2];

        coor[0]=x/spacing;
        coor[1]=y/spacing;

        return coor;
    }

    public int[] getGridCoordinateFromMap(int x, int y)
    {
        int[] coor = new int[2];

        coor[0] = x*spacing+spacing;
        coor[1] = y*spacing+spacing;

        return coor;
    }

    public void setWalkable(int x, int y, boolean isWalkable)
    {
        walkable[y][x] = isWalkable;
    }

    public boolean getGridIsWalkable(int x, int y)
    {
        return walkable[y][x];
    }

    //checks to see if the point is within the bounds of the map
    public boolean isInBounds(int xPos, int yPos)
    {
        return ((yPos>=0&&yPos<walkable.length)&&(xPos>=0&&xPos<walkable[0].length));
    }

    //precondition: derive grid coordinates from map coordinates
    public boolean hasSpace(int[] coor)
    {
        if (coor.length!=2)
            return false;
        else
        {
            return hasSpace(coor[0], coor[1]);
        }
    }
    //determines if there is enough space at the calculated node from an unwalkable node
    //precondition: derive grid coordinates from map coordinates
    public boolean hasSpace(int xPos, int yPos)
    {
        for (int y = yPos-spacing; y<=yPos+spacing; y++)
            for (int x = xPos-spacing; x<=xPos+spacing; x++)
            {
                if (isInBounds(x, y)&&walkable[x][y])
                {

                }
                else
                    return false;
            }
        return true;
    }
}
