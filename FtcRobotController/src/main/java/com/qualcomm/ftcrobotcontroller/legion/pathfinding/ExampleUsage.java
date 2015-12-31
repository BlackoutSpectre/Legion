/*    
    Copyright (C) 2012 http://software-talk.org/ (developer@software-talk.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.qualcomm.ftcrobotcontroller.legion.pathfinding;

import java.util.List;

/**
 * A simple example for the usage of this package.
 * 
 * @see ExampleFactory
 * @see ExampleNode
 */
public class ExampleUsage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Map<ExampleNode> myMap = new Map<ExampleNode>(20, 20, new ExampleFactory(),0);
        List<ExampleNode> path = myMap.findPath(10, 15, 19, 19);

        for (int i = 0; i < path.size(); i++) {
            System.out.print("(" + path.get(i).getxPosition() + ", " + path.get(i).getyPosition() + ") -> ");
        myMap.drawMap();
        }
    }


}
