package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
//import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.ftcrobotcontroller.legion.Helper;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Grid;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.Map;
import com.qualcomm.ftcrobotcontroller.legion.pathfinding.PathingNode;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MapCompiler extends Activity {

    private Grid scaleInfo;
    private Map<PathingNode> actualTileGrid;
    private Map<PathingNode> scaledTileGrid;
    private File baseConfigFolder;
//    ImageLoader imageLoader;
    ProgressBar progressBar;
    ImageView bitmapPreview;
    TextView statusText;
    BitmapFactory.Options options;
    Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_compiler);
        baseConfigFolder = Helper.getBaseFolder();
//        imageLoader = ImageLoader.getInstance();


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bitmapPreview = (ImageView) findViewById(R.id.imageView);
        statusText = (TextView) findViewById(R.id.status_message_text);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmapPreview.setAdjustViewBounds(true);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
        File bitmapFile = new File(Helper.getBaseFolder(),Helper.bitmapImageFilename);
        try {
            FileInputStream fileInputStream = new FileInputStream(bitmapFile);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            bitmapPreview.setMaxHeight(bitmap.getHeight());
            bitmapPreview.setMaxWidth(bitmap.getWidth());
            bitmapPreview.setImageBitmap(bitmap);



        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),"File not found: "+bitmapFile.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        /*DisplayImageOptions options = new DisplayImageOptions.Builder().build();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getBaseContext()).build();
        imageLoader.init(imageLoaderConfiguration);
        imageLoader.displayImage(baseConfigFolder.toString() + Helper.bitmapImageFilename, bitmapPreview, options,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        bitmapPreview.setMaxHeight(bitmap.getHeight());
                        bitmapPreview.setMaxWidth(bitmap.getWidth());
                        bitmapPreview.setImageBitmap(bitmap);
                        setBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                }, new ImageLoadingProgressListener(){
                    @Override
                    public void onProgressUpdate(String s, View view, int i, int i1) {
                        progressBar.setMax(i1);
                        progressBar.setProgress(i);
                    }
        });*/

    }
}
