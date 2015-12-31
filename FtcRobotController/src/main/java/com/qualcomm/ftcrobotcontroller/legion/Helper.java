package com.qualcomm.ftcrobotcontroller.legion;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Blackout Spectre on 12/18/2015.
 */
public class Helper
{

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
                return baseConfigFolder;
            }
            else
            {
                baseConfigFolder.mkdir();
                return baseConfigFolder;
            }
        }
        else
            return null;
    }
    public static class UI
    {
        protected static Context context;
        public int getIntFromString(String numberString, int defaultNumber)
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
    }
}