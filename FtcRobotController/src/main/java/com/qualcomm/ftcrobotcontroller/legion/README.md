# Navigation
Note about direct navigation: Directly navigating to the waypoint would cause Legion to drive in that
direction until the robot reaches that area, so collisions may occur. Use direct navigation when
absolutely necessary. Generally, you'll want to turn off collision detection when Legion is direct
navigating.

It is also a good idea to scale down the navigation map (like a lower resolution picture) to
decrease the path finding time, however this does decrease the accuracy of Legion's navigation.