package com.qualcomm.ftcrobotcontroller.legion.Configuration;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qualcomm.ftcrobotcontroller.R;

public class ConfigurationOpener extends ListActivity {
    String[] menuOptions = {"General", "Field Map Compiler",
            "Waypoints","Spawnpoints","Velocity Detection Setup", "Procedures"};



    ListView options;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_opener);

        //String[] menuOptions = {"General", getApplicationContext().getResources().getString(R.string.legion_map_compiler_opener),
        //"Waypoints","Spawnpoints","Velocity Detection Setup", "Procedures"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),android.R.layout.
        //simple_list_item_1, menuOptions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.
                simple_list_item_1, menuOptions);

        options = (ListView) findViewById(android.R.id.list);
        options.setAdapter(adapter);
        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position)
                {

                    case 0:
                        Toast.makeText(getApplicationContext(),menuOptions[0],Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        intent = new Intent(getBaseContext(),MapCompiler.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getBaseContext(),VelocityConfig.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Not found: " +position,Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration_opener, menu);
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

    public void onItemClick()
    {

    }
}
