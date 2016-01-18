package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.ftcrobotcontroller.legion.Navigation;

import java.io.File;
import java.io.FileNotFoundException;
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
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),"Error finding config file: "+configFileDir.getPath()
                ,Toast.LENGTH_LONG).show();

                e.printStackTrace();
                return;
            }
        }
    }

    private void updateFromRadius()
    {

    }
}
