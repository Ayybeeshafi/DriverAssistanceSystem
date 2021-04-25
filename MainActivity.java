package com.example.abdullah.opencvframeget;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends Activity implements CvCameraViewListener2, View.OnClickListener, LocationListener {
    private static final String TAG = "OCVSample::Activity";
    Button btn, btn_settings, btn_map;
    String filename;
    Uri image_uri;
    File image_file;
    Bitmap image_bit;
    String encoded_string=null;
    String image_name=null;
    String str, receiver, message;
    double lat, lang;
    String RNumber;

    String Receiver_Number,Num_R;
    LocationManager lm1;

    private Tutorial3View mOpenCvCameraView;

    Toast toast_both,toast_lane,toast_vehicle;
    MediaPlayer both_media,lane_media,vehicle_media;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        //int region=Calibration.roi;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        System.loadLibrary("opencv_java3");
        setContentView(R.layout.activity_main);
        mOpenCvCameraView = (Tutorial3View) findViewById(R.id.tutorial3_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        //mOpenCvCameraView.enableFpsMeter();
        mOpenCvCameraView.setMaxFrameSize(400, 320);
        btn = (Button) findViewById(R.id.screenshot);
        btn.setOnClickListener(this);
        btn_settings = (Button) findViewById(R.id.settingsbutton);

        //Warning toasts
        LayoutInflater inflater = getLayoutInflater();
        View layout_both = inflater.inflate(R.layout.both_warning, (ViewGroup) findViewById(R.id.lane_layout_id));
        View layout_lane = inflater.inflate(R.layout.lane_warning, (ViewGroup) findViewById(R.id.lane_layout_id));
        View layout_vehicle = inflater.inflate(R.layout.vehicle_warning, (ViewGroup) findViewById(R.id.lane_layout_id));
        toast_both = new Toast(this);
        toast_both.setGravity(Gravity.CENTER, 0, 0);
        toast_both.setDuration(Toast.LENGTH_SHORT);
        toast_both.setView(layout_both);
        toast_lane = new Toast(this);
        toast_lane.setGravity(Gravity.CENTER, 0, 0);
        toast_lane.setDuration(Toast.LENGTH_SHORT);
        toast_lane.setView(layout_lane);
        toast_vehicle = new Toast(this);
        toast_vehicle.setGravity(Gravity.CENTER, 0, 0);
        toast_vehicle.setDuration(Toast.LENGTH_SHORT);
        toast_vehicle.setView(layout_vehicle);
        //warning toasts end here

        //Warning sounds
        both_media = MediaPlayer.create(this, R.raw.warning);
        lane_media = MediaPlayer.create(this, R.raw.warning);
        vehicle_media = MediaPlayer.create(this, R.raw.alarm);
        //Warnng sounds end here
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        btn_map = (Button) findViewById(R.id.locationbutton);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmaps();
            }
        });


        IntentFilter filter = new IntentFilter();
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(new MyReceiver(), filter);

        lm1 = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, Criteria.ACCURACY_COARSE, (LocationListener) this);


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }


    int frame_count = 0;
    Boolean lane_flag;

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        Mat src = inputFrame.rgba();
        org.opencv.core.Size sz = inputFrame.rgba().size();
        frame_count++;
        int track_chk=0;
        int warning_flag=0;
        if (SettingsActivity.lane_switch&&SettingsActivity.vehicle_switch)
        {
            track_chk=1;
        }
        else if(SettingsActivity.lane_switch)
        {
            track_chk=2;
        }
        else if(SettingsActivity.vehicle_switch)
        {
            track_chk=3;
        }

        warning_flag = Tracker.tracking_engine(src.nativeObj, frame_count,track_chk,Calibration.roi);
        if (warning_flag==1) {
            toast_lane.show();
            lane_media.start();
        }
        else if(warning_flag==2)
        {
            toast_vehicle.show();
            vehicle_media.start();
        }
        else if(warning_flag==3)
        {
            toast_both.show();
            both_media.start();
        }

        return src;
    }




    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick event");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String fileName = Environment.getExternalStorageDirectory().getPath() + "/sample_picture_" + currentDateandTime + ".jpg";
        mOpenCvCameraView.takePicture(fileName);
        Toast.makeText(this, "captured and saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartupActivity.class);
        startActivity(intent);
        finish();

    }

    void openSettings() {
        Intent settingsintent = new Intent(this, SettingsActivity.class);
        startActivity(settingsintent);
    }

    void openmaps() {
        Intent mapintent = new Intent(this, MapsActivity.class);
        startActivity(mapintent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {

        lat= location.getLatitude();
        lang=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }






    private void makeRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://houseoftechnology.com.pk/thirdeye/connection.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Uploading" + response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("encoded_string", encoded_string);
                map.put("image_name", image_name);
                map.put("Number", Receiver_Number);

                return map;
            }
        };
        requestQueue.add(request);
    }

    class MyReceiver extends BroadcastReceiver {

        public MyReceiver() {
            Toast.makeText(getApplicationContext(), "Tracking Started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving


            Bundle bundle = intent.getExtras();
            SmsMessage[] recievedMsgs = null;
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                recievedMsgs = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    recievedMsgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    str = "SMS from " + recievedMsgs[i].getOriginatingAddress() +
                            " : " + recievedMsgs[i].getMessageBody().toString();
                    receiver = recievedMsgs[i].getOriginatingAddress();
                    message = recievedMsgs[i].getMessageBody().toString();

                    Num_R= receiver;
                    receiver = receiver.substring(receiver.length() - 7);
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();


                }
            }



            if (message.startsWith("LocationRequested") && checkNumber(receiver) ) {



                String[] parts = str.split("<>");
                Receiver_Number = parts[1];

                Toast.makeText(getApplicationContext(), "Message Reciever is Working , " + Num_R, Toast.LENGTH_SHORT).show();


                new EncodeImage().execute();

            }
            else if(message.startsWith("LocationRequested"))
            {
                Toast.makeText(getApplicationContext(), "Not Authorised", Toast.LENGTH_SHORT).show();
                SmsManager sms= SmsManager.getDefault();
                sms.sendTextMessage(Num_R,null, "Not Authorised for Location Request",null,null);
            }
        }





    }

    private void sendmessage()
    {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
           @Override
            public void run() {
                SmsManager sms= SmsManager.getDefault();
                sms.sendTextMessage(Num_R,null, "LocationRequested <>"+lat+","+lang,null,null);

           }
        }, 10000);

    }
    boolean checkNumber(String Number)
    {
        SharedPreferences sharedpref = getSharedPreferences("list" , Context.MODE_PRIVATE);
        String ListSize = sharedpref.getString("ListSize","0");
        String li="";
        Boolean num_chk=false;
        int total= Integer.parseInt(ListSize);
        for(int i=0;i<total;i++)
        {
            li=sharedpref.getString("List"+i,"0");
            if(li.contains(Number))
            {
                RNumber=li;
                num_chk=true;
            }


        }
            return num_chk;
    }


    private class EncodeImage extends AsyncTask <Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            image_name="SenderNumberr.jpg"; //Image name
            filename="/storage/emulated/0/SenderNumberr.jpg"; //Image location
            mOpenCvCameraView.takePicture(filename); //take screenshot
            image_file= new File(filename); // save screenshot
            image_uri=Uri.fromFile(image_file); //convery image to Uri
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            image_bit=BitmapFactory.decodeFile(image_uri.getPath()); //convery jpeg to bitmap
            ByteArrayOutputStream stream = new ByteArrayOutputStream(); //convery bitmap to string
            image_bit.compress(Bitmap.CompressFormat.JPEG, 15, stream); // Compress image. 15 is compression quailty 100= best quailty
            byte[] array = stream.toByteArray(); //string into array
            encoded_string= Base64.encodeToString(array,0); //String to base 64 format to be converted on server using PHP
            //image_file.delete();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            image_file.delete();
            makeRequest(); // makes request to server to upload image
            sendmessage();
        }
    }
}
