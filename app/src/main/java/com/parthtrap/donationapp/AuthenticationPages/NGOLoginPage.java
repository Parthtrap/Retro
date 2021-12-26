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
import com.parthtrap.donationapp.NGOPortal.HomePage;
import com.parthtrap.donationapp.R;

public class NGOLoginPage extends AppCompatActivity{

	FirebaseAuth mAuth = FirebaseAuth.getInstance(); // FireBase Authentication

	// Defining Front End Components
	Button loginButton;
	EditText EmailBox, PassBox;
	TextView SignUpRedirectText, NGORedirectText;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ngologin_page);

		// Linking Frontend to Backend via ID
		loginButton = findViewById(R.id.LogInButtonNGOLogInPage);
		EmailBox = findViewById(R.id.NGOEmailInputLoginPage);
		PassBox = findViewById(R.id.NGOPasswordInputLoginPage);
		SignUpRedirectText = findViewById(R.id.CreateNewNGOAccountLoginPage);
		NGORedirectText = findViewById(R.id.NotNGOUser);

		// OnClick for SignUp Link... take to Signup Page
		SignUpRedirectText.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Intent i = new Intent(NGOLoginPage.this, NGOSignUpPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick User Login Button... Take to User Login Page
		NGORedirectText.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Intent i = new Intent(NGOLoginPage.this, LogInPage.class);
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
					Toast.makeText(NGOLoginPage.this, "Please Fill All Parameters", Toast.LENGTH_SHORT).show();
			}
		});

	}

	// Sign In Function
	public void signinFirebase(String userEmail, String userPass){
		mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
			@Override public void onComplete(@NonNull Task<AuthResult> task){
				if(task.isSuccessful()){
					// Sign in success... redirect to Home Page
					Intent i = new Intent(NGOLoginPage.this, HomePage.class);
					startActivity(i);
					finish();
				}else{
					// Sign in failed... display toast message to the user.
					Toast.makeText(NGOLoginPage.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// On Pressing Back on the Login Page... take to Main Activity
	@Override public void onBackPressed(){
		Intent i = new Intent(NGOLoginPage.this, MainActivity.class);
		startActivity(i);
		finish();
	}
}