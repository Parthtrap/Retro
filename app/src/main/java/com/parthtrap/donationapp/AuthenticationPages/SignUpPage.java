/*
New User SignUp Page
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.HelperClasses.UserInfoHelper;
import com.parthtrap.donationapp.MainActivity;
import com.parthtrap.donationapp.R;

public class SignUpPage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("USER_SIGN_UP_PAGE_LOGS");

	// Initiating Front End Components
	EditText EmailBox, PassBox, ConfirmPassBox;
	Button SubmitButton;
	TextView LoginRedirect, NGOSignupRedirect;

	// Initiating Firebase Auth
	FirebaseAuth auth = FirebaseAuth.getInstance();

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_page);

		Log.d(LOG.getPAGES_LOG(), "User Sign Up Page Created");
		Log.d(LOG.getLOCAL_LOG(), "User Sign Up Page Created");

		// Linking Front End Components to Corresponding IDs
		EmailBox = findViewById(R.id.EmailInputSignupPage);
		PassBox = findViewById(R.id.PasswordInputSignUpPage);
		ConfirmPassBox = findViewById(R.id.ConfirmPasswordInputSignUpPage);
		SubmitButton = findViewById(R.id.SubmitButtonSignUpPage);
		LoginRedirect = findViewById(R.id.LogInPageRedirectSignUpPage);
		NGOSignupRedirect = findViewById(R.id.NGOSignUpRedirectSignUpPage);

		// Submit Button On Click Listener
		SubmitButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){

				Log.d(LOG.getLOCAL_LOG(), "Sign Up Button Pressed");

				// Getting all the Values from Front End
				String email = EmailBox.getText().toString();
				String pass = PassBox.getText().toString();
				String confirmPass = ConfirmPassBox.getText().toString();

				if(pass.equals(confirmPass)){

					// Check if both fields are empty or not
					if(!email.isEmpty() && !pass.isEmpty()){
						// Sign Up Function Call
						Log.d(LOG.getLOCAL_LOG(), "Sign Up Function Called");
						signUpFirebase(email, pass);
					}else{
						// If Null... Show toast Message
						Log.d(LOG.getLOCAL_LOG(), "Did not fill all parameters");
						Toast.makeText(SignUpPage.this, "Please Fill All Parameters", Toast.LENGTH_SHORT).show();
					}
				}else{
					// Password != Confirm Password Toast Message
					Log.d(LOG.getLOCAL_LOG(), "Password is not equal to confirm Password");
					Toast.makeText(SignUpPage.this, "Password != Confirm Password", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// OnClick Log In text
		LoginRedirect.setOnClickListener(new View.OnClickListener(){
			// Redirect to User Login Page
			@Override public void onClick(View v){
				Intent i = new Intent(SignUpPage.this, LogInPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick NGO SignUp text
		NGOSignupRedirect.setOnClickListener(new View.OnClickListener(){
			// Redirect to NGO Sign Up Page
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "NGO SignUp Redirect Clicked");
				Intent i = new Intent(SignUpPage.this, NGOSignUpPage.class);
				startActivity(i);
				finish();
			}
		});

	}

	// SignUp Function
	public void signUpFirebase(String userEmail, String userPass){
		// Firebase Function to Sign Up
		auth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
			@Override public void onComplete(@NonNull Task<AuthResult> task){
				Log.d(LOG.getDATA_LOG(), "Firebase Sign Up Authentication Function Success");
				if(task.isSuccessful()){
					// Sign Up success
					Log.d(LOG.getLOCAL_LOG(), "Sign Up Successful. Calling Log In Function");
					Toast.makeText(SignUpPage.this, "Account Created", Toast.LENGTH_SHORT).show();
					// Sign In
					signinFirebase(userEmail, userPass);
				}else{
					// Sign in fails
					Log.d(LOG.getLOCAL_LOG(), "Sign Up Failed");
					Toast.makeText(SignUpPage.this, "Account Not Created", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	// Sign In Function
	public void signinFirebase(String userEmail, String userPass){
		// Firebase SignIn Function
		auth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
			@Override public void onComplete(@NonNull Task<AuthResult> task){
				Log.d(LOG.getDATA_LOG(), "Firebase Sign In Authentication Function Success");
				// If SignIn is successful
				if(task.isSuccessful()){
					// Redirect to NGO Home Page
					// Make a Placeholder Profile which will later be updated in Info Input page
					UserInfoHelper newuser = new UserInfoHelper();
					newuser.setName("null");
					FirebaseFirestore.getInstance().collection("UserProfiles").document(FirebaseAuth.getInstance().getUid()).set(newuser);
					Intent i = new Intent(SignUpPage.this, SignUpInfoInputPage.class);
					startActivity(i);
					Log.d(LOG.getLOCAL_LOG(), "Sign In Successful. Redirect to NGO Home Page");
					finish();
				}else{
					// If Sign In fails
					Log.d(LOG.getLOCAL_LOG(), "Invalid Credentials");
					Toast.makeText(SignUpPage.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "User Sign Up Page Destroyed");
		super.onDestroy();
	}

	// On Pressing Back on the Login Page
	@Override public void onBackPressed(){
		// Redirect to Main Activity
		Intent i = new Intent(SignUpPage.this, MainActivity.class);
		startActivity(i);
		Log.d(LOG.getLOCAL_LOG(), "Back Pressed. Redirect to Main Activity");
		finish();
	}
}