package com.example.abdullah.opencvframeget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class StartupActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtv1;
    Button btnStart;
    Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txtv1 = (TextView)findViewById(R.id.textView);
        btnStart=(Button)findViewById(R.id.btnStart);
        btnSetting=(Button)findViewById(R.id.btnSetting);
        Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.logo1);
        btnStart.setOnClickListener(this);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensettings();
            }
        });
        System.loadLibrary("main");
        /*String str=Tracker.hello1();
        txtv1.setText(Tracker.hello1());*/

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnStart.getId()) {
            //Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, Calibration.class);
            startActivity(intent);
            finish();
        }


    }

    void opensettings()
    {
        Intent settingsintent = new Intent(this, SettingsActivity.class);
        startActivity(settingsintent);

    }
    public void onBackPressed(){
      finish();
    }
}
