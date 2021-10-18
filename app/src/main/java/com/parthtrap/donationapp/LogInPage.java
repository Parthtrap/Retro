package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInPage extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button loginbutton;
    TextView EmailBox, PassBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        loginbutton = findViewById(R.id.LogInButtonLogInPage);
        EmailBox = findViewById(R.id.EmailInputLoginPage);
        PassBox = findViewById(R.id.PasswordInputLoginPage);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = EmailBox.getText().toString();
                String pass = PassBox.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty())
                    signinFirebase(email, pass);
                else
                    Toast.makeText(LogInPage.this, "Fill All Parameters", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void signinFirebase(String userEmail, String userPass) {
        mAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(LogInPage.this, HomePage.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogInPage.this, "Invalid Email/Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}