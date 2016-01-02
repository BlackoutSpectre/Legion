package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.ftcrobotcontroller.legion.Helper;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Grid;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Map;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.PathingNode;


import java.io.File;

public class MapCompiler extends Activity {

    private Grid scaleInfo;
    private Map<PathingNode> actualTileGrid;
    private Map<PathingNode> scaledTileGrid;
    private File baseConfigFolder;
    ImageLoader imageLoader;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_compiler);
        baseConfigFolder = Helper.getBaseFolder();
        imageLoader = ImageLoader.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_compiler, menu);
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

    public void loadBitmap(View view)
    {

    }
}
