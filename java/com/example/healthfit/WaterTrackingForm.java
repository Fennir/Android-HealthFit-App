package com.example.healthfit;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WaterTrackingForm extends AppCompatActivity {

    EditText water;
    TextView username, homeTextView, logoutTextView;;
    Button waterTrackingBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    final Calendar myCalendar= Calendar.getInstance();
    EditText dateEt, timeEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_tracking_form);

        //variable declaration
        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);
        water = (EditText) findViewById(R.id.waterValue);
        dateEt=(EditText) findViewById(R.id.date);
        timeEt = (EditText) findViewById(R.id.time);
        waterTrackingBtn = (Button) findViewById(R.id.waterTrackingButton);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        //process
        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

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
                new DatePickerDialog(WaterTrackingForm.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WaterTrackingForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr = String.format("%02d", selectedHour);
                        String minuteStr = String.format("%02d", selectedMinute);
                        timeEt.setText(hourStr + ":" + minuteStr);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        waterTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateWater() || !validateDate() || !validateTime()) {
                    Toast.makeText(WaterTrackingForm.this, "Wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the current date
                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
                    String formattedCurrentDate = df.format(currentDate);

                    // Get the inputted date
                    String dateValue = dateEt.getText().toString();

                    // Compare the inputted date with the current date
                    if (dateValue.compareTo(formattedCurrentDate) > 0) {
                        // Inputted date is later than the current date
                        Toast.makeText(WaterTrackingForm.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Valid date, proceed with saving the data
                        database = FirebaseDatabase.getInstance();
                        reference = database.getReference("waterTrack").child(name).push();

                        String usernameValue = username.getText().toString();
                        String waterValue = water.getText().toString();
                        String timeValue = timeEt.getText().toString();

                        WaterTrackingData helperClass = new WaterTrackingData(waterValue, dateValue, timeValue, usernameValue);
                        reference.setValue(helperClass);

                        Toast.makeText(WaterTrackingForm.this, "You have added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WaterTrackingForm.this, WaterTracking.class).putExtra("name", name);
                        startActivity(intent);
                    }
                }
            }
        });


    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateEt.setText(dateFormat.format(myCalendar.getTime()));
    }
    public void openHome(String value){
        startActivity(new Intent(WaterTrackingForm.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(WaterTrackingForm.this, LoginActivity.class).putExtra("name", value));
    }
    public Boolean validateWater(){
        String val = water.getText().toString();
        if (val.isEmpty()){
            water.setError("Cannot be empty");
            return false;
        } else {
            water.setError(null);
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
    public Boolean validateTime(){
        String val = timeEt.getText().toString();
        if (val.isEmpty()){
            timeEt.setError("Cannot be empty");
            return false;
        } else {
            timeEt.setError(null);
            return true;
        }
    }
}