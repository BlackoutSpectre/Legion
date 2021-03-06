package com.qualcomm.ftcrobotcontroller.legion;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Blackout Spectre on 12/18/2015.
 */
public class Helper
{
    public static final String bitmapImageFilename = "/map.bmp";

    /**
     * gets the path to the configuration folder, if the path doesn't exist, then it will generate
     * that path.
     * @return null if external storage isn't present, the File folder if storage is present
     */
    public static File getBaseFolder()
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            File root = Environment.getExternalStorageDirectory();
            File baseConfigFolder = new File(root.getAbsolutePath()+ "/Legion Configuration");
            if (baseConfigFolder.exists())
            {
//                makeNoMediaFile();
                return baseConfigFolder;
            }
            else
            {
                baseConfigFolder.mkdir();
//                makeNoMediaFile();
                return baseConfigFolder;
            }
        }
        else
            return null;
    }
    public static void makeNoMediaFile()
    {
        makeNoMediaFile("");
    }
    public static void makeNoMediaFile(String dir)
    {
        File baseFolder = getBaseFolder();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(baseFolder, dir+"/.nomedia")));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static class UI
    {
        protected static Context context;
        public static int getIntFromString(String numberString, int defaultNumber)
        {
            int number;
            if (numberString.length()>0)
            {
                try
                {
                    number = Integer.parseInt(numberString);
                }
                catch (Exception e)
                {
                    number = defaultNumber;
                    //Toast.makeText(context.getApplicationContext(), "Bad number: \"" + numberString +
                    //        "\". Defaulting to \"" + defaultNumber +"\"", Toast.LENGTH_LONG).show();
                }
            }
            else {
                number = defaultNumber;
                //Toast.makeText(context.getApplicationContext(), "Bad number: \"" + numberString +
                //        "\". Defaulting to \"" + defaultNumber +"\"", Toast.LENGTH_LONG).show();
            }
            return number;
        }
        public static double getDoubleFromString(String numberString, double defaultNumber)
        {
            double number;
            if (numberString.length()>0)
            {
                try
                {
                    number = Double.parseDouble(numberString);
                }
                catch (Exception e)
                {
                    number = defaultNumber;
                    //Toast.makeText(context.getApplicationContext(), "Bad number: \"" + numberString +
                    //        "\". Defaulting to \"" + defaultNumber +"\"", Toast.LENGTH_LONG).show();
                }
            }
            else {
                number = defaultNumber;
                //Toast.makeText(context.getApplicationContext(), "Bad number: \"" + numberString +
                //        "\". Defaulting to \"" + defaultNumber +"\"", Toast.LENGTH_LONG).show();
            }
            return number;
        }
    }

    /**
     * Find the circumference of a circle from the radius. Inputting using one unit of measurement
     * will return in the same unit of measurement.
     * @param radius radius
     * @return circumference
     */
    public static double findCircumference(double radius)
    {
        return 2*radius*Math.PI;

    }

    /**
     * Inputting using one unit of measurement
     * will return in the same unit of measurement.
     * @param circumference circumference
     * @return radius
     */
    public static double findRadius(double circumference)
    {
        return circumference/(2*Math.PI);
    }
}
