package com.parthtrap.donationapp.UserPortal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.ProfileDisplay;
import com.parthtrap.donationapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ExchangeItemViewPage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("EXCHANGE_ITEM_DISPLAY_PAGE_LOGS");

	// Defining Front End Components
	TextView ItemNameBox, ItemDescBox, ItemCategoryBox, ItemWantForBox, ItemOwnerBox;
	ImageView ItemImageBox;
	ImageButton DeleteItemButton;
	Button InterestedButton;

	// Defining Variables
	String ItemID;
	String BackID = "";
	String OwnerID;

	// Connecting to firebase Authentication and database
	FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
	FirebaseAuth auth = FirebaseAuth.getInstance();

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange_item_view);

		Log.d(LOG.getPAGES_LOG(), "Exchange Item Display Page Created");
		Log.d(LOG.getLOCAL_LOG(), "Exchange Item Display Page Created");

		// Connecting Front end to back end via IDs
		ItemNameBox = findViewById(R.id.ExchangeItemNameDisplay);
		ItemDescBox = findViewById(R.id.ExchangeItemDescDisplay);
		ItemCategoryBox = findViewById(R.id.ExchangeItemCategoryDisplay);
		ItemWantForBox = findViewById(R.id.ExchangeItemWantForDisplay);
		ItemOwnerBox = findViewById(R.id.ExchangeItemOwnerDisplay);
		ItemImageBox = findViewById(R.id.ExchangeItemImageDisplay);
		DeleteItemButton = findViewById(R.id.DeleteButtonExchangeItemDisplay);
		InterestedButton = findViewById(R.id.ExchangeItemInterestedButton);

		// Set Button Visibility to INVISIBLE
		DeleteItemButton.setVisibility(View.INVISIBLE);
		InterestedButton.setVisibility(View.INVISIBLE);

		// Get Intent from previous page
		Intent i = getIntent();
		ItemID = i.getStringExtra("id");
		BackID = i.getStringExtra("idfrom");

		// Displaying Data on screen
		fsdb.collection("ExchangeItems").document(ItemID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@Override public void onSuccess(DocumentSnapshot documentSnapshot){
				Log.d(LOG.getDATA_LOG(), "Item Data Received");
				ExchangeItemClass DisplayItem = documentSnapshot.toObject(ExchangeItemClass.class);
				// Display item info in text Box
				OwnerID = DisplayItem.getOwnerID();
				ItemNameBox.setText(DisplayItem.getName().substring(0, 1).toUpperCase() + DisplayItem.getName().substring(1));
				ItemDescBox.setText(DisplayItem.getDescription());
				ItemCategoryBox.setText(DisplayItem.getCategory());
				ItemWantForBox.setText(DisplayItem.getWantFor());
				// Display Image using Picasso
				Picasso.get().load(DisplayItem.getImageURL()).into(ItemImageBox);
				// Delete button if Owner looks at the item... else interested Button
				if(OwnerID.equals(auth.getCurrentUser().getUid()))
					DeleteItemButton.setVisibility(View.VISIBLE);
				else
					InterestedButton.setVisibility(View.VISIBLE);
			}
		}).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
			@Override public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task){
				fsdb.collection("UserProfiles").document(OwnerID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
					@Override public void onSuccess(DocumentSnapshot documentSnapshot){
						// Setting Owner Name using the Owner ID attached to the Item object
						Log.d(LOG.getDATA_LOG(), "Item Owner Data received");
						ItemOwnerBox.setText(documentSnapshot.getString("name"));
					}
				});
			}
		});

		// Onclick on Item Owner to redirect to his/her profile
		ItemOwnerBox.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Clicked on Owner... redirecting to Profile Page");
				Intent i = new Intent(ExchangeItemViewPage.this, ProfileDisplay.class);
				i.putExtra("id", OwnerID);
				startActivity(i);
			}
		});

		// Alert Dialogue on Clicking Delete Button
		DeleteItemButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Item Delete Press");
				AlertDialog.Builder alert = new AlertDialog.Builder(ExchangeItemViewPage.this);
				alert.setTitle(R.string.DeleteItemTitle).setMessage(R.string.DeleteItemText).setNegativeButton(R.string.DeleteItemNo, new DialogInterface.OnClickListener(){
					@Override public void onClick(DialogInterface dialog, int which){
						dialog.cancel(); // Just go back
					}
				}).setPositiveButton(R.string.DeleteItemYes, new DialogInterface.OnClickListener(){
					@Override public void onClick(DialogInterface dialog, int which){
						fsdb.collection("ExchangeItems").document(ItemID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
							@Override public void onSuccess(DocumentSnapshot documentSnapshot){
								//	Delete the Item as well as the Image from the database
								FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imageURL")).delete();
								Log.d(LOG.getDATA_LOG(), "Item Deleted");
								if(!BackID.equals("null")){
									// Go back to the profile page from where you were redirected here
									Intent i = new Intent(ExchangeItemViewPage.this, ProfileDisplay.class);
									i.putExtra("id", BackID);
									startActivity(i);
									fsdb.collection("ExchangeItems").document(ItemID).delete();
									finish();
								}else{
									//	Go back to Home page
									Intent i = new Intent(ExchangeItemViewPage.this, HomePage.class);
									startActivity(i);
									fsdb.collection("ExchangeItems").document(ItemID).delete();
									finish();
								}
							}
						});
					}
				}).show();
				alert.create();
			}
		});
	}

	@Override public void onBackPressed(){
		Log.d(LOG.getLOCAL_LOG(), "Back Button Pressed");
		if(!BackID.equals("null")){
			// Go back to the profile page from where you were redirected here
			Intent i = new Intent(ExchangeItemViewPage.this, ProfileDisplay.class);
			i.putExtra("id", BackID);
			startActivity(i);
			finish();
		}else{
			//	Go back where ever you came from
			super.onBackPressed();
		}

	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "Exchange Item View Page Destroyed");
		super.onDestroy();
	}
}