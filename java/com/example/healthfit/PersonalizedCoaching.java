package com.example.healthfit;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalizedCoaching extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextView username,homeTextView, logoutTextView;
    TextView firstname, lastname, emailUser, phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized_coaching);

        homeTextView = (TextView) findViewById(R.id.btnHome);
        logoutTextView = (TextView) findViewById(R.id.btnLogout);
        firstname = (EditText) findViewById(R.id.inputFirstName);
        lastname = (EditText) findViewById(R.id.inputLastName);
        emailUser = (EditText) findViewById(R.id.inputEmail);
        phonenumber = (EditText) findViewById(R.id.inputPhoneNumber);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //username
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");

        username = (TextView) findViewById(R.id.usernameTextView);
        username.setText(name);


        homeTextView.setOnClickListener(v -> {
            openHome(name);
        });

        logoutTextView.setOnClickListener(v -> {
            openLogin(name);
        });

        Button coachingButton = findViewById(R.id.coachingBtn);
        coachingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validateFname() | !validateLname() | !validateEmail() | !validatePhone()) {
                    Toast.makeText(PersonalizedCoaching.this, "Wrong!", Toast.LENGTH_SHORT).show();
                } else{submitForm(name);}
            }
        });
    }

    private void submitForm(String value) {

        String firstName = firstname.getText().toString();
        String lastName = lastname.getText().toString();
        String email = emailUser.getText().toString();
        String phoneNumber = phonenumber.getText().toString();

        // Save user data to Firebase Realtime Database
        saveUserData(firstName, lastName, email, phoneNumber);

        // Display "Coach will contact soon" toast message
        Toast.makeText(this, "Coach will contact soon", Toast.LENGTH_SHORT).show();

        // Delay for 10 seconds and then navigate to the location page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start Location activity
                Intent intent = new Intent(PersonalizedCoaching.this, Location.class);
                startActivity(intent);
                finish();
                redirectToHomePage(value);
            }
        }, 1250); //2.5 seconds

    }

    private void saveUserData(String firstName, String lastName, String email, String phoneNumber) {
        DatabaseReference usersRef = mDatabase.child("landingPage");
        String userId = usersRef.push().getKey();
        User user = new User(firstName, lastName, email, phoneNumber);
        usersRef.child(userId).setValue(user);
    }

    private static class User {
        public String firstName;
        public String lastName;
        public String email;
        public String phoneNumber;

        public User() {
        }

        public User(String firstName, String lastName, String email, String phoneNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }
    }

    public void openHome(String value){
        startActivity(new Intent(PersonalizedCoaching.this, MainActivity.class).putExtra("name", value));
    }
    public void openLogin(String value){
        startActivity(new Intent(PersonalizedCoaching.this, LoginActivity.class).putExtra("name", value));
    }

    private void redirectToHomePage(String value) {
        // Delay for 10 seconds and then navigate to the home page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start HomeActivity
                Intent intent = new Intent(PersonalizedCoaching.this, MainActivity.class).putExtra("name", value);
                startActivity(intent);
                finish();
            }
        }, 5000); // 10 seconds
    }
    public Boolean validateFname(){
        String val = firstname.getText().toString();
        if (val.isEmpty()){
            firstname.setError("Cannot be empty");
            return false;
        } else {
            firstname.setError(null);
            return true;
        }
    }
    public Boolean validateLname(){
        String val = lastname.getText().toString();
        if (val.isEmpty()){
            lastname.setError("Cannot be empty");
            return false;
        } else {
            lastname.setError(null);
            return true;
        }
    }
    public Boolean validateEmail(){
        String val = emailUser.getText().toString();
        if (val.isEmpty()){
            emailUser.setError("Cannot be empty");
            return false;
        } else {
            emailUser.setError(null);
            return true;
        }
    }
    public Boolean validatePhone(){
        String val = phonenumber.getText().toString();
        if (val.isEmpty()){
            phonenumber.setError("Cannot be empty");
            return false;
        } else {
            phonenumber.setError(null);
            return true;
        }
    }
}




