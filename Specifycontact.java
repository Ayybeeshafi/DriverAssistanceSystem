package com.example.abdullah.opencvframeget;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Specifycontact extends AppCompatActivity implements View.OnClickListener {


    String[] items = {};
    private ArrayList<String> data = new ArrayList<String>(Arrays.asList(items));
    ListView lv;
    Boolean Update = false;
    View Current;
    ImageView abt_us;
    ImageView add_contct;
    String cNumber,cName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specifycontact);
        lv = (ListView) findViewById(R.id.listview);
        //   generateListContent();
        loadInfo();
        lv.setAdapter(new MyListAdaper(this, R.layout.list_item, data));
        abt_us=(ImageView)findViewById(R.id.abt_us);
        add_contct=(ImageView)findViewById(R.id.add_contact);
        abt_us.setOnClickListener(this);
        add_contct.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent dataa) {

        super.onActivityResult(reqCode, resultCode, dataa);

        if (reqCode == 1) {

            if (dataa != null && resultCode == RESULT_OK) {

                Uri contactData = dataa.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                        phones.moveToFirst();
                        cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        cName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        //  Toast.makeText(getApplicationContext(), cName + " : " + cNumber, Toast.LENGTH_SHORT).show();
                        //setCn(cNumber);
                        if (Update == true) {
                            UpdateListContent(cName + " : " + cNumber);
                        }
                        if (Update == false) {
                            generateListContent(cName + " : " + cNumber);
                        }

                    }


                }
            }
        }
    }

    private void generateListContent(String a) {
        data.add(a);
        lv.setAdapter(new MyListAdaper(this, R.layout.list_item, data));
    }

    private void UpdateListContent(String s) {
        TextView tv = (TextView) Current.findViewById(R.id.list_item_text_Number);
        tv.setText(cName);


    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }*/

   /* @Override
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
        if (id == R.id.action_Add) {

            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;

        void Update() {
            notifyDataSetChanged();
        }

        private List<String> mObjects;

        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();


                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text_Number);
                viewHolder.buttonU = (Button) convertView.findViewById(R.id.list_item_btn_Update);
                viewHolder.buttonD = (Button) convertView.findViewById(R.id.list_item_btn_Delete);

                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            final View finalConvertView = convertView;
            mainViewholder.buttonU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Update = true;
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    Current = finalConvertView;
                    startActivityForResult(intent, 1);
                }

            });

            mainViewholder.buttonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();

                }
            });

            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public class ViewHolder {

        TextView title;
        Button buttonD;
        Button buttonU;

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
        if(v.getId()==add_contct.getId())
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        saveInfo();
    }
    public void saveInfo() {
        SharedPreferences sharedpref = getSharedPreferences("list" , Context.MODE_PRIVATE);
        sharedpref.edit().clear().commit();
        SharedPreferences.Editor editor = sharedpref.edit();
        for(int i=0;i<data.size();i++)
        {
            editor.putString("List"+i , data.get(i));
        }
        editor.putString("ListSize" , String.valueOf(data.size()));
        editor.apply();
    }
    private void loadInfo() {
        SharedPreferences sharedpref = getSharedPreferences("list" , Context.MODE_PRIVATE);
        String ListSize = sharedpref.getString("ListSize","0");

        int total= Integer.parseInt(ListSize);
        for(int i=0;i<total;i++)
        {
            String li=sharedpref.getString("List"+i,"");
            data.add(i,li);


        }

    }

}





