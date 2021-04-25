package com.example.abdullah.opencvframeget;

import android.app.ActionBar;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView t1;
    Switch laneswitch;
    Switch vehicleswitch;
    ImageView abt_us;
    public static boolean lane_switch=true;
    public static boolean vehicle_switch=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.distance_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        t1 = (TextView) findViewById(R.id.textView);
        t1.setOnClickListener(this);
        abt_us=(ImageView) findViewById(R.id.abt_us);
        abt_us.setOnClickListener(this);
        laneswitch = (Switch) findViewById(R.id.switchlane);
        laneswitch.setChecked(lane_switch);
        laneswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    laneswitch.setChecked(true);
                    lane_switch=true;
                }
                else
                {
                    laneswitch.setChecked(false);
                    lane_switch=false;
                }
            }
        });
        vehicleswitch = (Switch) findViewById(R.id.switchvehicle);
        vehicleswitch.setChecked(vehicle_switch);
        vehicleswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    vehicleswitch.setChecked(true);
                    vehicle_switch=true;
                }
                else
                {
                    vehicleswitch.setChecked(false);
                    vehicle_switch=false;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==abt_us.getId())
        {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_id));
            // set a message
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(" Developed By: \r\n\n Syed Ali Kazim \n And \n Abdullah Akbar.\r\n \r\n All Rights Reserved.");
            // Toast configuration
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }
        else {
            Intent specifyintent = new Intent(this, Specifycontact.class);
            startActivity(specifyintent);
        }
       // finish();

    }

    public void onBackPressed(){
        Intent mainintent = new Intent(this, StartupActivity.class);
        startActivity(mainintent);
        finish();
    }



    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);

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
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_id));
            // set a message
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(" Developed By: \r\n\n Syed Ali Kazim \n And \n Abdullah Akbar.\r\n \r\n All Rights Reserved.");
            // Toast configuration
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

*/


}
