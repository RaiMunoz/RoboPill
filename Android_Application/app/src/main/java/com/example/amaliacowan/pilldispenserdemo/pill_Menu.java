package com.example.amaliacowan.pilldispenserdemo;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.Calendar;
import java.util.Set;
import java.util.ArrayList;
import java.util.UUID;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;


public class pill_Menu extends AppCompatActivity {


        //initializing bluetooth settings
        //public static String EXTRA_ADDRESS = "device_address";
        Button btnconnect;
        Button manageschedule;
        Button btnOn, btnOff,medicinebtn;
        ListView devicelist;
        private int mYear, mMonth, mDay, mHour, mMinute;

        private BluetoothAdapter myBluetooth = null;
        private Set<BluetoothDevice> pairedDevices;

        //Second intent initialization
        String address = null;
        private ProgressDialog progress;
        BluetoothSocket btSocket = null;
        private boolean isBtConnected = false;
        //SPP UUID. Look for it
        static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        Intent intent;
        static int STATIC_INTEGER_VALUE =1;
        static int STATIC_INTEGER_MES =0;
        static int STATIC_INTEGER_LOG =0;
        String Message= null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            btnconnect= (Button)findViewById(R.id.button7);
            manageschedule = (Button)findViewById(R.id.button9);
            medicinebtn= (Button)findViewById(R.id.button8);

            btnOn = (Button)findViewById(R.id.button13);
            btnOff = (Button)findViewById(R.id.button14);

            btnconnect.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    intent = new Intent(pill_Menu.this, Bluetooth_Pair_Menu.class);
                    startActivityForResult(intent,STATIC_INTEGER_VALUE);
                }

            });

            btnOn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    turnOnLed();      //method to turn on
                }
            });

            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    turnOffLed();   //method to turn off
                }
            });

            btnOn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    turnOnLed();      //method to turn on
                }
            });

            btnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    turnOffLed();   //method to turn off
                }
            });

            manageschedule.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    intent = new Intent(pill_Menu.this, manage_schedule.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    //startActivity(intent);
                    startActivityForResult(intent, STATIC_INTEGER_MES);
                }

            });

            medicinebtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    intent = new Intent(pill_Menu.this, add_med_form.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    //startActivity(intent);
                    startActivityForResult(intent,STATIC_INTEGER_LOG);
                }

            });


            /*fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == STATIC_INTEGER_VALUE) {

                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        address = data.getStringExtra(Bluetooth_Pair_Menu.EXTRA_ADDRESS);
                        new ConnectBT().execute(); //Call the class to connect
                    }
            }
            else if( requestCode == STATIC_INTEGER_MES)
            {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Message = data.getStringExtra(manage_schedule.EXTRA_DATE);
                    if (btSocket!=null)
                    {
                        try
                        {
                            //btSocket.getOutputStream().write(Message.toString().getBytes());

                            btSocket.getOutputStream().write("op2;".getBytes());
                            //btSocket.getOutputStream().write("op1;".getBytes());

                        }
                        catch (IOException e)
                        {
                            msg("Error");
                        }
                    }

                }
            }

        }

        private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
        {
            private boolean ConnectSuccess = true; //if it's here, it's almost connected

            @Override
            protected void onPreExecute() {
                progress = ProgressDialog.show(pill_Menu.this, "Connecting...", "Please wait!!!");  //show a progress dialog
            }

            @Override
            protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
            {
                try {
                    if (btSocket == null || !isBtConnected) {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();//start connection
                    }
                } catch (IOException e) {
                    ConnectSuccess = false;//if the try failed, you can check the exception here
                }
                return null;
            }



            @Override
            protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
            {
                super.onPostExecute(result);

                if (!ConnectSuccess) {
                    msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                    //finish();
                } else {
                    msg("Connected.");
                    isBtConnected = true;
                }
                progress.dismiss();
            }
        }


        private void msg(String s)
        {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }

        private void turnOffLed()
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("op2;".toString().getBytes());
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                    String Timing = "On " + Integer.toString(mMonth+1) + "/" + Integer.toString(mDay) + "/" + Integer.toString(mYear) + " at " + Integer.toString(mHour) + ":" + Integer.toString(mMinute) + " the patient should take their medicine!;";
                    String Timestamp = Integer.toString(0)+Integer.toString(mMonth+1) + Integer.toString(mDay) + Integer.toString(mYear) + Integer.toString(mHour) + Integer.toString(mMinute)+";";

                    //btSocket.getOutputStream().write(Timestamp.getBytes());

                    //btSocket.getOutputStream().write(Timing.getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }

        private void turnOnLed()
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("op1;".toString().getBytes());
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                    String Timing = "On " + Integer.toString(mMonth+1) + "/" + Integer.toString(mDay) + "/" + Integer.toString(mYear) + " at " + Integer.toString(mHour) + ":" + Integer.toString(mMinute) + " the patient should take their medicine!;";
                    String Timestamp = Integer.toString(0)+Integer.toString(mMonth+1) +Integer.toString(mDay) + Integer.toString(mYear) + Integer.toString(mHour) + Integer.toString(mMinute)+";";
                    //btSocket.getOutputStream().write(Timestamp.getBytes());
                    //btSocket.getOutputStream().write(Timing.getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_menu, menu);
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
}
