/*
Login Page for Users in App.
 */

package com.parthtrap.donationapp.AuthenticationPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.parthtrap.donationapp.MainActivity;
import com.parthtrap.donationapp.R;
import com.parthtrap.donationapp.UserPortal.HomePage;

public class LogInPage extends AppCompatActivity{

	FirebaseAuth mAuth = FirebaseAuth.getInstance(); // FireBase Authentication

	// Defining Front End Components
	Button loginButton;
	EditText EmailBox, PassBox;
	TextView SignUpRedirectText, NGORedirectText;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in_page);

		// Linking Frontend to Backend via ID
		loginButton = findViewById(R.id.LogInButtonLogInPage);
		EmailBox = findViewById(R.id.EmailInputLoginPage);
		PassBox = findViewById(R.id.PasswordInputLoginPage);
		SignUpRedirectText = findViewById(R.id.CreateNewAccountLoginPage);
		NGORedirectText = findViewById(R.id.NotUserNGO);

		// OnClick for SignUp Link... take to Signup Page
		SignUpRedirectText.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Intent i = new Intent(LogInPage.this, SignUpPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick NGO Login Button... Take to NGO Login Page
		NGORedirectText.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Intent i = new Intent(LogInPage.this, NGOLoginPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick for Login Button
		loginButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){

				// Store values in Temporary Variables
				String email = EmailBox.getText().toString();
				String pass = PassBox.getText().toString();

				if(!email.isEmpty() && !pass.isEmpty())    // Check That they are not null
					signinFirebase(email, pass);
				else                                        // If Null... Show toast Message
					Toast.makeText(LogInPage.this, "Please Fill All Parameters", Toast.LENGTH_SHORT).show();
			}
		});

	}

	// Sign In Function
	public void signinFirebase(String userEmail, String userPass){
		mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
			@Override public void onComplete(@NonNull Task<AuthResult> task){
				if(task.isSuccessful()){
					// Sign in success... redirect to Home Page
					Intent i = new Intent(LogInPage.this, HomePage.class);
					startActivity(i);
					finish();
				}else{
					// Sign in failed... display toast message to the user.
					Toast.makeText(LogInPage.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// On Pressing Back on the Login Page... take to Main Activity
	@Override public void onBackPressed(){
		Intent i = new Intent(LogInPage.this, MainActivity.class);
		startActivity(i);
		finish();
	}
}