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
import com.parthtrap.donationapp.HelperClasses.NGOInfoHelper;
import com.parthtrap.donationapp.MainActivity;
import com.parthtrap.donationapp.R;

public class NGOSignUpPage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("NGO_SIGN_UP_PAGE_LOGS");

	// Initiating Front End Components
	EditText EmailBox, PassBox, ConfirmPassBox;
	Button SubmitButton;
	TextView LoginRedirect, NGOSignupRedirect;

	// Initiating Firebase Auth
	FirebaseAuth auth = FirebaseAuth.getInstance();

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ngosign_up_page);

		Log.d(LOG.getPAGES_LOG(), "NGO Sign Up Page Created");
		Log.d(LOG.getLOCAL_LOG(), "NGO Sign Up Page Created");

		// Linking Front End Components to Corresponding IDs
		EmailBox = findViewById(R.id.NGOEmailInputSignupPage);
		PassBox = findViewById(R.id.NGOPasswordInputSignUpPage);
		ConfirmPassBox = findViewById(R.id.NGOConfirmPasswordInputSignUpPage);
		SubmitButton = findViewById(R.id.NGOSubmitButtonSignUpPage);
		LoginRedirect = findViewById(R.id.NGOLogInPageRedirectSignUpPage);
		NGOSignupRedirect = findViewById(R.id.UserSignUpRedirectSignUpPage);

		// Submit Button On Click Listener
		SubmitButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){

				Log.d(LOG.getLOCAL_LOG(), "Sign Up Button Pressed");

				// Getting all the Values from Front End
				String email = EmailBox.getText().toString().trim();
				String pass = PassBox.getText().toString().trim();
				String confirmPass = ConfirmPassBox.getText().toString().trim();

				if(pass.equals(confirmPass)){

					// Check if both fields are empty or not
					if(!email.isEmpty() && !pass.isEmpty()){
						// Sign Up Function Call
						Log.d(LOG.getLOCAL_LOG(), "Sign Up Function Called");
						signUpFirebase(email, pass);
					}else{
						// If Null... Show toast Message
						Log.d(LOG.getLOCAL_LOG(), "Did not fill all parameters");
						Toast.makeText(NGOSignUpPage.this, "Please Fill All Parameters", Toast.LENGTH_SHORT).show();
					}
				}else{
					// Password != Confirm Password Toast Message
					Log.d(LOG.getLOCAL_LOG(), "Password is not equal to confirm Password");
					Toast.makeText(NGOSignUpPage.this, "Password != Confirm Password", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// OnClick Log In text
		LoginRedirect.setOnClickListener(new View.OnClickListener(){
			// Redirect to NGO Login Page
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "NGO Login Redirect Clicked");
				Intent i = new Intent(NGOSignUpPage.this, NGOLoginPage.class);
				startActivity(i);
				finish();
			}
		});

		// OnClick User SignUp text
		NGOSignupRedirect.setOnClickListener(new View.OnClickListener(){
			// Redirect to User Sign Up Page
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "User SignUp Redirect Clicked");
				Intent i = new Intent(NGOSignUpPage.this, SignUpPage.class);
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
					Toast.makeText(NGOSignUpPage.this, "Account Created", Toast.LENGTH_SHORT).show();
					// Sign In
					signinFirebase(userEmail, userPass);
				}else{
					// Sign Up fails
					Log.d(LOG.getLOCAL_LOG(), "Sign Up Failed");
					Toast.makeText(NGOSignUpPage.this, "Account Not Created", Toast.LENGTH_SHORT).show();
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
					NGOInfoHelper newNGO = new NGOInfoHelper();
					newNGO.setName("null");
					FirebaseFirestore.getInstance().collection("NGOProfiles").document(FirebaseAuth.getInstance().getUid()).set(newNGO);
					Intent i = new Intent(NGOSignUpPage.this, NGOSignUpInfoInputPage.class);
					startActivity(i);
					Log.d(LOG.getLOCAL_LOG(), "Sign In Successful. Redirect to NGO Home Page");
					finish();
				}else{
					// If Sign In fails
					Log.d(LOG.getLOCAL_LOG(), "Invalid Credentials");
					Toast.makeText(NGOSignUpPage.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "NGO Sign Up Page Destroyed");
		super.onDestroy();
	}

	// On Pressing Back on the Login Page
	@Override public void onBackPressed(){
		// Redirect to Main Activity
		Intent i = new Intent(NGOSignUpPage.this, MainActivity.class);
		startActivity(i);
		Log.d(LOG.getLOCAL_LOG(), "Back Pressed. Redirect to Main Activity");
		finish();
	}

}