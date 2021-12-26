package com.parthtrap.donationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.parthtrap.donationapp.AuthenticationPages.SignUpMapInfoPage;
import com.parthtrap.donationapp.HelperApadters.DonateItemDisplayAdapter;
import com.parthtrap.donationapp.HelperApadters.ExchangeItemDisplayAdapter;
import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.HelperClasses.UserInfoHelper;
import com.parthtrap.donationapp.UserPortal.ExchangeItemViewPage;

import java.util.HashMap;

public class ProfileDisplay extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("MAIN_ACTIVITY_PAGE_LOGS");

	// Front End Components Defined
	TextView NameBox, AddressBox, PhoneBox, EmailBox, RatingDisplay;
	ExchangeItemDisplayAdapter adapter;
	DonateItemDisplayAdapter adapter2;
	LinearLayout RatingLayout;
	EditText ReviewBox;
	RatingBar RatingBox;
	Button SubmitRating, LocationChange, LogOutButton;

	// Connecting to firebase Authentication and database
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	CollectionReference userCollection = db.collection("UserProfiles");
	CollectionReference userCollections = db.collection("UserProfiles");
	CollectionReference itemCollection = db.collection("ExchangeItems");
	CollectionReference itemCollection2 = db.collection("DonateItems");

	// Defining Variables
	float NewRating = 0;
	int RatingCount = 0;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_display);

		// Get Intent from previous page
		Intent i = getIntent();
		String id = i.getStringExtra("id");

		Log.d(LOG.getPAGES_LOG(), "Profile Display Page Created");
		Log.d(LOG.getLOCAL_LOG(), "Profile Display Page Created");

		// Connecting Front end to back end via IDs
		NameBox = findViewById(R.id.NameDisplayDisplayPage);
		AddressBox = findViewById(R.id.AddressDisplayDisplayPage);
		PhoneBox = findViewById(R.id.PhoneDisplayDisplayPage);
		EmailBox = findViewById(R.id.EmailDisplayDisplayPage);
		LogOutButton = findViewById(R.id.LogOutButton);
		RatingLayout = findViewById(R.id.RatingLayoutProfilePage);
		ReviewBox = findViewById(R.id.ReviewBarProfilePage);
		RatingBox = findViewById(R.id.RatingBarProfilePage);
		RatingBox.setStepSize(1);
		RatingDisplay = findViewById(R.id.RatingDisplayDisplayPage);
		SubmitRating = findViewById(R.id.RatingSubmitButtonProfilePage);
		LocationChange = findViewById(R.id.LocationChangeButtonProfilePage);

		// If you are viewing someone else's Profile... Show the Rating Option
		// else show Logout Button and Location Show Button
		if(id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
			RatingLayout.setVisibility(View.INVISIBLE);
		}else{
			LocationChange.setVisibility(View.INVISIBLE);
			LogOutButton.setVisibility(View.INVISIBLE);
		}

		// Displaying Data on screen
		userCollection.document(id).addSnapshotListener(this, new EventListener<DocumentSnapshot>(){
			@Override public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error){
				Log.d(LOG.getDATA_LOG(), "User Data Received");
				if(error != null)
					finish();

				// Display item info in text Box
				assert value != null;
				UserInfoHelper DisplayUser = value.toObject(UserInfoHelper.class);
				NameBox.setText(DisplayUser.getName());
				AddressBox.setText(DisplayUser.getAddress());
				EmailBox.setText(DisplayUser.getEmailId());
				RatingDisplay.setText(DisplayUser.getRating() + "");

				if(DisplayUser.getPublicPhone())
					PhoneBox.setText(DisplayUser.getPhoneNumber());
				else
					PhoneBox.setText("[Hidden]");
			}
		});

		// Log Out Button On Click Listener
		LogOutButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Sign Out Button Pressed");
				Intent i = new Intent(ProfileDisplay.this, MainActivity.class);
				startActivity(i);
				FirebaseAuth.getInstance().signOut();
				finish();
			}
		});

		// Get User's Exchange items using query and update the Adapter
		FirestoreRecyclerOptions<ExchangeItemClass> options = new FirestoreRecyclerOptions.Builder<ExchangeItemClass>().setQuery(itemCollection.whereEqualTo("ownerID", id), ExchangeItemClass.class).build();
		adapter = new ExchangeItemDisplayAdapter(options);
		RecyclerView recyclerView = findViewById(R.id.ItemListRecyclerViewProfilePage);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);

		// Get User's Donation items using query and update the Adapter
		FirestoreRecyclerOptions<DonateItemClass> options2 = new FirestoreRecyclerOptions.Builder<DonateItemClass>().setQuery(itemCollection2.whereEqualTo("ownerID", id), DonateItemClass.class).build();
		adapter2 = new DonateItemDisplayAdapter(options2);
		RecyclerView recyclerView2 = findViewById(R.id.DonateItemListRecyclerViewProfilePage);
		recyclerView2.setHasFixedSize(true);
		recyclerView2.setLayoutManager(new LinearLayoutManager(this));
		recyclerView2.setAdapter(adapter2);

		// On Clicking Item... Take to Item's Info Display
		adapter.setOnItemClickListener(new ExchangeItemDisplayAdapter.OnItemClickListener(){
			@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
				Log.d(LOG.getLOCAL_LOG(), "Clicked on Exchange Item... Display Details");
				Intent intent = new Intent(ProfileDisplay.this, ExchangeItemViewPage.class);
				intent.putExtra("id", documentSnapshot.getId());
				intent.putExtra("idfrom", id);
				startActivity(intent);
				finish();
			}
		});

		// On Donation Item Display
		adapter2.setOnItemClickListener(new DonateItemDisplayAdapter.OnItemClickListener(){
			@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
				// If you are the Owner... Ask to Delete.
				if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(id)){
					AlertDialog.Builder alert = new AlertDialog.Builder(ProfileDisplay.this);
					alert.setTitle(R.string.DeleteItemTitle).setMessage(R.string.DeleteItemText).setNegativeButton(R.string.DeleteItemNo, new DialogInterface.OnClickListener(){
						@Override public void onClick(DialogInterface dialog, int which){
							dialog.cancel(); // Just go back
						}
					}).setPositiveButton(R.string.DeleteItemYes, new DialogInterface.OnClickListener(){
						@Override public void onClick(DialogInterface dialog, int which){
							itemCollection2.document(documentSnapshot.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
								@Override public void onSuccess(DocumentSnapshot documentSnapshot){
									//	Delete the Item as well as the Image from the database
									Log.d(LOG.getLOCAL_LOG(), "Clicked on Delete Item... Deleting Item");
									FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imageURL")).delete();
									itemCollection2.document(documentSnapshot.getId()).delete();
									Log.d(LOG.getDATA_LOG(), "Item Deleted");

								}
							});
						}
					}).show();
					alert.create();
				}
				// If not owner... You cant do anything
				else{
					Log.d(LOG.getLOCAL_LOG(), "Donation Item Clicked... Not the Owner");
					Toast.makeText(ProfileDisplay.this, "Item Clicked", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Rating Button onClick
		SubmitRating.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Rating Submitted");
				if(RatingBox.getRating() != 0 && !ReviewBox.getText().toString().trim().isEmpty()){
					HashMap<String, Object> ratingMap = new HashMap<>();
					ratingMap.put("Rating", RatingBox.getRating());
					ratingMap.put("Review", ReviewBox.getText().toString().trim());
					userCollection.document(id).collection("Ratings").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(ratingMap).addOnSuccessListener(new OnSuccessListener<Void>(){
						@Override public void onSuccess(Void unused){
							Log.d(LOG.getDATA_LOG(), "Rating Uploaded");
						}
					});
					// All item rating to be updated
					RefreshUserRatings(id);
				}
			}
		});

		// Location Change OnCLick
		LocationChange.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Location Change Button Clicked");
				Intent intent = new Intent(ProfileDisplay.this, SignUpMapInfoPage.class);
				intent.putExtra("idfrom", id);
				startActivity(intent);
				finish();
			}
		});
	}

	// Rating Update Function
	private void RefreshUserRatings(String id){
		NewRating = 0;
		RatingCount = 0;

		userCollections.document(id).collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
			@Override public void onSuccess(QuerySnapshot queryDocumentSnapshots){
				Log.d(LOG.getDATA_LOG(), "User Rating Updated");
				for(DocumentSnapshot snapshot : queryDocumentSnapshots){
					NewRating += snapshot.getLong("Rating").intValue();
					RatingCount++;
				}
				NewRating = NewRating / RatingCount;
				if(RatingCount == 0)
					userCollections.document(id).update("rating", 0);
				else
					userCollections.document(id).update("rating", NewRating);
				// User's Items Rating Updating
				RefreshItemRatings(id);
			}

		});

	}

	// All user's Item to be refreshed
	private void RefreshItemRatings(String id){

		// Getting all User's Items
		itemCollection.whereEqualTo("ownerID", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
			@Override public void onSuccess(QuerySnapshot queryDocumentSnapshots){
				Log.d(LOG.getDATA_LOG(), "User ItemsRating Updated");
				for(DocumentSnapshot snapshot : queryDocumentSnapshots){
					itemCollection.document(snapshot.getId()).update("rating", NewRating);
				}
			}
		});
	}

	// Starting Adapters on Start
	@Override protected void onStart(){
		super.onStart();
		adapter.startListening();
		adapter2.startListening();
	}

	// Stopping Adapters on Pause
	@Override protected void onPause(){
		super.onPause();
		adapter.stopListening();
		adapter2.stopListening();
	}
	@Override protected void onDestroy(){
		super.onDestroy();
		Log.d(LOG.getPAGES_LOG(), "Profile Display Page Destroyed");
	}
}