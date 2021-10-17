package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Initializing FrontEnd Components
    Button LogInPage, SignUpPage;

    // Initiating Firebase Auth
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking Buttons to Corresponding IDs
        LogInPage = findViewById(R.id.LogInMainAct);
        SignUpPage = findViewById(R.id.SignUpMainAct);

        // Sign Up Page On Click Listener
        SignUpPage.setOnClickListener(v -> {
                    Intent i = new Intent(MainActivity.this, SignUpPage.class);
                    startActivity(i);
                    finish();
                }
        );

        LogInPage.setOnClickListener(v -> {
                    Intent i = new Intent(MainActivity.this, LogInPage.class);
                    startActivity(i);
                    finish();
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Getting the user from Firebase Auth
        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
        {
            Intent i = new Intent(MainActivity.this, HomePage.class);
            startActivity(i);
            finish();
        }

    }
}