package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DailyExercise extends AppCompatActivity {

    ImageView cardioNav, strengthNav, bodyNav;
    TextView username, homeTextView, logoutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_exercise);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);
        cardioNav = (ImageView) findViewById(R.id.cardioNav);
        strengthNav = (ImageView) findViewById(R.id.strengthNav);
        bodyNav = (ImageView) findViewById(R.id.bodyNav);

        cardioNav.setOnClickListener(v -> {
            openCardio(name);
        });

        strengthNav.setOnClickListener(v -> {
            openStrength(name);
        });

        bodyNav.setOnClickListener(v -> {
            openBody(name);
        });

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

    }
    public void openCardio(String value){ startActivity(new Intent(DailyExercise.this, DailyExerciseCardio.class).putExtra("name", value)); }
    public void openStrength(String value){ startActivity(new Intent(DailyExercise.this, DailyExerciseStrength.class).putExtra("name", value)); }
    public void openBody(String value){ startActivity(new Intent(DailyExercise.this, DailyExerciseBodyweight.class).putExtra("name", value)); }
    public void openHome(String value){
        startActivity(new Intent(DailyExercise.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(DailyExercise.this, LoginActivity.class).putExtra("name", value));
    }
}