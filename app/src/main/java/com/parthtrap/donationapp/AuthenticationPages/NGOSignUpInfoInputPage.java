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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.parthtrap.donationapp.HelperClasses.NGOInfoHelper;
import com.parthtrap.donationapp.NGOPortal.HomePage;
import com.parthtrap.donationapp.R;

public class NGOSignUpInfoInputPage extends AppCompatActivity{

	// Defining Front End Components
	TextView EmailDisplay;
	EditText NameBox, PhoneBox, AddressBox;
	Button Submit;

	// Connecting to Firebase Authentication and Database. Also getting Collection References
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	DocumentReference userCollection = db.collection("NGOProfiles").document(user.getUid());

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ngosign_up_info_input_page);

		// Creating UserHelper object to store user data in one place
		NGOInfoHelper newNGO = new NGOInfoHelper();

		// Connecting backend to frontend using IDs
		EmailDisplay = findViewById(R.id.NGOEmailDisplaySignUpInfoInput);
		NameBox = findViewById(R.id.NGONameInputSignUpInfoInput);
		PhoneBox = findViewById(R.id.NGOPhoneInputSignUpInfoInput);
		AddressBox = findViewById(R.id.NGOAddressInputSignUpInfoInput);
		Submit = findViewById(R.id.SubmitNGOSignUpInfoInput);

		// Display Email ID from Firebase Authentication
		EmailDisplay.setText(user.getEmail());

		// Submit Button onClick Listener
		Submit.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){

				// Setting info into the object.
				newNGO.setName(NameBox.getText().toString().trim());
				newNGO.setPhoneNumber(PhoneBox.getText().toString().trim());
				newNGO.setAddress(AddressBox.getText().toString().trim());

				// Check if info is entered or not
				if(!newNGO.getName().isEmpty() && !newNGO.getPhoneNumber().isEmpty() && !newNGO.getAddress().isEmpty()){

					newNGO.setEmailId(user.getEmail());

					// Set Data to Database
					userCollection.set(newNGO).addOnSuccessListener(new OnSuccessListener<Void>(){
						// Redirect to Map Info Entering page
						@Override public void onSuccess(Void unused){
							Intent i = new Intent(NGOSignUpInfoInputPage.this, HomePage.class);
							startActivity(i);
							finish();
						}
					}).addOnFailureListener(new OnFailureListener(){
						// If Failed... Show a Message
						@Override public void onFailure(@NonNull Exception e){
							Toast.makeText(NGOSignUpInfoInputPage.this, e.toString(), Toast.LENGTH_SHORT).show();
						}
					});
				}
				// If All the Info is not filled
				else
					Toast.makeText(NGOSignUpInfoInputPage.this, "Fill All The Fields Above", Toast.LENGTH_SHORT).show();
			}
		});

	}
}