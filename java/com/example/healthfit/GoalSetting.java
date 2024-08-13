package com.example.healthfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GoalSetting extends AppCompatActivity {

    TextView username, homeTextView, logoutTextView;
    Button addBtn;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    GoalSettingData goalData;

    String nameHolder;

    //data holder
    TextView dateVt;
    TextView startDate, endDate, fitnessGoal, progressNotes;
    String strStartDate,strEndDate, strFitnessGoal, strProgressNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        String formattedDate = df.format(c);

        dateVt = (TextView) findViewById(R.id.tvCurrentDate);//current date
        dateVt.setText(formattedDate);

        viewList(formattedDate);

    }//on creaate

    public void viewList(String dateValue){

        addBtn = (Button) findViewById(R.id.btnUpdateGoal);
        goalData = new GoalSettingData();
        listView = (ListView) findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();

        //settext holder
        startDate = (TextView) findViewById(R.id.tvStartDate);
        endDate = (TextView) findViewById(R.id.tvEndDate);
        progressNotes = (TextView) findViewById(R.id.tvNotes);
        fitnessGoal = (TextView) findViewById(R.id.tvFitnessGoal);

        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);


        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        nameHolder = name;

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        dbRef = database.getReference("goalSetting").child(name);

        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    strStartDate = snapshot.child("startDate").getValue(String.class);
                    strEndDate = snapshot.child("endDate").getValue(String.class);
                    strFitnessGoal = snapshot.child("fitnessGoal").getValue(String.class);
                    strProgressNotes = snapshot.child("noteProgress").getValue(String.class);

                    GoalSettingData goalData = new GoalSettingData(strStartDate, strEndDate, strFitnessGoal, strProgressNotes);

                    // Update UI with the retrieved data
                    startDate.setText(goalData.getStartDate());
                    endDate.setText(goalData.getEndDate());
                    fitnessGoal.setText(goalData.getFitnessGoal());
                    progressNotes.setText(goalData.getNoteProgress());

                }
                else {
                    progressNotes.setText("You haven't record your goals yet..");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addBtn.setOnClickListener(v -> {
            openGoalSettingForm(name, strStartDate, strEndDate, strFitnessGoal, strProgressNotes);
        });

    } //viewList

    public void openHome(String value){
        startActivity(new Intent(GoalSetting.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(GoalSetting.this, LoginActivity.class).putExtra("name", value));
    }

    public void openGoalSettingForm(String name, String strStartDate, String strEndDate, String strFitnessGoal, String strProgressNotes){
        startActivity(new Intent(GoalSetting.this, GoalSettingForm.class).putExtra("name", name).putExtra("startDate", strStartDate).putExtra("endDate", strEndDate).putExtra("fitnessGoal", strFitnessGoal).putExtra("note", strProgressNotes)); }

    public void onBackPressed() {
        Intent intent = new Intent(GoalSetting.this, MainActivity.class).putExtra("name", nameHolder);
        startActivity(intent);
    }
}