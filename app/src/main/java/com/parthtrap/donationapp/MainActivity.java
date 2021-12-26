package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.UserPortal.HomePage;

public class MainActivity extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("MAIN_ACTIVITY_PAGE_LOGS");

	// Initializing FrontEnd Components
	Button LogInPage, SignUpPage;

	// Initiating Firebase Auth
	FirebaseAuth auth = FirebaseAuth.getInstance();

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(LOG.getPAGES_LOG(), "Main Activity Page Created");
		Log.d(LOG.getLOCAL_LOG(), "Main Activity Page Created");

		// Linking Buttons to Corresponding IDs
		LogInPage = findViewById(R.id.LogInMainAct);
		SignUpPage = findViewById(R.id.SignUpMainAct);

		// Sign Up Page On Click Listener
		SignUpPage.setOnClickListener(v -> {
			Log.d(LOG.getLOCAL_LOG(), "Sign up Page Redirect");
			Intent i = new Intent(MainActivity.this, com.parthtrap.donationapp.AuthenticationPages.SignUpPage.class);
			startActivity(i);
			finish();
		});

		// Log In Page On Click Listener
		LogInPage.setOnClickListener(v -> {
			Log.d(LOG.getLOCAL_LOG(), "Log In Page Redirect");
			Intent i = new Intent(MainActivity.this, com.parthtrap.donationapp.AuthenticationPages.LogInPage.class);
			startActivity(i);
			finish();
		});
	}

	@Override protected void onStart(){
		super.onStart();
		// Getting the user from Firebase Auth
		FirebaseUser user = auth.getCurrentUser();
		if(user != null){
			Log.d(LOG.getLOCAL_LOG(), "User is Not Null... Redirecting to Home Page");
			Intent i = new Intent(MainActivity.this, HomePage.class);
			startActivity(i);
			finish();
		}
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "Main Activity Page Destroyed");
		super.onDestroy();
	}

	@Override public void onBackPressed(){
		Log.d(LOG.getPAGES_LOG(), "Main Activity Back Press... Destroy Everything");
		finishAffinity();
		super.onBackPressed();
	}
}