/*
Call This Function when you want to Update the Coordinates onto Database.
*/

package com.parthtrap.donationapp.HelperFunctions;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;

public class ExchangeItemLocationUpdate{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("EXCHANGE_ITEM_LOCATION_UPDATE_LOGS");

	public ExchangeItemLocationUpdate(String UID, double latitude, double longitude){

		// Updating Location to Database
		FirebaseFirestore.getInstance().collection("ExchangeItems").whereEqualTo("ownerID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
			@Override public void onSuccess(QuerySnapshot queryDocumentSnapshots){
				for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
					FirebaseFirestore.getInstance().collection("ExchangeItems").document(snapshot.getId()).update("longitude", longitude);
					FirebaseFirestore.getInstance().collection("ExchangeItems").document(snapshot.getId()).update("latitude", latitude);
				}
				Log.d(LOG.getLOCAL_LOG(), "All Exchange Items owned by the User have been updated to new location");
			}
		});
	}
}
