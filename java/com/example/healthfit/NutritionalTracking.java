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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NutritionalTracking extends AppCompatActivity {


    TextView username, homeTextView, logoutTextView,result;
    Button addBtn, deleteBtn, refreshBtn;
    String nameHolder;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    private List<NutritionTrackingData> dataItems;
    private NutritionAdapter adapter;
    NutritionTrackingData NutritionData;
    final Calendar myCalendar= Calendar.getInstance();
    EditText dateVt;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nutrition_tracking);

    Date c = Calendar.getInstance().getTime();
    System.out.println("Current time => " + c);

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    String formattedDate = df.format(c);

    dateVt = findViewById(R.id.currentDate);
    refreshBtn = findViewById(R.id.refreshButton);

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
            new DatePickerDialog(NutritionalTracking.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

        //variable declaration
        TextView msg = (TextView) findViewById(R.id.message);
        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);
        addBtn = (Button) findViewById(R.id.addCaloriesBtn);
        deleteBtn = (Button) findViewById(R.id.deleteCaloriesBtn);
        NutritionData = new NutritionTrackingData();
        listView = (ListView) findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        result = findViewById(R.id.tvcalorie);

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String date = dateValue;

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);

        nameHolder = name;

        //process
        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        listView = findViewById(R.id.listView);

        dbRef = database.getReference("caloriesTracking").child(name);

        Query query = dbRef.orderByChild("date").equalTo(date);

        dataItems = new ArrayList<>();

        adapter = new NutritionAdapter(this, dataItems);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    dataItems.clear();
                    String getTotal;
                    Double totalAdded = 0.0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        NutritionData = ds.getValue(NutritionTrackingData.class);
                        dataItems.add(NutritionData);

                        getTotal = NutritionData.getTotalCalories();
                        totalAdded = Double.parseDouble(getTotal) + totalAdded;
                    }
                    adapter.notifyDataSetChanged();
                    msg.setText("Total calories is : "+totalAdded+"(kcals)");
                }
                else{
                    msg.setText("You have yet record your calories intake for today\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        listView.setAdapter(adapter);

        addBtn.setOnClickListener(v -> {
            openNutritionalTrack(name);
        });

        deleteBtn.setOnClickListener(v -> {
            deleteCaloriesTrack(name);
        });

    } //viewList

    public void openHome(String value){
        startActivity(new Intent(NutritionalTracking.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(NutritionalTracking.this, LoginActivity.class).putExtra("name", value));
    }
    public void openNutritionalTrack(String value){ startActivity(new Intent(NutritionalTracking.this, NutritionTrackingForm.class).putExtra("name", value));}

    public void deleteCaloriesTrack(String name){
        dbRef = database.getReference("caloriesTracking").child(name);

        Query query = dbRef.orderByChild("date").equalTo(dateVt.getText().toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Iterate through all the child nodes and find the latest data item
                    String latestDataItemKey = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NutritionTrackingData dataItem = snapshot.getValue(NutritionTrackingData.class);
                        if (latestDataItemKey == null || dataItem.getTime().compareTo(latestDataItemKey) > 0) {
                            latestDataItemKey = snapshot.getKey();
                        } else {
                            Toast.makeText(NutritionalTracking.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // Delete the latest data item
                    if (latestDataItemKey != null) {
                        dbRef.child(latestDataItemKey).removeValue();
                        Toast.makeText(NutritionalTracking.this, "You have deleted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NutritionalTracking.this, NutritionalTracking.class).putExtra("name", name);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(NutritionalTracking.this, "There is nothing to delete!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateVt.setText(dateFormat.format(myCalendar.getTime()));
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NutritionalTracking.this, MainActivity.class).putExtra("name", nameHolder);
        startActivity(intent);
    }


}