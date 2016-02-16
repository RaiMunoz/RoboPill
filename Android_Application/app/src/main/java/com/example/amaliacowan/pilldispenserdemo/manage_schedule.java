/*package com.example.amaliacowan.pilldispenserdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class manage_schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}*/
package com.example.amaliacowan.pilldispenserdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import android.app.Activity;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import java.util.Calendar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.support.v7.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
public class manage_schedule extends AppCompatActivity implements
        View.OnClickListener {

    public static String EXTRA_DATE = "DATE";
    Button btnDatePicker, btnTimePicker, buttonSetAlarm;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int dYear, dMonth, dDay, dHour, dMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);

        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);

        buttonSetAlarm = (Button) findViewById(R.id.button2);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        buttonSetAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            dDay=dayOfMonth;
                            dMonth=monthOfYear;
                            dYear=year;
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                            dHour=hourOfDay;
                            dMinute=minute;
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();


        }

        if (v == buttonSetAlarm) {
            Calendar current = Calendar.getInstance();

            final Calendar cal = Calendar.getInstance();
            cal.set(dYear,
                    dMonth,
                    dDay,
                    dHour,
                    dMinute,
                    00);

            /*if (cal.compareTo(current) <= 0) {
                //The set Date/Time already passed
                Toast.makeText(getApplicationContext(),
                        "Invalid Date/Time",
                        Toast.LENGTH_LONG).show();
            } else {*/

            setAlarm(cal);
            String address = "On " + Integer.toString(dMonth) + "/" + Integer.toString(dDay) + "/" + Integer.toString(dYear) + " at " + Integer.toString(dHour) + ":" + Integer.toString(dMinute) + " the patient will take their medicine!;";

            // Make an intent to start next activity.
            Intent i = new Intent(manage_schedule.this, pill_Menu.class);
            //Change the activity.
            i.putExtra(EXTRA_DATE, address); //this will be received at ledControl (class) Activity
           setResult(android.app.Activity.RESULT_OK, i);
            finish();
        }
    }

           // setAlarm(cal);
            //finish();
            //}




    private void setAlarm(Calendar targetCal) {




       /* Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        l = getIntent().getExtras().getString("song");
        i.putExtra(AlarmClock.EXTRA_HOUR, pickerTime.getCurrentHour());
        i.putExtra(AlarmClock.EXTRA_MINUTES, pickerTime.getCurrentMinute());
        i.putExtra(AlarmClock.EXTRA_RINGTONE, l);*/
       Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm Set.", Toast.LENGTH_LONG).show();

        //finish();
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        //AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis()+(30*1000), pendingIntent);

    }
}





