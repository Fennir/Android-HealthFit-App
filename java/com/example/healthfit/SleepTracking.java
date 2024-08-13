package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class SleepTracking extends AppCompatActivity {

    TextView username, sleep, wakeup, note,homeTextView, logoutTextView, totalHour;
    Button addBtn, refreshBtn;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    String nameHolder;
    SleepTrackingData sleepData;
    EditText dateVt;
    final Calendar myCalendar= Calendar.getInstance();
    String strWakeup, strSleep, strNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracking);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        String formattedDate = df.format(c);

        dateVt = (EditText) findViewById(R.id.currentDate);
        refreshBtn = (Button) findViewById(R.id.refreshButton);

        dateVt.setText(formattedDate);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dateVt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SleepTracking.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        refreshBtn.setOnClickListener(v -> {
            String selectedDate = dateVt.getText().toString().trim();

            // Refresh the query with the new date
            viewList(selectedDate);
        });

        viewList(formattedDate);

    }//on creaate

    public void viewList(String dateValue){

        addBtn = (Button) findViewById(R.id.addSleepTracking);
        sleepData = new SleepTrackingData();
        database = FirebaseDatabase.getInstance();
        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);
        totalHour = (TextView)findViewById(R.id.totalhour);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String date = dateValue;
        nameHolder = name;

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        sleep = (TextView) findViewById(R.id.sleepView);
        wakeup = (TextView) findViewById(R.id.wakeupView);
        note = (TextView) findViewById(R.id.sleepNote);

        dbRef = database.getReference("sleepTrack").child(name);

        Query query = dbRef.orderByChild("date").equalTo(date);

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double totalH=0.0;

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        strWakeup = ds.child("wakeupTime").getValue(String.class);
                        strSleep = ds.child("timeSleep").getValue(String.class);
                        strNote = ds.child("sleepNote").getValue(String.class);

                        SleepTrackingData sleepData = new SleepTrackingData(strSleep, strWakeup, date, strNote, name);

                        // Update UI with the retrieved data
                        sleep.setText(sleepData.getTimeSleep());
                        wakeup.setText(sleepData.getWakeupTime());
                        note.setText(sleepData.getSleepNote());

                        if (!TextUtils.isEmpty(strWakeup) && !TextUtils.isEmpty(strSleep)) {
                            try {
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
                                Date wakeupTime = timeFormat.parse(strWakeup);
                                Date sleepTime = timeFormat.parse(strSleep);

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(wakeupTime);
                                int wakeupHour = calendar.get(Calendar.HOUR_OF_DAY);
                                int wakeupMinute = calendar.get(Calendar.MINUTE);

                                calendar.setTime(sleepTime);
                                int sleepHour = calendar.get(Calendar.HOUR_OF_DAY);
                                int sleepMinute = calendar.get(Calendar.MINUTE);

                                if (wakeupHour < sleepHour || (wakeupHour == sleepHour && wakeupMinute < sleepMinute)) {
                                    // Crossing midnight, adjust the wakeup time by adding 24 hours
                                    calendar.setTime(wakeupTime);
                                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                                    wakeupTime = calendar.getTime();
                                }

                                long timeDifference = wakeupTime.getTime() - sleepTime.getTime();
                                double sleepingHours = timeDifference / (1000.0 * 60 * 60);
                                totalH += sleepingHours;
                            } catch (ParseException e) {
                                // Handle the exception or display an error message
                            }
                        }

                        totalHour.setText("Your sleeping hour: " + totalH + " hours");
                    }
                }

                else {
                    sleep.setText("time");
                    totalHour.setText("hours");
                    wakeup.setText("time");
                    note.setText("You haven't record your sleep yet..");
                }//else

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addBtn.setOnClickListener(v -> {
            openSleepTrack(name, date);
        });

    } //viewList

    public void openSleepTrack(String value, String value2){ startActivity(new Intent(SleepTracking.this, SleepTrackingForm.class).putExtra("name", value).putExtra("date", value2)); }
    public void openHome(String value){
        startActivity(new Intent(SleepTracking.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(SleepTracking.this, LoginActivity.class).putExtra("name", value));
    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateVt.setText(dateFormat.format(myCalendar.getTime()));
    }
    public void onBackPressed() {
        Intent intent = new Intent(SleepTracking.this, MainActivity.class).putExtra("name", nameHolder);
        startActivity(intent);
    }
}