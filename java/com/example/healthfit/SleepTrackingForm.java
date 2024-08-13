package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepTrackingForm extends AppCompatActivity {

    EditText wakeup, sleep;
    TextView username, note, homeTextView, logoutTextView;
    Button sleepTrackingBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    final Calendar myCalendar= Calendar.getInstance();
    EditText dateEt;
    String dateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracking_form);


        wakeup = (EditText) findViewById(R.id.wakeUpTime);
        sleep = (EditText) findViewById(R.id.sleepTime);
        note = (EditText) findViewById(R.id.sleepNote);
        dateEt=(EditText) findViewById(R.id.date);
        sleepTrackingBtn = (Button) findViewById(R.id.sleepSubmitBtn);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String dateToVerify = extras.getString("date");

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);
        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SleepTrackingForm.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        sleep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SleepTrackingForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr = String.format("%02d", selectedHour);
                        String minuteStr = String.format("%02d", selectedMinute);
                        sleep.setText(hourStr + ":" + minuteStr);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        wakeup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SleepTrackingForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr = String.format("%02d", selectedHour);
                        String minuteStr = String.format("%02d", selectedMinute);
                        wakeup.setText(hourStr + ":" + minuteStr);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });
        //if else

            sleepTrackingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String dateValuedummy = dateEt.getText().toString(); //from picker

                    FirebaseDatabase databaseChk = FirebaseDatabase.getInstance();
                    DatabaseReference dbRefChk = databaseChk.getReference("sleepTrack").child(name);
                    Query queryChk = dbRefChk.orderByChild("date").equalTo(dateValuedummy);

                    queryChk.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            if (!validateWakeup() | !validateSleep() | !validateNote() | !validateDate()) {
                                Toast.makeText(SleepTrackingForm.this, "Wrong!", Toast.LENGTH_SHORT).show();
                            } else {

                                // Get the current date
                                Date currentDate = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
                                String formattedCurrentDate = df.format(currentDate);

                                if (dateValuedummy.compareTo(formattedCurrentDate) > 0) {
                                    // Inputted date is later than the current date
                                    Toast.makeText(SleepTrackingForm.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (dataSnapshot.exists()) {
                                        //toast
                                        //redirected to last page
                                        Toast.makeText(SleepTrackingForm.this, "You already record today's data!!\nCome again tomorrow TvT", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SleepTrackingForm.this, SleepTracking.class).putExtra("name", name);
                                        startActivity(intent);
                                    }//if
                                    else {
                                        database = FirebaseDatabase.getInstance();
                                        reference = database.getReference("sleepTrack").child(name).push();

                                        String usernameValue = username.getText().toString();
                                        String noteValue = note.getText().toString();
                                        String sleepValue = sleep.getText().toString();
                                        String wakeupValue = wakeup.getText().toString();
                                        dateValue = dateEt.getText().toString();

                                        SleepTrackingData helperClass = new SleepTrackingData(sleepValue, wakeupValue, dateValue, noteValue, usernameValue);
                                        reference.setValue(helperClass);

                                        Toast.makeText(SleepTrackingForm.this, "You have added successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SleepTrackingForm.this, SleepTracking.class).putExtra("name", name);
                                        startActivity(intent);

                                    }//else
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });

        }//oncreate

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateEt.setText(dateFormat.format(myCalendar.getTime()));
    }
    public void openHome(String value){
        startActivity(new Intent(SleepTrackingForm.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(SleepTrackingForm.this, LoginActivity.class).putExtra("name", value));
    }
    public Boolean validateWakeup(){
        String val = wakeup.getText().toString();
        if (val.isEmpty()){
            wakeup.setError("Cannot be empty");
            return false;
        } else {
            wakeup.setError(null);
            return true;
        }
    }
    public Boolean validateSleep(){
        String val = sleep.getText().toString();
        if (val.isEmpty()){
            sleep.setError("Cannot be empty");
            return false;
        } else {
            sleep.setError(null);
            return true;
        }
    }
    public Boolean validateNote(){
        String val = note.getText().toString();
        if (val.isEmpty()){
            note.setError("Cannot be empty");
            return false;
        } else {
            note.setError(null);
            return true;
        }
    }
    public Boolean validateDate(){
        String val = dateEt.getText().toString();
        if (val.isEmpty()){
            dateEt.setError("Cannot be empty");
            return false;
        } else {
            dateEt.setError(null);
            return true;
        }
    }
}