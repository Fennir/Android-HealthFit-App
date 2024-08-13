package com.example.healthfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView homeTextView, logoutTextView;
    private Button dailyRoutineBtn, nutritionTrackBtn, waterTrackBtn, sleepTrackBtn, weightTrackBtn, goalSetBtn, personalizedCoachBtn;

    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        onclickListerner();

    } //oncreate

public void onclickListerner(){


    //variable declaration
    homeTextView = (TextView) findViewById(R.id.btnHome);
    logoutTextView = (TextView) findViewById(R.id.btnLogout);

    dailyRoutineBtn = (Button) findViewById(R.id.dailyExerciseButton);
    nutritionTrackBtn = (Button) findViewById(R.id.nutritionalTrackingButton);
    waterTrackBtn = (Button) findViewById(R.id.waterTrackingButton);
    sleepTrackBtn = (Button) findViewById(R.id.sleepTrackingButton);
    weightTrackBtn = (Button) findViewById(R.id.weightTrackingButton);
    goalSetBtn = (Button) findViewById(R.id.goalSettingButton);
    personalizedCoachBtn = (Button) findViewById(R.id.personalizedCoachingButton);

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

    dailyRoutineBtn.setOnClickListener(v -> {
        openDailyRoutine(name);
    });

    nutritionTrackBtn.setOnClickListener(v -> {
        openNutritionTrack(name);
    });

    waterTrackBtn.setOnClickListener(v -> {
        openWaterTrack(name);
    });

    sleepTrackBtn.setOnClickListener(v -> {
        openSleepTrack(name);
    });

    weightTrackBtn.setOnClickListener(v -> {
        openWeightTrack(name);
    });

    goalSetBtn.setOnClickListener(v -> {
        openGoalSet(name);
    });

    personalizedCoachBtn.setOnClickListener(v -> {
        openPersonalizedCoach(name);
    });
}

    public void openHome(String value){
        startActivity(new Intent(MainActivity.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra("name", value));
    }
    public void openDailyRoutine(String value){ startActivity(new Intent(MainActivity.this, DailyExercise.class).putExtra("name", value)); }
    public void openNutritionTrack(String value){ startActivity(new Intent(MainActivity.this, NutritionalTracking.class).putExtra("name", value)); }
    public void openWaterTrack(String value){ startActivity(new Intent(MainActivity.this, WaterTracking.class).putExtra("name", value)); }
    public void openSleepTrack(String value){ startActivity(new Intent(MainActivity.this, SleepTracking.class).putExtra("name", value)); }
    public void openWeightTrack(String value){ startActivity(new Intent(MainActivity.this, WeightTracking.class).putExtra("name", value)); }
    public void openGoalSet(String value){ startActivity(new Intent(MainActivity.this, GoalSetting.class).putExtra("name", value)); }
    public void openPersonalizedCoach(String value){ startActivity(new Intent(MainActivity.this, PersonalizedCoaching.class).putExtra("name", value)); }


} //main class