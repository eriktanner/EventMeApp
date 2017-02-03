package com.eventmeapp.eventmeapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Locale;

import static com.eventmeapp.eventmeapp.TabMap.getLatLng;


/**
 * Created by Erik Fok on 11/4/2016.
 */

public class CreateEvent extends AppCompatActivity {

    ConnectToServer connectionClass;
    StoreEvent storeNewEvent;
    ProgressBar pbBar;
    CheckBox checkBox;

    EditText etTitle, etLocation, etDescription;
    TextView tvDate, tvTime, tvAMPM;
    Calendar myCalendar, myTimeCalendar;
    private int year, month, day, mHour, mMinute;
    private boolean isDateSet = false, isTimeSet = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_plainheader);
        setContentView(R.layout.activity_create_event);

        etTitle = (EditText) findViewById(R.id.et_title);
        etLocation = (EditText) findViewById(R.id.et_location);
        etDescription = (EditText) findViewById(R.id.et_description);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAMPM = (TextView) findViewById(R.id.tv_am_pm);
        myCalendar = Calendar.getInstance();
        myTimeCalendar = Calendar.getInstance();
        Button btCreateEvent = (Button) findViewById(R.id.bt_create_event);
        checkBox = (CheckBox) findViewById(R.id.cbPrivateEvent);
        pbBar = (ProgressBar) findViewById(R.id.progressBarCreateEvent);
        pbBar.setVisibility(View.GONE);


        final LatLng mLatLng = getLatLng(); //LatLng of point user held down to create event



        //DatePicker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int yearPicked, int monthOfYear,
                                  int dayOfMonth) {
                day = dayOfMonth;
                month = monthOfYear;
                year = yearPicked;

                myCalendar.set(Calendar.YEAR, yearPicked);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                isDateSet = true;

                updateLabelDate();
            }

        };

        tvDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //TimePicker
        tvTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                mHour = 12;
                mMinute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mHour = selectedHour;
                        mMinute = selectedMinute;
                        tvTime.setText(pad(selectedHour, true) + ":" + pad(selectedMinute, false));
                        updateAMPM();
                    }
                }, mHour, mMinute, false);
                isTimeSet = true;
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        //CreateButton
        btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if all needed data is set
                if (etTitle.getText().toString().compareTo("") != 0 && etLocation.getText().toString().compareTo("") != 0
                        && isDateSet && isTimeSet) {

                    storeNewEvent = new StoreEvent();// this is the Asynctask
                    storeNewEvent.execute("");

                } else {
                    Toast.makeText(getApplicationContext(), "Not all required fields were filled.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void updateLabelDate() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tvDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateAMPM() {
        if (mHour < 12)
            tvAMPM.setText("AM");
        else
            tvAMPM.setText("PM");
    }

    private static String pad(int c, boolean isHour) {
        if (c == 0 && isHour)
            return "12";
        else if (c > 12 && isHour)
            return String.valueOf(c - 12);
        else if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public int itemClicked(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            return 1; //Event is Private
        } else {
            return 0;
        }
    }








    //Sends data to database
    public class StoreEvent extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String sTitle = etTitle.getText().toString();
        String sLocation = etLocation.getText().toString();
        String sTime = ""+ year + "-" + month + 1 + "-" + day + " " + mHour + ":" + pad(mMinute, false)+ ":" + "00";
        String sDescription = etDescription.getText().toString();
        int iGroupKey = 1; //TODO
        double myLong = getLatLng().longitude;
        double myLat = getLatLng().latitude;
        int isPrivate = itemClicked(checkBox);


        @Override
        protected void onPreExecute() {
            pbBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbBar.setVisibility(View.GONE);
            Toast.makeText(CreateEvent.this, r, Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                onBackPressed();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                connectionClass = new ConnectToServer();
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with Server";
                } else {
                    String query = "INSERT INTO events (EventName,EventLocation,EventDateTime,GroupKey,EventLatLng,EventDescription,IsPrivate)\n" +
                            "VALUES ('" + sTitle + "','" + sLocation + "','" + sTime+ "','" + iGroupKey + "','Point(" +myLong +" " + myLat + ")','"
                            + sDescription+"'," + isPrivate +")";
                    PreparedStatement ps = con.prepareStatement(query);
                    int rs = ps.executeUpdate();

                    if(rs >= 0) {
                        z = "Event Successfully Created";
                        isSuccess=true;
                    } else {
                        z = "Event Creation Failed";
                        isSuccess = false;
                    }

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exception" +sTime+ ex.getMessage();
            }

            return z;
        }

    }

}