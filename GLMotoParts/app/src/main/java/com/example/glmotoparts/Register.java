package com.example.glmotoparts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnSignUp;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance("https://gl-motoparts-default-rtdb.firebaseio.com/").getReference("users");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    database = FirebaseDatabase.getInstance().getReference("users");
                    database.child(username).child("username").setValue(username);
                    database.child(username).child("email").setValue(email);
                    database.child(username).child("password").setValue(password);

                    Toast.makeText(getApplicationContext(), "Register Berhasil", Toast.LENGTH_SHORT).show();
                    Intent register = new Intent(getApplicationContext(), Login.class);
                    startActivity(register);
                }

                // Create a new user object
                User user = new User(username, email, password);

                // Store the user data in the Firebase Realtime Database
                database.child(email.replace(".", ",").replace("@", ",")).setValue(user);

                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void signUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = database.push().getKey();
        User user = new User(username, email, password);
        database.child(userId).setValue(user);

        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
        Intent Login = new Intent(Register.this, Login.class);
        startActivity(Login);
        finish();
    }
}