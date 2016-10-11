package com.saloon.android.bluecactus.app.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saloon.android.bluecactus.R;



import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saloon.android.bluecactus.app.Network.NetworkRequests;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.StringReader;
import java.util.Calendar;


public class Appointment extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener

{
    private TextView timeTextView;
    private TextView dateTextView;
    Button timeButton;
    Button dateButton;
    Button submitButton;
    String time;
    String date;
    EditText detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Find our View instances
        timeTextView = (TextView)findViewById(R.id.time_textview);
        dateTextView = (TextView)findViewById(R.id.date_textview);
         timeButton = (Button)findViewById(R.id.time_button);
         dateButton = (Button)findViewById(R.id.date_button);
        submitButton = (Button)findViewById(R.id.submit_appointmnet);

        // Show a timepicker when the timeButton is clicked
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        Appointment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),true
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Appointment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userID = (shared.getString("id", ""));


                detail = (EditText)findViewById(R.id.appointment_detail);
                NetworkRequests networkRequests = new NetworkRequests(Appointment.this);
                networkRequests.setAppointment(time,date,detail.getText().toString(),userID);

                Toast.makeText(Appointment.this, "Your Appointment is been submitted, please wait for the admin reply. Thanks.",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Appointment.this, MainActivity.class);

                Appointment.this.startActivity(intent);

                finish();


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        this.time = hourString+"h "+minuteString+"m";
        timeTextView.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        this.date = dayOfMonth+"/"+(monthOfYear)+"/"+year;
        dateTextView.setText(date);
    }
}
