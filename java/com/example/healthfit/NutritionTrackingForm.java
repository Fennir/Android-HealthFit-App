package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NutritionTrackingForm extends AppCompatActivity {

    EditText proteinIntake;
    EditText carbohydratesIntake;
    EditText fatIntake;
    final Calendar myCalendar= Calendar.getInstance();
    EditText dateEt, timeEt;
    Button nutritionTrackingBtn;

    TextView username, homeTextView, logoutTextView;

    Button submitButton;

    FirebaseDatabase database;
    DatabaseReference reference;

    Calendar selectedDate;
    Calendar selectedTime;

    String dateHolder, timeHolder, totalCaloriesHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritional_tracking_form);

        proteinIntake = findViewById(R.id.proteinIntake);
        carbohydratesIntake= findViewById(R.id.carbohydratesIntake);
        fatIntake = findViewById(R.id.fatIntake);
        dateEt= findViewById(R.id.date);
        timeEt = findViewById(R.id.time);
        submitButton = findViewById(R.id.nutritionalTrackingButton);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);


        TextView homeButton = findViewById(R.id.btnHome);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // MainActivity
                Intent intent = new Intent(NutritionTrackingForm.this, MainActivity.class).putExtra("name", name);
                startActivity(intent);
            }
        });

        // LogoutBtn
        TextView logoutButton = findViewById(R.id.btnLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LoginActivity
                Intent intent = new Intent(NutritionTrackingForm.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Firebase database
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("caloriesTracking");

        // DatePickerDialog
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        // TimePickerDialog
        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateProtein() | !validateCarbohydrates() | !validateFat() | !validateDate() | !validateTime()) {
                    Toast.makeText(NutritionTrackingForm.this, "Wrong!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NutritionTrackingForm.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                    } else {

                        double protein = Double.parseDouble(proteinIntake.getText().toString());
                        double carbohydrates = Double.parseDouble(carbohydratesIntake.getText().toString());
                        double fat = Double.parseDouble(fatIntake.getText().toString());

                        // calories calculations
                        double calories = (protein * 4) + (carbohydrates * 4) + (fat * 9);

                        totalCaloriesHolder = String.format("%.2f", calories);

                        // Save the data to the Firebase Realtime Database
                        saveDataToDatabase(name, dateHolder, timeHolder, totalCaloriesHolder);

                        Toast.makeText(NutritionTrackingForm.this, "You have added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NutritionTrackingForm.this, NutritionalTracking.class).putExtra("name", name);
                        startActivity(intent);
                    }
                }
            }
        });
    }//on create



    //date to choose
    private void showDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, day);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
                        dateEt.setText(dateFormat.format(selectedDate.getTime()));

                        dateHolder =dateFormat.format(selectedDate.getTime()).toString();

                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    // Time to choose
    private void showTimePicker() {
        final Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hour);
                        selectedTime.set(Calendar.MINUTE, minute);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        timeEt.setText(timeFormat.format(selectedTime.getTime()));

                        timeHolder = timeFormat.format(selectedTime.getTime()).toString();
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    //database
    private void saveDataToDatabase(String username, String date, String time, String totalcalories) {
        // New entry "nutrition" node of the database
        DatabaseReference nutritionRef = reference.child(username).push();

        //  nutrition information
        NutritionTrackingData nutrition = new NutritionTrackingData(username, date, time, totalcalories);

        // nutrition object is to save in the database
        nutritionRef.setValue(nutrition);
    }
    public Boolean validateProtein(){
        String val = proteinIntake.getText().toString();
        if (val.isEmpty()){
            proteinIntake.setError("Cannot be empty");
            return false;
        } else {
            proteinIntake.setError(null);
            return true;
        }
    }
    public Boolean validateCarbohydrates(){
        String val = carbohydratesIntake.getText().toString();
        if (val.isEmpty()){
            carbohydratesIntake.setError("Cannot be empty");
            return false;
        } else {
            carbohydratesIntake.setError(null);
            return true;
        }
    }
    public Boolean validateFat(){
        String val = fatIntake.getText().toString();
        if (val.isEmpty()){
            fatIntake.setError("Cannot be empty");
            return false;
        } else {
            fatIntake.setError(null);
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

