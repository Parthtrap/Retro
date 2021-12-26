/*
Login Page for NGOs
 */

package com.parthtrap.donationapp.AuthenticationPages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.MainActivity;
import com.parthtrap.donationapp.NGOPortal.HomePage;
import com.parthtrap.donationapp.R;

public class NGOLoginPage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("NGO_LOG_IN_PAGE_LOGS");

	// FireBase Authentication Instance
	FirebaseAuth mAuth = FirebaseAuth.getInstance();

	// Defining Front End Components
	Button loginButton;
	EditText EmailBox, PassBox;
	TextView SignUpRedirectText, UserRedirectText;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ngologin_page);

		Log.d(LOG.getPAGES_LOG(), "NGO Login Page Created");
		Log.d(LOG.getLOCAL_LOG(), "NGO Login Page Created");

		// Linking Frontend to Backend via ID
		loginButton = findViewById(R.id.LogInButtonNGOLogInPage);
		EmailBox = findViewById(R.id.NGOEmailInputLoginPage);
		PassBox = findViewById(R.id.NGOPasswordInputLoginPage);
		SignUpRedirectText = findViewById(R.id.CreateNewNGOAccountLoginPage);
		UserRedirectText = findViewById(R.id.NotNGOUser);

		// OnClick for SignUp text
		SignUpRedirectText.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "NGO Sign Up Redirect Clicked");
				// Redirect to NGO SignUp Page
				Intent i = new Intent(NGOLoginPage.this, NGOSignUpPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick User Login text
		UserRedirectText.setOnClickListener(new View.OnClickListener(){
			// Redirect to User Login Page
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "User Login Redirect Clicked");
				Intent i = new Intent(NGOLoginPage.this, LogInPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick for Login Button
		loginButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Login Button Clicked");

				// Store Emails and Password in Variables
				String email = EmailBox.getText().toString().trim();
				String pass = PassBox.getText().toString().trim();

				// Check if they are null or not
				// If null then show message or else Login using Firebase

				if(!email.isEmpty() && !pass.isEmpty()){
					// if not Null... Sign In Function
					Log.d(LOG.getLOCAL_LOG(), "Sign in Function Called");
					signinFirebase(email, pass);
				}else{
					// If Null... Show toast Message
					Log.d(LOG.getLOCAL_LOG(), "Did not fill all parameters");
					Toast.makeText(NGOLoginPage.this, "Please Fill All Parameters", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	// Sign In Function
	public void signinFirebase(String userEmail, String userPass){
		// Firebase SignIn Function
		mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
			@Override public void onComplete(@NonNull Task<AuthResult> task){
				Log.d(LOG.getDATA_LOG(), "Firebase Sign In Authentication Function Success");
				// If SignIn is successful
				if(task.isSuccessful()){
					// Redirect to NGO Home Page
					Intent i = new Intent(NGOLoginPage.this, HomePage.class);
					startActivity(i);
					Log.d(LOG.getLOCAL_LOG(), "Sign In Successful. Redirect to NGO Home Page");
					finish();
				}
				// If SignIn Fails
				else{
					// Display toast message to the NGO.
					Log.d(LOG.getLOCAL_LOG(), "Invalid Credentials");
					Toast.makeText(NGOLoginPage.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "NGO Login Page Destroyed");
		super.onDestroy();
	}

	// On Pressing Back on the Login Page
	@Override public void onBackPressed(){
		// Redirect to Main Activity
		Intent i = new Intent(NGOLoginPage.this, MainActivity.class);
		startActivity(i);
		Log.d(LOG.getLOCAL_LOG(), "Back Pressed. Redirect to Main Activity");
		finish();
	}
}