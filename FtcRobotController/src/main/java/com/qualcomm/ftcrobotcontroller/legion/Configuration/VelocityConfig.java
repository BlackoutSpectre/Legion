package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.ftcrobotcontroller.legion.Helper;
import com.qualcomm.ftcrobotcontroller.legion.Navigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class VelocityConfig extends Activity {
    private EditText leftRadiusBox;
    private EditText rightRadiusBox;
    private EditText leftCircumBox;
    private EditText rightCircumBox;
    private File configFileDir = Navigation.WHEEL_CONFIG_FILE_DIR;
    private double leftRadius=-1;
    private double rightRadius=-1;
    private double leftCircum=-1;
    private double rightCircum=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velocity_config);

        leftRadiusBox = (EditText) findViewById(R.id.left_text_radius);
        rightRadiusBox = (EditText) findViewById(R.id.right_text_radius);
        leftCircumBox = (EditText) findViewById(R.id.left_text_circum);
        rightCircumBox = (EditText) findViewById(R.id.right_text_circum);

        if (configExists())
        {
            loadConfig();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_velocity_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean configExists()
    {
        return configFileDir.exists();
    }

    private void loadConfig()
    {
        if (configExists())
        {
            try {
                Scanner scanner = new Scanner(configFileDir);
                leftRadius = scanner.nextDouble();
                rightRadius = scanner.nextDouble();
                updateFromRadius();
                displayMeasurements();
                Toast.makeText(getApplicationContext(),"Existing configuration loaded",Toast.LENGTH_LONG)
                        .show();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),"Error finding config file: "+configFileDir.getPath()
                ,Toast.LENGTH_LONG).show();

                e.printStackTrace();

            }
        }
    }

    private void updateFromRadius()
    {
        leftCircum = Helper.findCircumference(leftRadius);
        rightCircum = Helper.findCircumference(rightRadius);
    }

    private void updateFromCircum()
    {
        leftRadius=Helper.findRadius(leftCircum);
        rightRadius=Helper.findRadius(rightCircum);
    }

    private boolean isAnyMeasurementUnset()
    {
        return leftRadius<=0||rightCircum<=0||leftCircum<=0||rightRadius<=0;
    }

    private void displayMeasurements()
    {

        if (leftRadius<=0)
            leftRadiusBox.setText("");
        else
            leftRadiusBox.setText(""+leftRadius);

        if(rightRadius<=0)
            rightRadiusBox.setText("");
        else
            rightRadiusBox.setText(""+rightRadius);

        if(leftCircum<=0)
            leftCircumBox.setText("");
        else
            leftCircumBox.setText(""+leftCircum);

        if(rightCircum<=0)
            rightCircumBox.setText("");
        else
            rightCircumBox.setText(""+rightCircum);
    }

    private void saveValues()
    {
        try {
            PrintWriter writer = new PrintWriter(configFileDir);
            writer.write(""+leftRadius+" "+rightRadius);
            writer.close();
            Toast.makeText(getApplicationContext(),"Files saved.",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveRadius(View view)
    {
        String right = rightRadiusBox.getText().toString();
        String left = leftRadiusBox.getText().toString();
        if(/*!isAnyMeasurementUnset()&&*/leftRadiusBox.getText().toString().length()!=0&&rightRadiusBox.getText().toString().length()!=0)
        {

            rightRadius=Helper.UI.getDoubleFromString(rightRadiusBox.getText().toString(),-1);
            leftRadius=Helper.UI.getDoubleFromString(leftRadiusBox.getText().toString(),-1);
            if(leftRadius>0&&rightRadius>0) {
                updateFromRadius();
                displayMeasurements();
                saveValues();
            }
            else
                Toast.makeText(getApplicationContext(),"Error: please set both values.",Toast.LENGTH_LONG)
                        .show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error: please set both values.",Toast.LENGTH_LONG)
            .show();
        }
    }

    public void saveCircum(View view)
    {
        if(/*!isAnyMeasurementUnset()&&*/leftCircumBox.getText().toString().length()!=0&&rightCircumBox.getText().toString().length()!=0)
        {
            rightCircum=Helper.UI.getDoubleFromString(rightCircumBox.getText().toString(),-1);
            leftCircum=Helper.UI.getDoubleFromString(leftCircumBox.getText().toString(),-1);
            if (leftCircum>0&&rightCircum>0) {
                updateFromCircum();
                displayMeasurements();
                saveValues();
            }
            else
                Toast.makeText(getApplicationContext(),"Error: please set both values.",Toast.LENGTH_LONG)
                        .show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error: please set both values.",Toast.LENGTH_LONG)
                    .show();
        }
    }
}
