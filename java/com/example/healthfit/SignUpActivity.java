package com.example.healthfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);

        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateUsername() | !validatePassword() | !validateEmail() | !validateName()){
                    Toast.makeText(SignUpActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                }else {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    String name = signupName.getText().toString();
                    String email = signupEmail.getText().toString();
                    String username = signupUsername.getText().toString();
                    String password = signupPassword.getText().toString();

                    UserData helperClass = new UserData(name, email, username, password);
                    reference.child(name).setValue(helperClass);

                    Toast.makeText(SignUpActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        }

    public Boolean validateName(){
        String val = signupName.getText().toString();
        if (val.isEmpty()){
            signupName.setError("Cannot be empty");
            return false;
        } else {
            signupName.setError(null);
            return true;
        }
    }

    public Boolean validateUsername(){
        String val = signupUsername.getText().toString();
        if (val.isEmpty()){
            signupUsername.setError("Cannot be empty");
            return false;
        } else {
            signupUsername.setError(null);
            return true;
        }
    }

    public Boolean validateEmail(){
        String val = signupEmail.getText().toString();
        if (val.isEmpty()){
            signupEmail.setError("Cannot be empty");
            return false;
        } else {
            signupEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = signupPassword.getText().toString();
        if (val.isEmpty()){
            signupPassword.setError("Cannot be empty");
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }
    }

}