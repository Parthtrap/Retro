package com.parthtrap.donationapp.AuthenticationPages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.HelperFunctions.DonateItemLocationUpdate;
import com.parthtrap.donationapp.HelperFunctions.ExchangeItemLocationUpdate;
import com.parthtrap.donationapp.ProfileDisplay;
import com.parthtrap.donationapp.R;
import com.parthtrap.donationapp.UserPortal.HomePage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SignUpMapInfoPage extends AppCompatActivity implements OnMapReadyCallback{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("USER_SIGN_UP_MAP_PAGE_LOGS");

	// Defining Front End Components
	GoogleMap mapView;
	Button getButton;

	// Connecting to Firebase Authentication and Database. Also getting Collection References
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseFirestore fbfs = FirebaseFirestore.getInstance();

	// Declaring Variable for storing data
	boolean Filled = false;
	String Idfrom;
	Object OriginalLatitude, OriginalLongitude;
	double NewLatitude, NewLongitude;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_map_info_page);

		Log.d(LOG.getLOCAL_LOG(), "User Sign Up Map Page Created");
		Log.d(LOG.getPAGES_LOG(), "User Sign Up Map Page Created");

		// Linking the Submit Button to front End
		getButton = findViewById(R.id.LocationSubmitButton);
		getButton.setClickable(false);

		// Getting Data from Intent
		Intent intent = getIntent();
		Idfrom = intent.getStringExtra("idfrom"); // idfrom stores where the page is called from.

		// Store original Variables in a variable
		fbfs.collection("UserProfiles").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@Override public void onSuccess(DocumentSnapshot documentSnapshot){
				Log.d(LOG.getDATA_LOG(), "Original Co-ordinates Recieved and Stored");
				OriginalLatitude = documentSnapshot.get("latitude");
				OriginalLongitude = documentSnapshot.get("longitude");
			}
		});

		// Connect to Google Map
		SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.GoogleMapLocationInput);
		supportMapFragment.getMapAsync(this);

	}

	// When Map is Ready... following is ran
	@Override public void onMapReady(@NonNull @NotNull GoogleMap googleMap){
		Log.d(LOG.getLOCAL_LOG(), "Google Map Created and Ready to use");

		// Show Google Map on Screen
		mapView = googleMap;

		// Set onClick Listener on map to get the Co-ordinates
		mapView.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
			@Override public void onMapClick(LatLng latLng){
				// Store new co-ordinates in a variable and put them in a <String, float> hash map
				Log.d(LOG.getLOCAL_LOG(), "New Co-ordinates Set");
				NewLatitude = latLng.latitude;
				NewLongitude = latLng.longitude;
				Map<String, Object> updates = new HashMap<>();
				updates.put("longitude", latLng.longitude);
				updates.put("latitude", latLng.latitude);

				// Old Marker is Cleared
				mapView.clear();
				// New Marker is Placed
				mapView.addMarker(new MarkerOptions().position(latLng).title("Exchange Location"));

				// Update new location in current user's Database
				FirebaseFirestore.getInstance().collection("UserProfiles").document(auth.getCurrentUser().getUid()).set(updates, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>(){
					@Override public void onSuccess(Void unused){
						Log.d(LOG.getDATA_LOG(), "New Co-Ordinates Uploaded to Database");
						Filled = true; // Mark that user has filled his location
					}
				});
			}
		});

		// Submit Button Click Listener
		getButton.setClickable(true);
		getButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Submit Button Clicked");
				// If Filled... Redirect to next page
				if(Filled){
					// Update All user's Items in the database.
					new ExchangeItemLocationUpdate(auth.getCurrentUser().getUid(), NewLatitude, NewLongitude);
					new DonateItemLocationUpdate(auth.getCurrentUser().getUid(), NewLatitude, NewLongitude);
					// Idfrom == null means its a new user... so take him/her to Home Page
					if(Idfrom == null){
						Intent i = new Intent(SignUpMapInfoPage.this, HomePage.class);
						startActivity(i);
						finish();
					}
					// Idfrom != null means user was updating his location... So take him back to his profile display page.
					else{
						Intent i = new Intent(SignUpMapInfoPage.this, ProfileDisplay.class);
						i.putExtra("id", Idfrom);
						startActivity(i);
						finish();
					}
				}
			}
		});
	}

	// On Pressing Back Button
	@Override public void onBackPressed(){
		Log.d(LOG.getLOCAL_LOG(), "Back Pressed.");
		// If nothing is Entered but there is something already in the database...
		if(OriginalLatitude != null){
			// Give Alert that all the progress will be lost as they have not submitted
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.MapBackTitle).setMessage(R.string.MapBackText).setNegativeButton(R.string.DeleteItemNo, new DialogInterface.OnClickListener(){
				@Override public void onClick(DialogInterface dialog, int which){
					// On Pressing no... Alert is cancelled
					dialog.cancel();
				}
			}).setPositiveButton(R.string.DeleteItemYes, new DialogInterface.OnClickListener(){
				@Override public void onClick(DialogInterface dialog, int which){
					Log.d(LOG.getLOCAL_LOG(), "Revert back to Original Co-Ordinates.");
					// Put back the Original Coordinates
					Map<String, Object> updates = new HashMap<>();
					updates.put("longitude", OriginalLongitude);
					updates.put("latitude", OriginalLatitude);
					FirebaseFirestore.getInstance().collection("UserProfiles").document(auth.getCurrentUser().getUid()).set(updates, SetOptions.merge());
					// Programs is taken back
					SignUpMapInfoPage.super.onBackPressed();
				}
			}).show();
			// Create the Alert
			alert.create();
		}
		// If new user tries to press back... Ask them to enter location
		else{
			Log.d(LOG.getLOCAL_LOG(), "Location Not Filled... Back Press not Accepted");
			Toast.makeText(this, "Enter Your Location", Toast.LENGTH_SHORT).show();
		}
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "User Sign Up Map Page Destroyed");
		super.onDestroy();
	}
}