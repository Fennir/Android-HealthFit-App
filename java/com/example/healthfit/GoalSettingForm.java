package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import java.util.Locale;

public class GoalSettingForm extends AppCompatActivity {

    TextView username, goal, notes;

    TextView homeTextView, logoutTextView;
    Button goalSettingBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    final Calendar myCalendar= Calendar.getInstance();
    EditText start, end;
    boolean startEditTextHasFocus = true;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting_form);

        start = (EditText) findViewById(R.id.etStartDate);
        end = (EditText) findViewById(R.id.etEndDate);
        goal = (EditText) findViewById(R.id.etFitnessGoal);
        notes = (EditText) findViewById(R.id.notes);
        goalSettingBtn = (Button) findViewById(R.id.btnUpdateGoal);
        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String startDate = extras.getString("startDate");
        String endDate = extras.getString("endDate");
        String fitnessGoal = extras.getString("fitnessGoal");
        String note = extras.getString("note");

        start.setText(startDate);
        end.setText(endDate);
        goal.setText(fitnessGoal);
        notes.setText(note);

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                if (startEditTextHasFocus) {
                    updateLabelStart();
                } else {
                    updateLabelEnd();
                }
            }
        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(GoalSettingForm.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditTextHasFocus = false;
                new DatePickerDialog(GoalSettingForm.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        goalSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateStart() | !validateEnd() | !validateGoal() | !validateNote()) {
                    Toast.makeText(GoalSettingForm.this, "Wrong!", Toast.LENGTH_SHORT).show();
                } else {

                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("goalSetting").child(name);

                    String goalValue = goal.getText().toString();
                    String noteValue = notes.getText().toString();
                    String startValue = start.getText().toString();
                    String endValue = end.getText().toString();

                    GoalSettingData helperClass = new GoalSettingData(startValue, endValue, goalValue, noteValue);
                    reference.setValue(helperClass);

                    Toast.makeText(GoalSettingForm.this, "You have updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalSettingForm.this, GoalSetting.class).putExtra("name", name);
                    startActivity(intent);
                }
            }

                });
    }//oncreate

    private void updateLabelStart(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        start.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateLabelEnd(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        end.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void openHome(String value){
        startActivity(new Intent(GoalSettingForm.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(GoalSettingForm.this, LoginActivity.class).putExtra("name", value));
    }
    public Boolean validateStart(){
        String val = start.getText().toString();
        if (val.isEmpty()){
            start.setError("Cannot be empty");
            return false;
        } else {
            start.setError(null);
            return true;
        }
    }
    public Boolean validateEnd(){
        String val = end.getText().toString();
        if (val.isEmpty()){
            end.setError("Cannot be empty");
            return false;
        } else {
            end.setError(null);
            return true;
        }
    }
    public Boolean validateGoal(){
        String val = goal.getText().toString();
        if (val.isEmpty()){
            goal.setError("Cannot be empty");
            return false;
        } else {
            goal.setError(null);
            return true;
        }
    }
    public Boolean validateNote(){
        String val = notes.getText().toString();
        if (val.isEmpty()){
            notes.setError("Cannot be empty");
            return false;
        } else {
            notes.setError(null);
            return true;
        }
    }

}