package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPage extends AppCompatActivity {

    // Initiating Front End Components
    EditText EmailBox, PassBox, ConfirmPassBox;
    Button SubmitButton;

    // Initiating Firebase Auth
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // Linking Front End Components to Corresponding IDs
        EmailBox = findViewById(R.id.EmailInputSignupPage);
        PassBox = findViewById(R.id.PasswordInputSignUpPage);
        ConfirmPassBox = findViewById(R.id.ConfirmPasswordInputSignUpPage);
        SubmitButton = findViewById(R.id.SubmitButtonSignUpPage);

        // Submit Button On Click Listener
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting all the Values from Front End
                String email = EmailBox.getText().toString();
                String pass = PassBox.getText().toString();
                String confirmPass = ConfirmPassBox.getText().toString();

                if (pass.equals(confirmPass))
                    signUpFirebase(email, pass); // Sign Up Function Call
                else
                    Toast.makeText(SignUpPage.this, "Password != Confirm Password",
                            Toast.LENGTH_SHORT).show();
            }
        });


    }

    // SignUp Function
    public void signUpFirebase(String userEmail, String userPass) {
        // Firebase Function to Sign Up
        auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign Up success
                            Toast.makeText(SignUpPage.this, "Account Created",
                                    Toast.LENGTH_SHORT).show();
                            // Sign In
                            signinFirebase(userEmail, userPass);
                        } else {
                            // Sign in fails
                            Toast.makeText(SignUpPage.this, "Account Not Created",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Sign In Function
    public void signinFirebase(String userEmail, String userPass) {
        auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign In success, Redirect to Home Page
                            Intent i = new Intent(SignUpPage.this, SignUpInfoInput.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If Sign In fails
                            Toast.makeText(SignUpPage.this, "Invalid Email/Password",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}