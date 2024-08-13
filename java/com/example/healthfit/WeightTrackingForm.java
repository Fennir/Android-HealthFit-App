package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WeightTrackingForm extends AppCompatActivity {

    EditText weightStart, weightCurrent, weightGoal, progressNote;
    DatabaseReference reference;
    FirebaseDatabase database;
    TextView username, homeTextView, logoutTextView;;;
    Button weightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracking_form);

        weightStart = (EditText) findViewById(R.id.etStartingWeight);
        weightCurrent = (EditText) findViewById(R.id.etCurrentWeight);
        weightGoal = (EditText) findViewById(R.id.etWeightGoal);
        progressNote = (EditText) findViewById(R.id.etProgressNote);
        weightBtn = (Button) findViewById(R.id.weightBtn);

        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        weightStart.setText(extras.getString("weightStart"));
        weightCurrent.setText(extras.getString("weightCurrent"));
        weightGoal.setText(extras.getString("weightGoal"));
        progressNote.setText(extras.getString("progressNote"));

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        weightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateStart() | !validateCurrent() | !validateGoal() | !validateNote()) {
                    Toast.makeText(WeightTrackingForm.this, "Wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("weightTrack").child(name);

                    String weightStartValue = weightStart.getText().toString();
                    String weightCurrentValue = weightCurrent.getText().toString();
                    String weightGoalValue = weightGoal.getText().toString();
                    String progressNoteValue = progressNote.getText().toString();

                    WeightTrackingData helperClass = new WeightTrackingData(weightStartValue, weightCurrentValue, weightGoalValue, progressNoteValue);
                    reference.setValue(helperClass);

                    Toast.makeText(WeightTrackingForm.this, "You have updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WeightTrackingForm.this, WeightTracking.class).putExtra("name", name);
                    startActivity(intent);

                }
            }
        });

    }//on create

    public void openHome(String value){
        startActivity(new Intent(WeightTrackingForm.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(WeightTrackingForm.this, LoginActivity.class).putExtra("name", value));
    }

    public Boolean validateStart(){
        String val = weightStart.getText().toString();
        if (val.isEmpty()){
            weightStart.setError("Cannot be empty");
            return false;
        } else {
            weightStart.setError(null);
            return true;
        }
    }
    public Boolean validateCurrent(){
        String val = weightCurrent.getText().toString();
        if (val.isEmpty()){
            weightCurrent.setError("Cannot be empty");
            return false;
        } else {
            weightCurrent.setError(null);
            return true;
        }
    }
    public Boolean validateGoal(){
        String val = weightGoal.getText().toString();
        if (val.isEmpty()){
            weightGoal.setError("Cannot be empty");
            return false;
        } else {
            weightGoal.setError(null);
            return true;
        }
    }
    public Boolean validateNote(){
        String val = progressNote.getText().toString();
        if (val.isEmpty()){
            progressNote.setError("Cannot be empty");
            return false;
        } else {
            progressNote.setError(null);
            return true;
        }
    }

}