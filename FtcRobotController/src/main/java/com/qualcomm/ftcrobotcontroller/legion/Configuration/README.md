#MapCompiler
    #Instructions:
 1. Make a BMP image named "field.bmp", max red channel for ramp (this will prevent Legion from seeing obstacles
 in these pixel), max blue channel for permanent wall (Legion will always consider this pixel has an
 obstacle), combine both channels at max to have both properties, black color means the area is empty.
 Note: a pixel represents a square centimeter on the field.
 2. Place the image in the root android directory under "/Legion Configuration", create one if not
 present. (If the program looks for or writes a file under this directory, it will generate the folder.
 3. Set resolution scale (this will decrease the compiled resolution of the map). Lower resolution
 makes it easier and faster for Legion to find a path (and takes less RAM), however,
 this will decrease the accuracy of the path.
 4. Press "update map" to see the preview (if the feature is built)
 5. When satisfied, press "Save and Compile" to save changes and compile the map for Legion to use.