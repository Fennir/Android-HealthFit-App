package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WeightTracking extends AppCompatActivity {

    Button updateWeightBtn;
    TextView weightStart, weightCurrent, weightGoal, progressNote, progressPercent, homeTextView, logoutTextView;
    String strWeightStart, strWeightCurrent, strWeightGoal, strProgressNote, nameHolder;
    double progressPercentage;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracking);

        viewList();
    }

    public void viewList(){

        updateWeightBtn = (Button) findViewById(R.id.updateWeightBtn);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //settext holder
        weightStart = (TextView) findViewById(R.id.viewStartingWeight);
        weightCurrent = (TextView) findViewById(R.id.viewCurrentWeight);
        weightGoal = (TextView) findViewById(R.id.viewWeightGoal);
        progressNote = (TextView) findViewById(R.id.progressNote);
        progressPercent = (TextView) findViewById(R.id.viewProgress);

        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        nameHolder = name;

        TextView username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        dbRef = database.getReference("weightTrack").child(name);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    strWeightStart = snapshot.child("weightStart").getValue(String.class);
                    strWeightCurrent = snapshot.child("weightCurrent").getValue(String.class);
                    strWeightGoal = snapshot.child("weightGoal").getValue(String.class);
                    strProgressNote = snapshot.child("progressNote").getValue(String.class);

                    WeightTrackingData weightData = new WeightTrackingData(strWeightStart, strWeightCurrent, strWeightGoal, strProgressNote);

                    // Update UI with the retrieved data
                    weightStart.setText(weightData.getWeightStart());
                    weightCurrent.setText(weightData.getWeightCurrent());
                    weightGoal.setText(weightData.getWeightGoal());
                    progressNote.setText(weightData.getProgressNote());

                    if (strWeightCurrent != null) {
                        double currentWeight = Double.parseDouble(strWeightCurrent);
                        double weightGoals = Double.parseDouble(strWeightGoal);
                        double difference = currentWeight - weightGoals;

                        if (difference > 0) {
                            // Weight gain formula
                            progressPercentage = (difference / weightGoals) * 100;
                            progressPercent.setText(String.format("%.2f", Math.abs(progressPercentage)) + "% (Weight Gain)");
                        } else if (difference < 0) {
                            // Weight loss formula
                            progressPercentage = 100 - ((Math.abs(difference) / weightGoals) * 100);
                            progressPercent.setText(String.format("%.2f", Math.abs(progressPercentage)) + "% (Towards Goal)");
                        } else {
                            progressPercentage = 0.0;
                            progressPercent.setText(String.format("%.2f", Math.abs(progressPercentage)) + "% (Reached)");
                        }
                    }
                }
                else{
                    progressNote.setText("You haven't update your weight yet");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateWeightBtn.setOnClickListener(v -> {
            openWeightForm(name,strWeightStart, strWeightCurrent, strWeightGoal, strProgressNote);
        });

    } //viewList

    public void openWeightForm(String name,String weightStart, String weightCurrent, String weightGoal, String progressNote){
        startActivity(new Intent(WeightTracking.this, WeightTrackingForm.class).putExtra("name", name).putExtra("weightStart", weightStart).putExtra("weightCurrent", weightCurrent).putExtra("weightGoal", weightGoal).putExtra("progressNote", progressNote)); }
    public void openHome(String value){
        startActivity(new Intent(WeightTracking.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(WeightTracking.this, LoginActivity.class).putExtra("name", value));
    }
    public void onBackPressed() {
        Intent intent = new Intent(WeightTracking.this, MainActivity.class).putExtra("name", nameHolder);
        startActivity(intent);
    }
}