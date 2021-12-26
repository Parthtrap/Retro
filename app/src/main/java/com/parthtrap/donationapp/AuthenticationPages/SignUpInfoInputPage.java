/*
User Input his info after Signing Up for the website.

*/

package com.parthtrap.donationapp.AuthenticationPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.parthtrap.donationapp.HelperClasses.UserInfoHelper;
import com.parthtrap.donationapp.R;

public class SignUpInfoInputPage extends AppCompatActivity{

	// Defining Front End Components
	TextView EmailDisplay;
	EditText NameBox, PhoneBox, AddressBox;
	Switch PhonePublic;
	Button Submit;

	// Connecting to Firebase Authentication and Database. Also getting Collection References
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	DocumentReference userCollection = db.collection("UserProfiles").document(user.getUid());

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_info_input);

		// Creating UserHelper object to store user data in one place
		UserInfoHelper newuser = new UserInfoHelper();

		// Connecting backend to frontend using IDs
		EmailDisplay = findViewById(R.id.EmailDisplaySignUpInfoInput);
		NameBox = findViewById(R.id.NameInputSignUpInfoInput);
		PhoneBox = findViewById(R.id.PhoneInputSignUpInfoInput);
		AddressBox = findViewById(R.id.AddressInputSignUpInfoInput);
		PhonePublic = findViewById(R.id.PublicPhoneSignUpInfoInput);
		Submit = findViewById(R.id.SubmitSignUpInfoInput);

		// Set if user wants his phone number to be visible to public or not.
		PhonePublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if(isChecked)
					newuser.setPublicPhone(true);
				else
					newuser.setPublicPhone(false);
			}
		});

		// Display Email ID from Firebase Authentication
		EmailDisplay.setText(user.getEmail());

		// Submit Button onClick Listener
		Submit.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){

				// Setting info into the object.
				newuser.setName(NameBox.getText().toString().trim());
				newuser.setPhoneNumber(PhoneBox.getText().toString().trim());
				newuser.setAddress(AddressBox.getText().toString().trim());

				// Check if info is entered or not
				if(!newuser.getName().isEmpty() && !newuser.getPhoneNumber().isEmpty() && !newuser.getAddress().isEmpty()){

					newuser.setEmailId(user.getEmail());

					// Set Data to Database
					userCollection.set(newuser).addOnSuccessListener(new OnSuccessListener<Void>(){
						// Redirect to Map Info Entering page
						@Override public void onSuccess(Void unused){
							Intent i = new Intent(SignUpInfoInputPage.this, SignUpMapInfoPage.class);
							startActivity(i);
							finish();
						}
					}).addOnFailureListener(new OnFailureListener(){
						// If Failed... Show a Message
						@Override public void onFailure(@NonNull Exception e){
							Toast.makeText(SignUpInfoInputPage.this, e.toString(), Toast.LENGTH_SHORT).show();
						}
					});
				}
				// If All the Info is not filled
				else
					Toast.makeText(SignUpInfoInputPage.this, "Fill All The Fields Above", Toast.LENGTH_SHORT).show();
			}
		});

	}
}